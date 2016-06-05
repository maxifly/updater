package com.kuku;

/**
 * Created by Maximus on 05.06.2016.
 */
public class ErrorParameters extends Exception {
    public ErrorParameters (String message) {
        super(message);
    }
    public ErrorParameters() {
        super();
    }
}
