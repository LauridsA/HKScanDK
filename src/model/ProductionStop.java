package model;

public class ProductionStop {
	private int id;
	private Long stopTime;
	private int stopLength;
	private String stopDescription;
	private int teamTimeTableId;
	
	public ProductionStop(int id, Long stopTime, int stopLength, String stopDescription, int teamTimeTableId){
		this.stopTime = stopTime;
		this.stopLength = stopLength;
		this.stopDescription = stopDescription;
		this.teamTimeTableId = teamTimeTableId;
		this.id = id;
	}

	public Long getStopTime() {
		return stopTime;
	}

	public void setStopTime(Long stopTime) {
		this.stopTime = stopTime;
	}

	public int getStopLength() {
		return stopLength;
	}

	public void setStopLength(int stopLength) {
		this.stopLength = stopLength;
	}

	public String getStopDescription() {
		return stopDescription;
	}

	public void setStopDescription(String stopDescription) {
		this.stopDescription = stopDescription;
	}

	public int getTeamTimeTableId() {
		return teamTimeTableId;
	}

	public void setTeamTimeTableId(int teamTimeTableId) {
		this.teamTimeTableId = teamTimeTableId;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return this.id;
	}

	@Override
	public String toString() {
		return "ProductionStop [stopTime=" + stopTime + ", stopLength=" + stopLength + ", stopDescription="
				+ stopDescription + ", teamTimeTableId=" + teamTimeTableId + "]";
	}
	
	
}
