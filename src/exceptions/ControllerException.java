package exceptions;

@SuppressWarnings("serial")
public class ControllerException extends Exception {

	public ControllerException(String s, Throwable e) {
		super(s, e);
	}
}
