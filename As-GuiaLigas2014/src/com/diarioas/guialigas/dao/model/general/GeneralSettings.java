package com.diarioas.guialigas.dao.model.general;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.diarioas.guialigas.dao.model.competition.Competition;
import com.diarioas.guialigas.dao.reader.DatabaseDAO;

public class GeneralSettings {

	private HashMap<?, ?> splash;
	private HashMap<?, ?> header;
	private HashMap<String, String> prefix;
	private HashMap<String, String> cookies;
	private HashMap<String, String> status;
	private HashMap<String, String> gamePlay;
	private HashMap<String, String> spades;
	private HashMap<String, String> clasificationLabels;
	private int currentCompetition;
	private ArrayList<Competition> competitions;

	public GeneralSettings() {
		currentCompetition = 0;
		splash = new HashMap<Object, Object>();
		prefix = new HashMap<String, String>();
		status = new HashMap<String, String>();
		gamePlay = new HashMap<String, String>();
		spades = new HashMap<String, String>();
		clasificationLabels = new HashMap<String, String>();
		competitions = new ArrayList<Competition>();
	}

	/**
	 * @return the splash
	 */
	public HashMap<?, ?> getSplash() {
		return splash;
	}

	/**
	 * @param splash
	 *            the splash to set
	 */
	public void setSplash(HashMap<?, ?> splash) {
		this.splash = splash;
	}

	public HashMap<?, ?> getHeader() {
		return header;
	}

	public void setHeader(HashMap<?, ?> header) {
		this.header = header;
	}

	/**
	 * @return the prefix
	 */
	public HashMap<String, String> getPrefix() {
		return prefix;
	}

	/**
	 * @param prefix
	 *            the prefix to set
	 */
	public void setPrefix(HashMap<String, String> prefix) {
		this.prefix = prefix;
	}
	

	public HashMap<String, String> getCookies() {
		return cookies;
	}

	public void setCookies(HashMap<String, String> cookies) {
		this.cookies = cookies;
	}

	/**
	 * @return the status
	 */
	public HashMap<String, String> getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(HashMap<String, String> status) {
		this.status = status;
	}

	/**
	 * @return the gamePlay
	 */
	public HashMap<String, String> getGamePlay() {
		return gamePlay;
	}

	/**
	 * @param gamePlay
	 *            the gamePlay to set
	 */
	public void setGamePlay(HashMap<String, String> gamePlay) {
		this.gamePlay = gamePlay;
	}

	/**
	 * @return the spades
	 */
	public HashMap<String, String> getSpades() {
		return spades;
	}

	/**
	 * @param spades
	 *            the spades to set
	 */
	public void setSpades(HashMap<String, String> spades) {
		this.spades = spades;
	}

	/**
	 * @return the clasificationLabels
	 */
	public HashMap<String, String> getClasificationLabels() {
		return clasificationLabels;
	}

	/**
	 * @param clasificationLabels
	 *            the clasificationLabels to set
	 */
	public void setClasificationLabels(
			HashMap<String, String> clasificationLabels) {
		this.clasificationLabels = clasificationLabels;
	}

	/**
	 * @return the competitions
	 */
	public ArrayList<Competition> getCompetitions() {
		return competitions;
	}

	/**
	 * @param competitions
	 *            the competitions to set
	 */
	public void setCompetitions(ArrayList<Competition> competitions) {
		this.competitions = competitions;
	}

	public Competition getCompetition(int competitionId) {
		for (Competition competition : competitions) {
			if (competitionId == competition.getId())
				return competition;
		}
		return null;
	}

	/**
	 * @return the currentCompetition
	 */
	public Competition getCurrentCompetition() {
		return competitions.get(currentCompetition);
	}

	/**
	 * @param currentCompetition the currentCompetition to set
	 */
	public void setCurrentCompetition(int index) {
		this.currentCompetition = index;
	}

}
