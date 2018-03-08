package be.kdg.musicmaker.Instrument;

public class InstrumentNotFoundException extends Exception {

    public InstrumentNotFoundException() {
    }

    public InstrumentNotFoundException(String message) {
        super(message);
    }
}