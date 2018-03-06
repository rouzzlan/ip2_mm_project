package be.kdg.musicmaker.util;


public class BandNotFoundException extends Exception{

    public BandNotFoundException() {
    }

    public BandNotFoundException(String message) {
        super(message);
    }
}
