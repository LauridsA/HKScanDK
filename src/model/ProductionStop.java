package model;

public class ProductionStop {
	private int id;
	private Long stopTime;
	private int stopLength;
	private String stopDescription;
	private int teamTimeTableId;
	private Team team;
	
	public ProductionStop(int id, Long stopTime, int stopLength, String stopDescription, Team team){
		this.stopTime = stopTime;
		this.stopLength = stopLength;
		this.stopDescription = stopDescription;
		this.id = id;
		this.team = team;
	}

	/**
	 * @return the team
	 */
	public Team getTeam() {
		return this.team;
	}

	/**
	 * @param team the team to set
	 */
	public void setTeam(Team team) {
		this.team = team;
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
