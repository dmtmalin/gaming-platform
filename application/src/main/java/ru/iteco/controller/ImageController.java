package ru.iteco.controller;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import ru.iteco.StorageException;
import ru.iteco.WebDavStorageService;
import ru.iteco.error.BadRequestException;
import ru.iteco.property.ImageThumbnailProperties;

@Controller
public class ImageController {
    private final WebDavStorageService webDavStorageService;
    private final ImageThumbnailProperties thumbnailProperties;

    public ImageController(WebDavStorageService webDavStorageService,
                           ImageThumbnailProperties imageThumbnailProperties) {
        this.webDavStorageService = webDavStorageService;
        this.thumbnailProperties = imageThumbnailProperties;
    }

    @PostMapping(value = "/preview/image")
    @ResponseBody
    public String previewIcon(@RequestParam(value = "image") MultipartFile image) {
        try {
            byte[] preview = webDavStorageService.resize(image,
                    thumbnailProperties.getWidth(),
                    thumbnailProperties.getHeight());
            return Base64.encodeBase64String(preview);
        }
        catch (StorageException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
