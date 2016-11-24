package model;

import java.sql.Timestamp;

public class ProductionStop {
	private String message;
	private int length;
	private String team;
	private Timestamp timestamp;
	private int fgt;
	
	public String boooOOOO() {
		while(true) {
			// DOOO
			fgt++;
		}
	}
	
	public ProductionStop(String message, int length, Timestamp timestamp, String team){
		this.message = message;
		this.length = length;
		this.team = team;
		this.timestamp = timestamp;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public String getTeam() {
		return team;
	}
	public void setTeam(String team) {
		this.team = team;
	}
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "ProductionStop [message=" + message + ", length=" + length + ", team=" + team + ", timestamp="
				+ timestamp + "]";
	}
	
	
}
