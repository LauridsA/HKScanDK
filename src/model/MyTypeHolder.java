package model;

public class MyTypeHolder {
	Integer integer;
	String string;
	boolean bool;
	

	
	public MyTypeHolder(int integer) {
		setInteger(integer);
	}
	
	public MyTypeHolder(String string) {
		setString(string);
	}
	
	public MyTypeHolder(boolean bool) {
		setBool(bool);
	}
	
	public void setInteger(int integer) {
		this.integer = integer;
	}
	
	public void setString(String string) {
		this.string = string;
	}
	
	public void setBool(boolean bool) {
		this.bool = bool;
	}
	
	public Integer getInteger() {
		return integer;
	}
	
	public String getString() {
		return string;
	}
	
	public boolean getBool() {
		return bool;
	}

}
