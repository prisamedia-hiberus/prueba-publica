package com.diarioas.guialigas.dao.model.general;

import java.util.ArrayList;

import com.diarioas.guialigas.dao.model.competition.Group;

public class TeamSection extends Section {

	private static final long serialVersionUID = 2L;

	private ArrayList<Group> groups;
	private String typeOrder;

	public TeamSection() {
		groups = new ArrayList<Group>();
	}

	/**
	 * @return the groups
	 */
	public ArrayList<Group> getGroups() {
		return groups;
	}

	/**
	 * @param groups
	 *            the groups to set
	 */
	public void setGroups(ArrayList<Group> groups) {
		this.groups = groups;
	}

	public void addGroup(Group group) {
		groups.add(group);
	}

	/**
	 * @return the typeOrder
	 */
	public String getTypeOrder() {
		return typeOrder;
	}

	/**
	 * @param typeOrder
	 *            the typeOrder to set
	 */
	public void setTypeOrder(String typeOrder) {
		this.typeOrder = typeOrder;
	}

}
