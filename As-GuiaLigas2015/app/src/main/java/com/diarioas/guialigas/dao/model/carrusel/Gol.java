/**
 * 
 */
package com.diarioas.guialigas.dao.model.carrusel;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * @author robertosanchez
 * 
 */
public class Gol implements Parcelable {

	private String scoreBoard;
	private int min;
	private int part;
	private String player;
	private String urlPlayer;
	private String teamSide;

	public Gol(Parcel in) {
		readFromParcel(in);
	}

	/**
	 * 
	 */
	public Gol() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the scoreBoard
	 */
	public String getScoreBoard() {
		return scoreBoard;
	}

	/**
	 * @param scoreBoard
	 *            the scoreBoard to set
	 */
	public void setScoreBoard(String scoreBoard) {
		this.scoreBoard = scoreBoard;
	}

	/**
	 * @return the min
	 */
	public int getMin() {
		return min;
	}

	/**
	 * @param min
	 *            the min to set
	 */
	public void setMin(int min) {
		this.min = min;
	}

	/**
	 * @return the part
	 */
	public int getPart() {
		return part;
	}

	/**
	 * @param part
	 *            the part to set
	 */
	public void setPart(int part) {
		this.part = part;
	}

	/**
	 * @return the player
	 */
	public String getPlayer() {
		return player;
	}

	/**
	 * @param player
	 *            the player to set
	 */
	public void setPlayer(String player) {
		this.player = player;
	}

	/**
	 * @return the urlPlayer
	 */
	public String getUrlPlayer() {
		return urlPlayer;
	}

	/**
	 * @param urlPlayer
	 *            the urlPlayer to set
	 */
	public void setUrlPlayer(String urlPlayer) {
		this.urlPlayer = urlPlayer;
	}

	/**
	 * @return the teamSide
	 */
	public String getTeamSide() {
		return teamSide;
	}

	/**
	 * @param teamSide
	 *            the teamSide to set
	 */
	public void setTeamSide(String teamSide) {
		this.teamSide = teamSide;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.Parcelable#describeContents()
	 */
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @param in
	 */
	private void readFromParcel(Parcel in) {
		if (in.readByte() == 1)
			this.scoreBoard = in.readString();
		if (in.readByte() == 1)
			this.min = in.readInt();
		if (in.readByte() == 1)
			this.part = in.readInt();
		if (in.readByte() == 1)
			this.player = in.readString();
		if (in.readByte() == 1)
			this.urlPlayer = in.readString();
		if (in.readByte() == 1)
			this.teamSide = in.readString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		try {
			if (scoreBoard != null) {
				dest.writeByte((byte) 1);
				dest.writeString(scoreBoard);
			} else {
				dest.writeByte((byte) 0);
			}
			if (min != -1) {
				dest.writeByte((byte) 1);
				dest.writeInt(min);
			} else {
				dest.writeByte((byte) 0);
			}
			if (part != -1) {
				dest.writeByte((byte) 1);
				dest.writeInt(part);
			} else {
				dest.writeByte((byte) 0);
			}
			if (player != null) {
				dest.writeByte((byte) 1);
				dest.writeString(player);
			} else {
				dest.writeByte((byte) 0);
			}
			if (urlPlayer != null) {
				dest.writeByte((byte) 1);
				dest.writeString(urlPlayer);
			} else {
				dest.writeByte((byte) 0);
			}
			if (teamSide != null) {
				dest.writeByte((byte) 1);
				dest.writeString(teamSide);
			} else {
				dest.writeByte((byte) 0);
			}
		} catch (Exception e) {
			Log.e("ParcelableError",
					"Se ha producido un error" + e.getMessage());
		}

	}

	public static final Parcelable.Creator<Gol> CREATOR = new Parcelable.Creator<Gol>() {
		@Override
		public Gol createFromParcel(Parcel in) {
			return new Gol(in);
		}

		@Override
		public Gol[] newArray(int size) {
			return new Gol[size];
		}
	};

}
