/**
 * 
 */
package com.diarioas.guialigas.dao.model.team;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.diarioas.guialigas.dao.model.Titulo;

/**
 * @author robertosanchez
 * 
 */
public class TituloTeam extends Titulo {

	private ArrayList<String> years;

	public TituloTeam(Parcel in) {
		super(in);
		readFromParcel(in);
		years = new ArrayList<String>();
	}

	/**
	 * @param name
	 */
	public TituloTeam(String name) {
		super(name);
		this.years = new ArrayList<String>();
	}

	/**
	 * @param name2
	 * @param num2
	 */
	public TituloTeam(String name, ArrayList<String> year) {
		super(name);
		this.years = year;
	}

	/**
	 * @return the year
	 */
	public ArrayList<String> getYear() {
		return years;
	}

	/**
	 * @param num
	 *            the year to set
	 */
	public void setYear(ArrayList<String> years) {
		this.years = years;
	}

	public void addYear(String year) {
		this.years.add(year);
	}

	/**
	 * @return
	 */
	public int getNumTitle() {
		// TODO Auto-generated method stub
		return this.years.size();
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
			this.name = in.readString();

		Bundle bundle;
		while ((bundle = in.readBundle()) != null) {
			years.add(bundle.getString("TiT" + name));
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		try {
			if (name != null) {
				dest.writeByte((byte) 1);
				dest.writeString(name);
			} else {
				dest.writeByte((byte) 0);
			}
			Bundle bundle = new Bundle(years.size());
			for (String year : years) {
				bundle.putString("TiT" + name, year);
			}

			dest.writeBundle(bundle);
		} catch (Exception e) {
			Log.e("ParcelableError",
					"Se ha producido un error" + e.getMessage());
		}

	}

	public static final Parcelable.Creator<TituloTeam> CREATOR = new Parcelable.Creator<TituloTeam>() {
		@Override
		public TituloTeam createFromParcel(Parcel in) {
			return new TituloTeam(in);
		}

		@Override
		public TituloTeam[] newArray(int size) {
			return new TituloTeam[size];
		}
	};

}
