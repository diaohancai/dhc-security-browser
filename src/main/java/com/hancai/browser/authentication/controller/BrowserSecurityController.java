package com.hancai.browser.authentication.controller;

import com.hancai.core.config.DhcSecurityCoreProperties;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author diaohancai
 */
@RestController
@RequestMapping("/api/browser-security")
public class BrowserSecurityController {

    private RequestCache requestCache = new HttpSessionRequestCache();

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private DhcSecurityCoreProperties dhcSecurityCoreProperties;

    @Autowired
    public BrowserSecurityController(DhcSecurityCoreProperties dhcSecurityCoreProperties) {
        this.dhcSecurityCoreProperties = dhcSecurityCoreProperties;
    }

    /**
     * 根据访问资源，确定响应<br/>
     * 如果是访问页面，且需要认证，则跳转到登录页面<br/>
     * 如果是访问非页面，且需要认证，则响应401未授权
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/authentication-require")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String authenticationRequire(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SavedRequest savedRequest = requestCache.getRequest(request, response);

        if(savedRequest != null) {
            String targetUrl = savedRequest.getRedirectUrl();
            if(StringUtils.endsWith(targetUrl, ".html")) {
                redirectStrategy.sendRedirect(request, response, dhcSecurityCoreProperties.getBrowser().getLoginPage());
            }
        }

        return "未认证，请先登录";
    }

}
