package com.proj.exception;
  /**
     * Models exception when user accesses operations that is not permitted by their current access level.
     */
public class IllegalUserOperationException extends RuntimeException{
    public IllegalUserOperationException(){}

    public IllegalUserOperationException(String msg) {
        super(msg);
    }

    public IllegalUserOperationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public IllegalUserOperationException(Throwable cause) {
        super(cause);
    }


}