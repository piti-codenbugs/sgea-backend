package com.codenbugs.sgeaapi.service.storage;

import com.cloudinary.Cloudinary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public Map<String, String> uploadFile(MultipartFile file) throws IOException {

        String publicId = UUID.randomUUID().toString();

        Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                Map.of(
                        "resource_type", "image",
                        "folder", "mi_app/dev",
                        "type", "upload",
                        "public_id", publicId,
                        "access_mode", "public"
                )
        );

        String url = (String) uploadResult.get("secure_url");
        String storedPublicId = (String) uploadResult.get("public_id");

        return Map.of(
                "url", url,
                "public_id", storedPublicId
        );
    }
}
