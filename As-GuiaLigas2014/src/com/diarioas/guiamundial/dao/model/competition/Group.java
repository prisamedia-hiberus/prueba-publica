package com.diarioas.guiamundial.dao.model.competition;

import java.io.Serializable;
import java.util.ArrayList;

import com.diarioas.guiamundial.dao.model.team.Team;

public class Group implements Serializable {

	private static final long serialVersionUID = 4L;

	private String name;
	private ArrayList<Team> teams;

	public Group() {
		teams = new ArrayList<Team>();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the teams
	 */
	public ArrayList<Team> getTeams() {
		return teams;
	}

	/**
	 * @param teams
	 *            the teams to set
	 */
	public void setTeams(ArrayList<Team> teams) {
		this.teams = teams;
	}

	/**
	 * @param team
	 */
	public void addTeam(Team team) {
		this.teams.add(team);
	}
}
