package be.kdg.musicmaker.instrument;

public class InstrumentNotFoundException extends Exception {

    public InstrumentNotFoundException() {
    }

    public InstrumentNotFoundException(String message) {
        super(message);
    }
}