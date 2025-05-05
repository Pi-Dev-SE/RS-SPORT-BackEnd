package tn.esprit.tic.se.spr01.TeamandPlayer.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}