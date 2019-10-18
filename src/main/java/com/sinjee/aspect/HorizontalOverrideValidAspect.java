package com.sinjee.aspect;

import com.sinjee.annotation.HorizontalOverrideValid;
import com.sinjee.tools.HashUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 小小极客
 * 时间 2019/10/18 23:33
 * @ClassName HorizontalOverrideValidAspect
 * 描述 横向越权 切面
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
     * @param horizontalOverrideValid
     * @return
     */
    @Around(value = "validPointcut() && @annotation(horizontalOverrideValid)")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint, HorizontalOverrideValid horizontalOverrideValid){
        String returnUrl = "" ;
        if (horizontalOverrideValid != null){
            returnUrl = horizontalOverrideValid.returnUrl() ;
        }

        //从后台取出用户的ID
        String userId = "" ;
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String passKey = request.getParameter("passkey") ;
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
