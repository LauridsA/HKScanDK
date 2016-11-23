package model;

public class DailyMessages {
	private String message;
	private int startTimeStamp;
	private int endTimeStamp;
	
	public DailyMessages(String message, int startTimeStamp, int endTimeStamp){
		this.message = message;
		this.startTimeStamp = startTimeStamp;
		this.endTimeStamp = endTimeStamp;
	}

	@Override
	public String toString() {
		return "DailyMessages [message=" + message + ", startTimeStamp=" + startTimeStamp + ", endTimeStamp="
				+ endTimeStamp + "]";
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getStartTimeStamp() {
		return startTimeStamp;
	}

	public void setStartTimeStamp(int startTimeStamp) {
		this.startTimeStamp = startTimeStamp;
	}

	public int getEndTimeStamp() {
		return endTimeStamp;
	}

	public void setEndTimeStamp(int endTimeStamp) {
		this.endTimeStamp = endTimeStamp;
	}
}
