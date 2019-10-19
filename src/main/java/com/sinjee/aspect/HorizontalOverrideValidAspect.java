package com.sinjee.aspect;

import com.sinjee.annotation.HorizontalOverrideValid;
import com.sinjee.tools.HashUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author 小小极客
 * 时间 2019/10/18 23:33
 * @ClassName HorizontalOverrideValidAspect
 * 描述 横向越权 切面
 * Aspect 使用例子
 **/
@Slf4j
@Aspect
@Component
public class HorizontalOverrideValidAspect {

    /**
     * 定义切入点
     */
    @Pointcut("@annotation(com.sinjee.annotation.HorizontalOverrideValid)")
    public void validPointcut(){}

    /**
     * 环绕通知 进行校验
     * @param proceedingJoinPoint
     * @return
     */
    @Around("validPointcut()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint){

        MethodSignature signature = (MethodSignature)proceedingJoinPoint.getSignature() ;
        Method method = signature.getMethod() ;

        //取得方法上的注解
        HorizontalOverrideValid horizontalOverrideValid = method.getAnnotation(HorizontalOverrideValid.class) ;
        String returnUrl = "" ;
        if (null != horizontalOverrideValid){
            returnUrl = horizontalOverrideValid.returnUrl() ;
        }

        //从后台取出用户的ID
        String userId = "" ;
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String passKey = request.getParameter("passkey") ;

        //后台随机生成一个keys[useId+编号+随机号]
        String passKeyString = userId + "keys" ;
        String passKeyCreate = HashUtil.signKey(passKeyString,"UTF-8") ;

        Object resultObject = null ;
        //比较passKey 和 passKeyCreate
        try {
            if (passKeyCreate.equals(passKey)) {
                resultObject = proceedingJoinPoint.proceed();
            }else {
                String redirectUrl = "redirect:"+returnUrl ;
                resultObject = redirectUrl ;
            }
        } catch (Throwable e) {
            log.error(e.getMessage());
        }

        return resultObject ;

    }
}
