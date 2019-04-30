package com.kangyonggan.blog.exception;

/**
 * 博客通用异常
 *
 * @author kangyonggan
 * @since 8/9/18
 */
public class BlogException extends RuntimeException {

    public BlogException() {
    }

    public BlogException(String message) {
        super(message);
    }

    public BlogException(String message, Throwable cause) {
        super(message, cause);
    }

    public BlogException(Throwable cause) {
        super(cause);
    }

    public BlogException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
