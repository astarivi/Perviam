package ovh.astarivi.perviam.gis.remote.exceptions;


public class BadRequestException extends Exception{
    public BadRequestException(String cause) {
        super(cause);
    }

    public BadRequestException(Exception e) {
        super(e);
    }
}
