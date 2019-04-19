package com.hancai.browser.kaptcha.rest;

import com.google.code.kaptcha.Producer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author diaohancai
 */
@RestController
@RequestMapping("/api/kaptcha")
public class KaptchaResource {

    public static String KAPTCHA_SESSION_KEY = "kaptcha";

    @Resource(name = "kaptchaProducer")
    private Producer kaptchaProducer;

    /**
     * 获取验证码图片
     *
     * @param request
     * @param response
     * @param session
     * @throws IOException
     */
    @GetMapping
    public void getImage(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException {
        String text = kaptchaProducer.createText(); // 生成验证码文本
        session.setAttribute(KAPTCHA_SESSION_KEY, text); // 验证码文本放入session中
        BufferedImage kaptchaImage = kaptchaProducer.createImage(text); // 生成验证码图片

        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate"); // 不使用缓存
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg"); // image/jpeg输出格式

        ServletOutputStream servletOutputStream = response.getOutputStream();
        ImageIO.write(kaptchaImage, "jpg", servletOutputStream);

        try {
            servletOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            servletOutputStream.close();
        }
    }

}
