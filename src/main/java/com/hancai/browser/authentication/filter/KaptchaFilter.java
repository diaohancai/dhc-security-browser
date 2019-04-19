package com.hancai.browser.authentication.filter;

import com.hancai.browser.kaptcha.rest.KaptchaResource;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证码校验过滤器
 *
 * @author diaohancai
 */
@Component
public class KaptchaFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("KaptchaFilter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        // 登录才需要拦截
        if(!"/api/login".equals(httpServletRequest.getRequestURI())) {
            chain.doFilter(request, response);
            return ;
        }

        String kaptchaCode = request.getParameter("kaptcha"); // 前端提交的验证码
        String kaptchaSessionCode = (String) httpServletRequest.getSession().getAttribute(KaptchaResource.KAPTCHA_SESSION_KEY); // session中的验证码

        if(StringUtils.isBlank(kaptchaCode)) {
            throw new InsufficientAuthenticationException("服务端没有收到登录验证码");
        }
        if(StringUtils.isBlank(kaptchaSessionCode)) {
            throw new InsufficientAuthenticationException("服务端没有验证码生成异常");
        }
        if(!kaptchaCode.equalsIgnoreCase(kaptchaSessionCode)) {
            throw new InsufficientAuthenticationException("验证码错误");
        }

        httpServletRequest.getSession().removeAttribute(KaptchaResource.KAPTCHA_SESSION_KEY); // 清掉旧的验证码
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        logger.info("KaptchaFilter destroy");
    }

}
