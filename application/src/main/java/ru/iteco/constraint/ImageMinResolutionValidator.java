package ru.iteco.constraint;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class ImageMinResolutionValidator implements ConstraintValidator<ImageMinResolution, MultipartFile> {

    private int width;
    private int height;

    @Override
    public void initialize(ImageMinResolution image) {
        this.width = image.width();
        this.height = image.height();
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {
        if (multipartFile == null || multipartFile.isEmpty())
            return true;
        try {
            InputStream stream = multipartFile.getInputStream();
            BufferedImage image = ImageIO.read(stream);
            return image == null || image.getWidth() >= width && image.getHeight() >= height;
        }
        catch (IOException e) {
            return true;
        }

    }
}
