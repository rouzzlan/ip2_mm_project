package be.kdg.musicmaker.util;

public class InstrumentNotFoundException extends Exception {

    public InstrumentNotFoundException() {
    }

    public InstrumentNotFoundException(String message) {
        super(message);
    }
}