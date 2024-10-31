package com.exe.sharkauction.components.notifications;


import com.exe.sharkauction.components.events.MailEvent;
import com.exe.sharkauction.repositories.IUserRepository;
import jakarta.mail.*;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


import java.util.Properties;

@Component
public class MailService {
    private static final String CONTENT_TYPE_TEXT_HTML = "text/html;charset=\"utf-8\"";

    @Value("${app.mail.host}")
    private String host;
    @Value("${app.mail.port}")
    private String port;
    @Value("${app.mail.username}")
    private String email;
    @Value("${app.mail.password}")
    private String password;
    private JavaMailSender javaMailSender;
    private IUserRepository userRepository;
    @Autowired
    ThymeleafService thymeleafService;

    @Async
    @EventListener
    public void sendMail(MailEvent event) {
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(email, password);
                    }
                });
        Message message = new MimeMessage(session);

        try {
            if (event.getType().equals("verify")) {
                message.setRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(event.getUser().getEmail())});

                message.setFrom(new InternetAddress(email));
                message.setSubject("Xác thực tài khoản");
                message.setContent(thymeleafService.getVerifyContent(event.getUser(), event.getUrl()), CONTENT_TYPE_TEXT_HTML);
                Transport.send(message);
            } else if (event.getType().equals("forgot")) {
                message.setRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(event.getUser().getEmail())});

                message.setFrom(new InternetAddress(email));
                message.setSubject("Đặt lại mật khẩu");
                message.setContent(thymeleafService.getResetPasswordContent(event.getUser(), event.getUrl()), CONTENT_TYPE_TEXT_HTML);
                Transport.send(message);
            } else if ("auction_end".equals(event.getType())) {
                message.setRecipients(Message.RecipientType.TO, new InternetAddress[]
                        {new InternetAddress(event.getAuction().getProduct().getSeller().getEmail())});
                message.setFrom(new InternetAddress(email));
                message.setSubject("Kết thúc đấu giá");
                message.setContent(thymeleafService.getAuctionEndContent(event.getAuction()), CONTENT_TYPE_TEXT_HTML);
                Transport.send(message);
            } else if ("auction_win".equals(event.getType())) {
                message.setRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(event.getAuction().getWinner().getEmail())});
                message.setFrom(new InternetAddress(email));
                message.setSubject("Chúc mừng bạn đã thắng đấu giá!");
                message.setContent(thymeleafService.getAuctionWinContent(event.getAuction()), CONTENT_TYPE_TEXT_HTML);
                Transport.send(message);

            } else if ("confirm_product".equals(event.getType())) {
                message.setRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(event.getProduct().getSeller().getEmail())});
                message.setFrom(new InternetAddress(email));
                message.setSubject("Sản phẩm của bạn đã được duyệt!");
                message.setContent(thymeleafService.getConfirmProductContent(event.getProduct()), CONTENT_TYPE_TEXT_HTML);
                Transport.send(message);

            }

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
