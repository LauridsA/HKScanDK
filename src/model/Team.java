package model;

public class Team {
	private int teamId = 0;
	private int teamTimeTableId = 0;
	private long startTime = 0;
	private long endTime = 0;
	
	public Team(int teamId, int teamTimeTableId, long startTime, long endTime) {
		this.teamId = teamId;
		this.teamTimeTableId = teamTimeTableId;
		this.startTime = startTime;
		this.endTime = endTime;
	}

}
