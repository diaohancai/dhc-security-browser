package com.hancai.browser.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.util.Config;
import com.hancai.browser.authentication.filter.KaptchaFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author diaohancai
 */
@Configuration
public class DhcSecurityBrowserConfigurer {

    /**
     * Spring Security 密码加密/解密工具
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // 推荐使用 BCrypt 加密/解密器
        return new BCryptPasswordEncoder();
    }

    @Bean(name = "kaptchaProducer")
    public Producer kaptchaProducer() {
        Properties kaptchaProperties = new Properties();
        kaptchaProperties.put("kaptcha.border", "no");
        kaptchaProperties.put("kaptcha.textproducer.char.length","4");
        kaptchaProperties.put("kaptcha.image.height","50");
        kaptchaProperties.put("kaptcha.image.width","150");
        kaptchaProperties.put("kaptcha.obscurificator.impl","com.google.code.kaptcha.impl.ShadowGimpy");
        kaptchaProperties.put("kaptcha.textproducer.font.color","black");
        kaptchaProperties.put("kaptcha.textproducer.font.size","40");
        kaptchaProperties.put("kaptcha.noise.impl","com.google.code.kaptcha.impl.DefaultNoise");
        kaptchaProperties.put("kaptcha.textproducer.char.string","acdefhkmnprtwxy2345678");

        Config config = new Config(kaptchaProperties);
        return config.getProducerImpl();
    }

    /*@Bean
    public FilterRegistrationBean kaptchaFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        KaptchaFilter kaptchaFilter = new KaptchaFilter();
        filterRegistrationBean.setFilter(kaptchaFilter);

        // 指定过滤 url pattern
        List<String> urlPatterns = new ArrayList<>();
        urlPatterns.add("/api/login");
        filterRegistrationBean.setUrlPatterns(urlPatterns);

        return filterRegistrationBean;
    }*/

    @Bean
    public PersistentTokenRepository persistentTokenRepository(DataSource dataSource) {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource); // 手动注入属性
        return jdbcTokenRepository;
    }

}
