package com.febs.system.aspect;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @description:
 * @date: 2022/9/27
 **/
@Aspect
@Component
@Slf4j
public class LogAspect {

    private static final String dateFormat = "yyyy-MM-dd HH:mm:ss";
    private static final String STRING_START = "\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n";
    private static final String STRING_END = "\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n";

    //设置切入点
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void serviceLog() {
    }

    /**
     * 切换方法，记录日志
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("serviceLog()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Class<?> targetClass = method.getDeclaringClass();

//        StringBuffer classAndMethod = new StringBuffer();

//        Log classAnnotation = targetClass.getAnnotation(Log.class);
//        Log methodAnnotation = method.getAnnotation(Log.class);
//
//        if (classAnnotation != null) {
//            if (classAnnotation.ignore()) {
//                return joinPoint.proceed();
//            }
//            classAndMethod.append(classAnnotation.value()).append("-");
//        }
//
//        if (methodAnnotation != null) {
//            if (methodAnnotation.ignore()) {
//                return joinPoint.proceed();
//            }
//            classAndMethod.append(methodAnnotation.value());
//        }

        String target = targetClass.getName() + "#" + method.getName();

        String params;
        params = JSONObject.toJSONStringWithDateFormat(joinPoint.getArgs(), dateFormat, SerializerFeature.WriteMapNullValue);

        log.info(STRING_START + "开始调用--> {} 参数:{}", target, params);

        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long timeConsuming = System.currentTimeMillis() - start;

        log.info("\n调用结束<-- {} 返回值:{} 耗时:{}ms" + STRING_END, target, JSONObject.toJSONStringWithDateFormat(result, dateFormat, SerializerFeature.WriteMapNullValue), timeConsuming);
        return result;
    }
}