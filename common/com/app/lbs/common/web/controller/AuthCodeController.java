/*
 * Copyleft 2012 Power by colen.
 *
 * Project: app-crm
 * Date: Mar 17, 2012
 */
package com.app.lbs.common.web.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * AuthCode Controller
 *
 * @author colen
 *
 */
@Controller(value="common_authCodeController")
public class AuthCodeController extends WebCommonController {

    /** Logger */
    private static final Logger LOG = Logger.getLogger(AuthCodeController.class);
    
    /** Code  */
    private static final String CDS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwzxy1234567890";
    private static final Random R = new Random();
    private static final int MAX_LEN = 5;
    private static final int MIN_LEN = 4;

    /**
     * Pre-New Auth Code
     *
     * @param response Response
     * @throws IOException
     */
    @RequestMapping("common/pnew_auth_code.shtm")
    public void preNewAuthCode(HttpServletResponse response) throws IOException {
        StringBuilder bud = new StringBuilder();

        int len = MIN_LEN + R.nextInt(MAX_LEN - MIN_LEN + 1);
        for (int i = 0; i < len; i++) {
            bud.append(CDS.charAt(R.nextInt(CDS.length())));
        }

        // store to cookie and encrypt
        setCookie(response, "auth_code", bud.toString(), -1, true);

        Map<String, Object> result = getDefaultResult();
        outputJSON(response, result);
    }

    /**
     * create a new AuthCode
     *
     * @param response Response
     * @throws IOException IOException
     */
    @RequestMapping("common/new_auth_code.shtm")
    public void newAuthCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOG.info("new authcode begin...");

        response.setContentType("image/jpeg");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        int width = 200;
        int height = 80;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Arial", Font.PLAIN, 60));
        g.setColor(getRandColor(160, 200));
        int x, y;
        for (int i = 0; i < 300; i++) {
            x = R.nextInt(width);
            y = R.nextInt(height);
            g.drawLine(x, y, x + R.nextInt(50), y + R.nextInt(50));
        }

        // auth code
        String authcode = getCookieValue(request, "auth_code", true);
        int len = authcode.length();
        int dx = 15 * (MAX_LEN + 1 - len);
        for (int i = 0; i < len; i++) {
            g.setColor(new Color(
                    20 + R.nextInt(110),
                    20 + R.nextInt(110),
                    20 + R.nextInt(110)));
            x = dx + 32 * i;
            y = 70 + 20 * (R.nextInt(2) - 1);
            g.drawString(String.valueOf(authcode.charAt(i)), x, y);
        }
        g.dispose();

        LOG.info("authcode created (" + authcode + ")");

        OutputStream output = response.getOutputStream();
        ImageIO.write(image, "jpg", output);
        output.flush();
        output.close();

        LOG.info("new authcode end.");
    }

    private Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) fc = 255;
        if (bc > 255) bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }
}
