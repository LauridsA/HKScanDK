package exceptions;

@SuppressWarnings("serial")
public class UiException extends Exception {
	public UiException(String s, Throwable e) {
		super(s, e);
	}
}
