/**
 * 
 */
package com.diarioas.guialigas.dao.model.team;

import android.os.Parcel;
import android.util.Log;

/**
 * @author robertosanchez
 * 
 */
public class Star extends Staff {

	private String position;
	private String numInternational;
	private String age;
	private String stature;
	private String weight;
	private String clubName;
	private String clubShield;
	private String url;
	private String playerId;

	public Star(Parcel in) {
		super(in);
	}

	public Star() {
		super();
	}

	/**
	 * @return the position
	 */
	public String getPosition() {
		return position;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(String position) {
		this.position = position;
	}

	/**
	 * @return the numInternational
	 */
	public String getNumInternational() {
		return numInternational;
	}

	/**
	 * @param numInternational
	 *            the numInternational to set
	 */
	public void setNumInternational(String numInternational) {
		this.numInternational = numInternational;
	}

	/**
	 * @return the age
	 */
	public String getAge() {
		return age;
	}

	/**
	 * @param age
	 *            the age to set
	 */
	public void setAge(String age) {
		this.age = age;
	}

	/**
	 * @return the stature
	 */
	public String getStature() {
		return stature;
	}

	/**
	 * @param stature
	 *            the stature to set
	 */
	public void setStature(String stature) {
		this.stature = stature;
	}

	/**
	 * @return the weight
	 */
	public String getWeight() {
		return weight;
	}

	/**
	 * @param weight
	 *            the weight to set
	 */
	public void setWeight(String weight) {
		this.weight = weight;
	}

	/**
	 * @return the clubName
	 */
	public String getClubName() {
		return clubName;
	}

	/**
	 * @param clubName
	 *            the clubName to set
	 */
	public void setClubName(String clubName) {
		this.clubName = clubName;
	}

	/**
	 * @return the clubShield
	 */
	public String getClubShield() {
		return clubShield;
	}

	/**
	 * @param clubShield
	 *            the clubShield to set
	 */
	public void setClubShield(String clubShield) {
		this.clubShield = clubShield;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the playerId
	 */
	public String getPlayerId() {
		return playerId;
	}

	/**
	 * @param playerId
	 *            the playerId to set
	 */
	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	@Override
	protected void readFromParcel(Parcel in) {
		super.readFromParcel(in);

		if (in.readByte() == 1)
			this.position = in.readString();
		if (in.readByte() == 1)
			this.numInternational = in.readString();
		if (in.readByte() == 1)
			this.age = in.readString();
		if (in.readByte() == 1)
			this.stature = in.readString();
		if (in.readByte() == 1)
			this.weight = in.readString();
		if (in.readByte() == 1)
			this.clubName = in.readString();
		if (in.readByte() == 1)
			this.clubShield = in.readString();
		if (in.readByte() == 1)
			this.url = in.readString();
		if (in.readByte() == 1)
			this.playerId = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		try {
			super.writeToParcel(dest, flags);

			if (position != null) {
				dest.writeByte((byte) 1);
				dest.writeString(position);
			} else {
				dest.writeByte((byte) 0);
			}
			if (numInternational != null) {
				dest.writeByte((byte) 1);
				dest.writeString(numInternational);
			} else {
				dest.writeByte((byte) 0);
			}
			if (age != null) {
				dest.writeByte((byte) 1);
				dest.writeString(age);
			} else {
				dest.writeByte((byte) 0);
			}
			if (stature != null) {
				dest.writeByte((byte) 1);
				dest.writeString(stature);
			} else {
				dest.writeByte((byte) 0);
			}
			if (weight != null) {
				dest.writeByte((byte) 1);
				dest.writeString(weight);
			} else {
				dest.writeByte((byte) 0);
			}
			if (clubName != null) {
				dest.writeByte((byte) 1);
				dest.writeString(clubName);
			} else {
				dest.writeByte((byte) 0);
			}
			if (clubShield != null) {
				dest.writeByte((byte) 1);
				dest.writeString(clubShield);
			} else {
				dest.writeByte((byte) 0);
			}
			if (url != null) {
				dest.writeByte((byte) 1);
				dest.writeString(url);
			} else {
				dest.writeByte((byte) 0);
			}
			if (playerId != null) {
				dest.writeByte((byte) 1);
				dest.writeString(playerId);
			} else {
				dest.writeByte((byte) 0);
			}
		} catch (Exception e) {
			Log.e("ParcelableError",
					"Se ha producido un error" + e.getMessage());
		}

	}
}
