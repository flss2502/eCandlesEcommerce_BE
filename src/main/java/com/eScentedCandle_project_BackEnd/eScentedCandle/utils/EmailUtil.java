package com.eScentedCandle_project_BackEnd.eScentedCandle.utils;

import com.eScentedCandle_project_BackEnd.eScentedCandle.models.Order;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.enums.OrderStatusEnum;
import com.eScentedCandle_project_BackEnd.eScentedCandle.models.enums.PaymentStatusEnum;
import com.eScentedCandle_project_BackEnd.eScentedCandle.services.impl.OtpService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.security.SecureRandom;

@Component
@RequiredArgsConstructor
public class EmailUtil {

    private final JavaMailSender javaMailSender;
    private final OtpService otpService;
    private final SpringTemplateEngine templateEngine;

    public void sendSetPassword(String email) throws MessagingException {
        String otp = generateOtp(); // Tạo mã OTP

        // Lưu mã OTP vào OtpService
        otpService.saveOtp(email, otp);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Set Password");

        String htmlContent = """
                <html>
                    <head>
                        <style>
                            div {
                                background-color: #f2f2f2;
                                padding: 10px;
                                border-radius: 5px;
                            }
                            a {
                                color: #007bff;
                                text-decoration: none;
                            }
                            a:hover {
                                color: #0056b3;
                            }
                        </style>
                    </head>
                    <body>
                        <div>
                            <h1>OTP xac thuc</h1>
                            <p>Your OTP is: %s</p>
                        </div>
                    </body>
                </html>
                """.formatted(otp);

        mimeMessageHelper.setText(htmlContent, true);
        javaMailSender.send(mimeMessage);
    }


    public void sendOrderStatusUpdate(String email, Order order) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Order Status Update");

        // Prepare the context for Thymeleaf
        Context context = new Context();
        context.setVariable("order", order);
//        context.setVariable("orderStatus", orderStatus.toString());
//        context.setVariable("paymentStatus", paymentStatus.toString());

        // Process the HTML template
        String htmlContent = templateEngine.process("emailTemplate", context);

        mimeMessageHelper.setText(htmlContent, true);
        javaMailSender.send(mimeMessage);
    }

    public void sendPaymentConfirmation(String email, Order order) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Xác nhận thanh toán đơn hàng " + order.getId());

        // Prepare the context for Thymeleaf
        Context context = new Context();
        context.setVariable("order", order);

        // Process the HTML template
        String htmlContent = templateEngine.process("payment-confirmation", context);

        mimeMessageHelper.setText(htmlContent, true);
        javaMailSender.send(mimeMessage);
    }

    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        int otp = random.nextInt(900000) + 100000;
        return String.valueOf(otp);
    }
}
