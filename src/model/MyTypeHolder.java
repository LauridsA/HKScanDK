package model;

public class MyTypeHolder {
	private Integer integer;
	private String string;
	private boolean bool;
	private FieldType type;
	
	
	public enum FieldType {
		INTEGER, STRING, BOOL
	}
	

	
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
		setType(FieldType.INTEGER);
	}
	
	public void setString(String string) {
		this.string = string;
		setType(FieldType.STRING);
	}
	
	public void setBool(boolean bool) {
		this.bool = bool;
		setType(FieldType.BOOL);
	}
	
	public Integer getInteger() {
		return integer;
	}
	
	public String getString() {
		return string;
	}
	
	public Boolean getBool() {
		return bool;
	}
	
	private void setType(FieldType type) {
		this.type = type;

	}
	
	@Override
	public String toString() {
		String string;
		switch (type) {
		case STRING:
			string = getString();
			break;
		case INTEGER:
			string = getInteger().toString();
			break;
		case BOOL:
			string = getBool().toString();
			break;
		default:
			string = "Fejl";
			break;
		}
		return string;
	}

}
