package exceptions;


public class DbaException extends Exception {
	
	
	public DbaException(String s, Throwable e) {
		super(s, e);
	}
	
	public DbaException(String s) {
		super(s);
	}
	

}
