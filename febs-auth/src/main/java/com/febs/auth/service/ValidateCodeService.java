package com.febs.auth.service;

import com.febs.auth.properties.FebsAuthProperties;
import com.febs.auth.properties.FebsValidateCodeProperties;
import com.febs.common.entity.FebsConstant;
import com.febs.common.exception.ValidateCodeException;
import com.febs.common.service.RedisService;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @description:
 * @date: 2022/9/18
 **/
@Service
public class ValidateCodeService {

    @Autowired
    private RedisService redisService;
    @Autowired
    private FebsAuthProperties properties;

    /**
     * 生成验证码
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    public void create(HttpServletRequest request, HttpServletResponse response) throws IOException, ValidateCodeException {
        String key = request.getParameter("key");
        if (StringUtils.isBlank(key)) {
            throw new ValidateCodeException("验证码key不能为空");
        }
        FebsValidateCodeProperties code = properties.getCode();
        setHeader(response, code.getType());

        Captcha captcha = createCaptcha(code);
        redisService.set(FebsConstant.CODE_PREFIX + key, StringUtils.lowerCase(captcha.text()), code.getTime());
        captcha.out(response.getOutputStream());
    }

    /**
     * 校验验证码
     *
     * @param key   前端上送 key
     * @param value 前端上送待校验值
     */
    public void check(String key, String value) throws ValidateCodeException {
        Object codeInRedis = redisService.get(FebsConstant.CODE_PREFIX + key);
        if (StringUtils.isBlank(value)) {
            throw new ValidateCodeException("请输入验证码");
        }
        if (codeInRedis == null) {
            throw new ValidateCodeException("验证码已过期");
        }
        if (!StringUtils.equalsIgnoreCase(value, String.valueOf(codeInRedis))) {
            throw new ValidateCodeException("验证码不正确");
        }
    }

    /*
     * @param: code
     * @return: com.wf.captcha.base.Captcha
     * @description: 生成验证码。在前后端不分离的架构下，我们通过浏览器传输的jsessionid来和验证码图片一一对应，但前后的分离的模式下，客户端发送的请求并没有携带jsessionid（因为不再基于Session），所以我们需要客户端在发送获取验证码请求的时候，携带一个key（比如按一定算法生成的随机字符串，模拟jsessionid）来和验证码一一对应。于是我们在create里一开始就从请求中获取key值，然后根据验证码配置文件生成验证码，并将验证码字符保存到了Redis中（Redis Key为febs.captcha. + 客户端上送的key值，有效时间为配置文件定义的120秒），并将验证码图片以流的形式返回给客户端
     * @date: 2022/9/18
     */
    private Captcha createCaptcha(FebsValidateCodeProperties code) {
        Captcha captcha = null;
        if (StringUtils.equalsIgnoreCase(code.getType(), FebsConstant.GIF)) {
            captcha = new GifCaptcha(code.getWidth(), code.getHeight(), code.getLength());
        } else {
            captcha = new SpecCaptcha(code.getWidth(), code.getHeight(), code.getLength());
        }
        captcha.setCharType(code.getCharType());
        return captcha;
    }

    private void setHeader(HttpServletResponse response, String type) {
        if (StringUtils.equalsIgnoreCase(type, FebsConstant.GIF)) {
            response.setContentType(MediaType.IMAGE_GIF_VALUE);
        } else {
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
        }
        response.setHeader(HttpHeaders.PRAGMA, "No-cache");
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache");
        response.setDateHeader(HttpHeaders.EXPIRES, 0L);
    }
}
