package com.codenbugs.sgeaapi.service.email;

import com.codenbugs.sgeaapi.entity.users.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.Map;

@Service
public class EmailService {
    private static final String FROM = "onboarding@resend.dev";

    @Value("${resend.api.key}")
    private String apiKey;

    //private static final String FROM = "CodeNBugsDevOps@hotmail.com";
    private final SpringTemplateEngine templateEngine;
    private final RestTemplate restTemplate = new RestTemplate();

    public EmailService(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
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

        sendTemplateEmail(user, subject, "confirmation-email", model, null);
    }

    @Async
    protected void sendTemplateEmail(User user, String subject, String templateName, Map<String, Object> model, Date deliveryDate) {

        try {
            Context context = new Context();
            context.setVariables(model);
            String html = templateEngine.process(templateName, context);

            // 🔹 Enviar con Resend (HTTP)
            sendWithResend(user.getEmail(), subject, html);

        } catch (Exception e) {
            System.err.println("FALLO AL ENVIAR CORREO: " + e.getMessage());
        }
    }

    private void sendWithResend(String to, String subject, String html) {

        String url = "https://api.resend.com/emails";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "from", FROM,
                "to", to,
                "subject", subject,
                "html", html
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response =
                    restTemplate.postForEntity(url, request, String.class);

            System.out.println("Correo enviado, status: " + response.getStatusCode());

        } catch (Exception e) {
            System.err.println("ERROR enviando correo con Resend: " + e.getMessage());
        }
    }
}