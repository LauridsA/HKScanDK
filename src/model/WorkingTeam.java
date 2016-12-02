package model;

public class WorkingTeam {

	private static WorkingTeam instance;
	private static int teamId = 0;
	private static int teamtimetableid = 0;
	

	private WorkingTeam() {
		// empty constructor. TeamID to be retrieved later, maybe?
	}
	
	public static WorkingTeam getInstance() {
		if(instance == null) {
			instance = new WorkingTeam(); // TODO how do we retrieve current working team from the DB?
		}
		return instance;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		WorkingTeam.teamId = teamId;
	}

	public static int getTeamtimetableid() {
		return teamtimetableid;
	}

	public static void setTeamtimetableid(int teamtimetableid) {
		WorkingTeam.teamtimetableid = teamtimetableid;
	}
	
}
