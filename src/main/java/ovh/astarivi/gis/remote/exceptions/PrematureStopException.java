package ovh.astarivi.gis.remote.exceptions;


public class PrematureStopException extends Exception {
    public PrematureStopException(Exception e) {
        super(e);
    }
}
