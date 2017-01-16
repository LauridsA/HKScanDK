package exceptions;

@SuppressWarnings("serial")
public class PassThroughException extends Exception{
	public PassThroughException(String s, Throwable e) {
		super(s, e);
	}

	public PassThroughException(String s) {
		super(s);
	}
}
