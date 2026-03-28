package com.codenbugs.sgeaapi.service.email;

import com.codenbugs.sgeaapi.entity.users.User;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.context.Context;


import java.util.Date;
import java.util.Map;

@Service
public class EmailService {
    private static final String FROM = "CodeNBugsDevOps@hotmail.com";
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    //private final NotificationRepository notificationRepository;

    public EmailService(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        //this.notificationRepository = notificationRepository;
    }

    @Async
    public void sendStatusRejectedEmail(User user, String title, String description, boolean isApproved) {
        String acceptText = "Notificación sobre cuenta: Su cuenta ha sido habilitada, puede iniciar sesión. Para cualquier duda comuníquese con un administrador.";
        String rejectedText = "Notificación sobre cuenta: Su cuenta ha sido deshabilitada, comuníquese con un administrador para más información.";
        String subject = isApproved ? acceptText : rejectedText;

        Map<String, Object> model = Map.of(
                "userName", user.getFirstName() + " " + user.getLastName(),
                "tittle", title,
                "description", description
        );

        sendTemplateEmail(user, "Cuenta", subject, "confirmation-email", model, null);
    }

    @Async
    protected void sendTemplateEmail(User user, String notificationType, String subject, String templateName, Map<String, Object> model, Date deliveryDate) {
        // Añadir entidad notificación

        //notificationRepository.save(notification);

        try {
            Context context = new Context();
            context.setVariables(model);
            String html = templateEngine.process(templateName, context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(html, true);
            helper.setFrom(FROM);

            mailSender.send(message);

           // notification.setMessage(html);
            //notification.setEmailSent(true);

        } catch (Exception e) {
            System.err.println("FALLO AL ENVIAR CORREO: " + e.getMessage());
            //notification.setErrorMessage("ERROR: " + e.getMessage());
            //notification.setEmailSent(false);
        }
        //notificationRepository.save(notification);
    }
}