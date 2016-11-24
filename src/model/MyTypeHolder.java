package model;

/**
 * MyTypeHolder is used for returning values to the UI.
 * When using one of the setting methods or constructors the class will also set what type it is.
 * 
 *
 */
public class MyTypeHolder {
	private Integer integer;
	private String string;
	private boolean bool;
	private FieldType type;
	
	
	/**
	 * Enum for determening what type the MyTypeHolder is.
	 *
	 */
	public enum FieldType {
		INTEGER, STRING, BOOL
	}
	

	
	/**
	 * Constructor for setting integer
	 * @param integer
	 */
	public MyTypeHolder(int integer) {
		setInteger(integer);
	}
	
	/**
	 * Constuctor for setting String
	 * @param string
	 */
	public MyTypeHolder(String string) {
		setString(string);
	}
	
	/**
	 * Constructor for setting booleans
	 * @param bool
	 */
	public MyTypeHolder(boolean bool) {
		setBool(bool);
	}
	
	/**
	 * Setter for integer
	 * Will also set the type of MyTypeHolder with an enum
	 * @param integer
	 */
	public void setInteger(int integer) {
		this.integer = integer;
		setType(FieldType.INTEGER);
	}
	
	/**
	 * Setter for String
	 * Will also set the type of MyTypeHolder with an enum
	 * @param string
	 */
	public void setString(String string) {
		this.string = string;
		setType(FieldType.STRING);
	}
	
	
	/**
	 * Setter for boolean
	 * Will also set the type of MyTypeHolder with an enum
	 * @param bool
	 */
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
	
	
	
	/**
	 * Sets the type of MyTypeHolder
	 * @param type
	 */
	private void setType(FieldType type) {
		this.type = type;

	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
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
