package eBIS.AppConfig;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(METHOD)
@Documented
public @interface CustomWebExceptionHandler {
	Class<? extends Throwable>[] value() default {Exception.class,ArrayIndexOutOfBoundsException.class,UnsupportedEncodingException.class,ClassNotFoundException.class,
		FileNotFoundException.class,IOException.class,InterruptedException.class,NoSuchFieldException.class,NoSuchMethodException.class,NullPointerException.class,RuntimeException.class,StringIndexOutOfBoundsException.class,
		javax.net.ssl.SSLException.class,IllegalStateException.class,ClassCastException.class,ArithmeticException.class,IllegalArgumentException.class,NoSuchMethodException.class,StackOverflowError.class,NoClassDefFoundError.class,
		ExceptionInInitializerError.class,IllegalArgumentException.class,IllegalThreadStateException.class,AssertionError.class,javax.mail.MessagingException.class,javax.net.ssl.SSLHandshakeException.class,
		org.postgresql.util.PSQLException.class,javax.el.PropertyNotFoundException.class,org.springframework.web.HttpMediaTypeNotAcceptableException.class,org.springframework.web.HttpRequestMethodNotSupportedException.class,javax.servlet.ServletException.class,
		org.springframework.jdbc.BadSqlGrammarException.class
		
		};
}
