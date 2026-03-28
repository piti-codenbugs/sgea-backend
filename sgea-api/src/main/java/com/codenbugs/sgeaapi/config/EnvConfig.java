package com.codenbugs.sgeaapi.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvConfig {
    static {
        String currentDir = System.getProperty("user.dir");
        System.out.println("Directorio actual: " + currentDir);

        try {
            Dotenv dotenv = Dotenv.configure()
                    .directory("./")
                    .filename(".env")
                    .ignoreIfMissing()
                    .load();

            String key = dotenv.get("BREVO_SMTP_KEY");
            if (key != null && !key.isEmpty()) {
                System.setProperty("BREVO_SMTP_KEY", key);
                System.out.println("CLAVE SMTP CARGADA: " + key.substring(0, 20) + "...");
            } else {
                System.out.println("ERROR: BREVO_SMTP_KEY no encontrada en .env");
            }

        } catch (Exception e) {
            System.out.println("ERROR AL CARGAR .env: " + e.getMessage());
            e.printStackTrace();
        }
    }
}