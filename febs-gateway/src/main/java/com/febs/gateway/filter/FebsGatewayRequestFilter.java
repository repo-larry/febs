package com.febs.gateway.filter;

import com.febs.common.entity.FebsConstant;
import com.febs.common.entity.FebsResponse;
import com.febs.common.utils.FebsUtil;
import com.febs.gateway.properties.FebsGatewayProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Base64Utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @description: 通过请求上下文RequestContext获取了转发的服务名称serviceId和请求对象HttpServletRequest，并打印请求日志。随后往请求上下文的头部添加了Key为ZuulToken，Value为febs:zuul:123456的信息
 * @date: 2022/9/18
 **/
@Slf4j
@Component
public class FebsGatewayRequestFilter extends ZuulFilter {

    @Autowired
    private FebsGatewayProperties properties;

    private AntPathMatcher pathMatcher = new AntPathMatcher();

    /*
     * @param:
     * @return: java.lang.String
     * @description: 对应Zuul生命周期的四个阶段：pre、post、route和error，我们要在请求转发出去前添加请求头，所以这里指定为pre
     * @date: 2022/9/18
     */
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    /*
     * @param:
     * @return: int
     * @description: PreDecorationFilter过滤器的优先级为5，所以我们可以指定为6让我们的过滤器优先级比它低 PreDecorationFilter用于处理请求上下文
     * @date: 2022/9/18
     */
    @Override
    public int filterOrder() {
        return 6;
    }

    /*
     * @param:
     * @return: boolean
     * @description: 是否执行该过滤器的run方法
     * @date: 2022/9/18
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        String serviceId = (String) ctx.get(FilterConstants.SERVICE_ID_KEY);
        HttpServletRequest request = ctx.getRequest();
        String host = request.getRemoteHost();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        log.info("请求URI：{}，HTTP Method：{}，请求IP：{}，ServerId：{}", uri, method, host, serviceId);

        // 禁止外部访问资源实现
        boolean shouldForward = true;
        String forbidRequestUri = properties.getForbidRequestUri();
        String[] forbidRequestUris = StringUtils.splitByWholeSeparatorPreserveAllTokens(forbidRequestUri, ",");
        if (forbidRequestUris != null && ArrayUtils.isNotEmpty(forbidRequestUris)) {
            for (String u : forbidRequestUris) {
                if (pathMatcher.match(u, uri)) {
                    shouldForward = false;
                }
            }
        }
        if (!shouldForward) {
            HttpServletResponse response = ctx.getResponse();
            FebsResponse febsResponse = new FebsResponse().message("该URI不允许外部访问");
            try {

                FebsUtil.makeResponse(
                        response, MediaType.APPLICATION_JSON_UTF8_VALUE,
                        HttpServletResponse.SC_FORBIDDEN, febsResponse
                );
                ctx.setSendZuulResponse(false);
                ctx.setResponse(response);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        byte[] token = Base64Utils.encode((FebsConstant.ZUUL_TOKEN_VALUE).getBytes());
        ctx.addZuulRequestHeader(FebsConstant.ZUUL_TOKEN_HEADER, new String(token));
        return null;
    }
}
