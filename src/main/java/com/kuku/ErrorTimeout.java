package com.kuku;

/**
 * Created by Maximus on 05.06.2016.
 */
public class ErrorTimeout extends Exception {
    public ErrorTimeout(String message) {
        super(message);
    }
    public ErrorTimeout() {
        super();
    }
}
