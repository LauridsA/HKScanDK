package model;

public class DailyMessages {
	private String message;
	private Long timestamp;
	private Long expire;
	private Long showDate;
	
	public DailyMessages(String message, Long timestamp, Long expire, Long showDate){
		this.message = message;
		this.timestamp = timestamp;
		this.expire = expire;
		this.showDate = showDate;
	}

	@Override
	public String toString() {
		return "DailyMessages [message=" + message + ", timestamp=" + timestamp + ", expire="
				+ expire + "showDate=" + showDate + "]";
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getTimeStamp() {
		return timestamp;
	}

	public void setTimeStamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public Long getExpire() {
		return expire;
	}

	public void setExpire(Long expire) {
		this.expire = expire;
	}

	public Long getShowDate() {
		return showDate;
	}

	public void setShowDate(Long showDate) {
		this.showDate = showDate;
	}
}
