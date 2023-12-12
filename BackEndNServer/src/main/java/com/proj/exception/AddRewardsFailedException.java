package com.proj.exception;

public class AddRewardsFailedException extends RuntimeException{
    public AddRewardsFailedException(){}

    public AddRewardsFailedException(String msg) {
        super(msg);
    }

    public AddRewardsFailedException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public AddRewardsFailedException(Throwable cause) {
        super(cause);
    }
}
