package be.kdg.musicmaker.band;


public class BandNotFoundException extends Exception{

    public BandNotFoundException() {
    }

    public BandNotFoundException(String message) {
        super(message);
    }
}
