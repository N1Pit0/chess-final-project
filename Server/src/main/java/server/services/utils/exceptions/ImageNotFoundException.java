package server.services.utils.exceptions;

public class ImageNotFoundException extends NullPointerException {
    public ImageNotFoundException(String message) {
        super(message);
    }
}
