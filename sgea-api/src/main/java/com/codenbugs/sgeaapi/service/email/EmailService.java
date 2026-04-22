package com.codenbugs.sgeaapi.service.email;

import com.codenbugs.sgeaapi.entity.student.Student;
import com.codenbugs.sgeaapi.entity.users.User;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

@Service
public class EmailService {

    private final GmailAuthUtil gmailAuthUtil;

    private final SpringTemplateEngine templateEngine;

    @Value("${gmail.api.from-email}")
    private String fromEmail;

    public EmailService(SpringTemplateEngine templateEngine, GmailAuthUtil gmailAuthUtil) {
        this.templateEngine = templateEngine;
        this.gmailAuthUtil = gmailAuthUtil;
    }

    @Async
    public void sendStatusRejectedEmail(User user, String title, String description, boolean isApproved) {
        String acceptText = "Notificación sobre cuenta: Su cuenta ha sido habilitada, puede iniciar sesión. Para cualquier duda comuníquese con un administrador.";
        String rejectedText = "Notificación sobre cuenta: Su cuenta ha sido deshabilitada, comuníquese con un administrador para más información.";
        String subject = isApproved ? acceptText : rejectedText;

        Map<String, Object> model = Map.of(
                "userName", user.getFirstName() + " " + user.getLastName(),
                "title", title,
                "description", description
        );

        sendTemplateEmail(user, subject, "confirmation-email", model, null);
    }

    @Async
    public void sendEquivalencyStatusEmail(Student student, boolean isApproved, String comment) {
        User user = student.getUser();

        String subject = isApproved ? "Equivalencia aprobada" : "Equivalencia rechazada";
        String title = isApproved ? "Tu solicitud de equivalencia fue aprobada" : "Tu solicitud de equivalencia fue rechazada";
        String description = isApproved
                ? "Tu solicitud de equivalencia ha sido aprobada exitosamente."
                : "Tu solicitud de equivalencia ha sido rechazada. Motivo: "
                + (comment != null ? comment : "No especificado");

        Map<String, Object> model = Map.of(
                "userName", user.getFirstName() + " " + user.getLastName() + ", carnet: " + student.getCarnet(),
                "title", title,
                "description", description
        );

        sendTemplateEmail(user, subject, "confirmation-email", model, null);
    }

    @Async
    protected void sendTemplateEmail(User user, String subject, String templateName, Map<String, Object> model, Date deliveryDate) {

        try {
            Context context = new Context();
            context.setVariables(model);
            String html = templateEngine.process(templateName, context);

            // 🔹 Enviar con Resend (HTTP)
            sendWithGmail(user.getEmail(), subject, html);

        } catch (Exception e) {
            System.err.println("FALLO AL ENVIAR CORREO: " + e.getMessage());
        }
    }

    private void sendWithGmail(String to, String subject, String html) {
        try {
            Gmail service = gmailAuthUtil.getGmailService();

            MimeMessage email = createEmail(to, fromEmail, subject, html);
            Message message = createMessageWithEmail(email);

            service.users().messages().send("me", message).execute();

            System.out.println("Correo enviado con Gmail API");

        } catch (Exception e) {
            System.err.println("ERROR enviando correo con Gmail: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private MimeMessage createEmail(String to, String from, String subject, String bodyHtml) throws Exception {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(from));
        email.addRecipient(jakarta.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);
        email.setContent(bodyHtml, "text/html; charset=utf-8");

        return email;
    }

    private Message createMessageWithEmail(MimeMessage email) throws Exception {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] rawBytes = buffer.toByteArray();

        String encodedEmail = Base64.getUrlEncoder().encodeToString(rawBytes);

        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }
}