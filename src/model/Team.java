package model;

public class Team {
	private int teamId = 0;
	private long startTime = 0;
	private long endTime = 0;
	private String teamName = "";
	private int teamSize = 0;
	private int department = 0;
	
	
	public Team(int teamId, long startTime, long endTime, String teamName, int teamSize, int department) {
		this.teamId = teamId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.teamName = teamName;
		this.teamSize = teamSize;
		this.department = department;
	}

	/**
	 * @return the teamName
	 */
	public String getTeamName() {
		return this.teamName;
	}

	/**
	 * @param teamName the teamName to set
	 */
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	/**
	 * @return the teamSize
	 */
	public int getTeamSize() {
		return this.teamSize;
	}

	/**
	 * @param teamSize the teamSize to set
	 */
	public void setTeamSize(int teamSize) {
		this.teamSize = teamSize;
	}

	/**
	 * @return the department
	 */
	public int getDepartment() {
		return this.department;
	}

	/**
	 * @param department the department to set
	 */
	public void setDepartment(int department) {
		this.department = department;
	}

	/**
	 * @return the teamId
	 */
	public int getTeamId() {
		return this.teamId;
	}

	/**
	 * @param teamId the teamId to set
	 */
	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	/**
	 * @return the startTime
	 */
	public long getStartTime() {
		return this.startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public long getEndTime() {
		return this.endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

}
