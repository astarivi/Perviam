package ovh.astarivi.perviam.gis.remote.exceptions;


public class PrematureStopException extends Exception {
    public PrematureStopException(Exception e) {
        super(e);
    }

    public PrematureStopException(String cause) {
        super(cause);
    }
}
