/**
 * 
 */
package com.diarioas.guialigas.dao.model.calendar;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * @author robertosanchez
 * 
 */
public class Day implements Parcelable {

	private int numDay;
	private String date;
	private boolean defecto;
	private ArrayList<Match> matches;

	public Day(Parcel in) {
		matches = new ArrayList<Match>();
		readFromParcel(in);
	}

	public Day() {
		matches = new ArrayList<Match>();
	}

	/**
	 * @return the numDay
	 */
	public int getNumDay() {
		return numDay;
	}

	/**
	 * @param numDay
	 *            the numDay to set
	 */
	public void setNumDay(int numDay) {
		this.numDay = numDay;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date2
	 *            the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the defecto
	 */
	public boolean isDefecto() {
		return defecto;
	}

	/**
	 * @param defecto
	 *            the active to set
	 */
	public void setDefecto(boolean defecto) {
		this.defecto = defecto;
	}

	/**
	 * @return the matches
	 */
	public ArrayList<Match> getMatches() {
		return matches;
	}

	/**
	 * @param matches
	 *            the matches to set
	 */
	public void setMatches(ArrayList<Match> matches) {
		this.matches = matches;
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
			this.numDay = in.readInt();

		if (in.readByte() == 1)
			this.date = in.readString();
		if (in.readByte() == 1)
			this.defecto = (in.readInt() == 1);

		if (in.readByte() == 1)
			in.readList(matches, Match.class.getClassLoader());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		try {
			if (numDay != -1) {
				dest.writeByte((byte) 1);
				dest.writeInt(numDay);
			} else {
				dest.writeByte((byte) 0);
			}

			if (date != null) {
				dest.writeByte((byte) 1);
				dest.writeString(date);
			} else {
				dest.writeByte((byte) 0);
			}
			dest.writeByte((byte) 1);
			if (defecto)
				dest.writeInt(1);
			else
				dest.writeInt(0);
			if (matches.size() != 0) {
				dest.writeByte((byte) 1);
				dest.writeList(matches);
			} else {
				dest.writeByte((byte) -1);
			}
		} catch (Exception e) {
			Log.e("ParcelableError",
					"Se ha producido un error" + e.getMessage());
		}

	}

	public static final Parcelable.Creator<Day> CREATOR = new Parcelable.Creator<Day>() {
		@Override
		public Day createFromParcel(Parcel in) {
			return new Day(in);
		}

		@Override
		public Day[] newArray(int size) {
			return new Day[size];
		}
	};

}
