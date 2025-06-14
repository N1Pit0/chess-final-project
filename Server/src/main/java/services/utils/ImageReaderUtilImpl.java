package services.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class ImageReaderUtilImpl implements ImageReaderUtil {

    public <T extends Image> Optional<T> readImage(String fileName) throws ClassCastException {
        BufferedImage image = null;
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResource(fileName)));
        } catch (IOException | NullPointerException e) {
            System.out.println("File not found: " + e.getMessage());
        }

        return Optional.ofNullable((T) image);
    }
}
