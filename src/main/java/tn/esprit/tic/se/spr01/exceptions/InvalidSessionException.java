package tn.esprit.tic.se.spr01.exceptions;



public class InvalidSessionException extends RuntimeException {
    public InvalidSessionException(String message) {
        super(message);
    }
}
