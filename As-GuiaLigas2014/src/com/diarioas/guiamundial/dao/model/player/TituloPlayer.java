/**
 * 
 */
package com.diarioas.guiamundial.dao.model.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.diarioas.guiamundial.dao.model.Titulo;

/**
 * @author robertosanchez
 * 
 */
public class TituloPlayer extends Titulo {

	private int numTitle = -1;
	private HashMap<String, ArrayList<String>> years;// Equipo - Array A–os

	public TituloPlayer(Parcel in) {
		super(in);
		readFromParcel(in);
		this.years = new HashMap<String, ArrayList<String>>();
	}

	/**
	 * @param title
	 */
	public TituloPlayer(String competition) {
		super(competition);
		this.years = new HashMap<String, ArrayList<String>>();
		// TODO: Si se crea asi, falta por poner el numTitle
	}

	/**
	 * @param name2
	 * @param num2
	 */
	public TituloPlayer(String competition, int numTitle) {
		super(competition);
		this.numTitle = numTitle;
		this.years = new HashMap<String, ArrayList<String>>();
	}

	/**
	 * @param numTitle2
	 * @param name2
	 * @param num2
	 */
	public TituloPlayer(String competition, int numTitle,
			HashMap<String, ArrayList<String>> year) {
		super(competition);
		this.numTitle = numTitle;
		this.years = new HashMap<String, ArrayList<String>>();
	}

	/**
	 * @return the numTitle
	 */
	public int getNumTitle() {
		return numTitle;
	}

	/**
	 * @param numTitle
	 *            the numTitle to set
	 */
	public void setNumTitle(int numTitle) {
		this.numTitle = numTitle;
	}

	/**
	 * @return the years
	 */
	public HashMap<String, ArrayList<String>> getYears() {
		return years;
	}

	/**
	 * @param team
	 * @return
	 * @return
	 */
	public ArrayList<String> getYears(String team) {
		return this.years.get(team);
	}

	/**
	 * @param years
	 *            the years to set
	 */
	public void setYears(HashMap<String, ArrayList<String>> years) {
		this.years = years;
	}

	public void addYear(String team, ArrayList<String> years) {
		this.years.put(team, years);
	}

	public void addYear(String team, String year) {
		if (!this.years.containsKey(team))
			this.years.put(team, new ArrayList<String>());
		this.years.get(team).add(year);
	}

	/**
	 * 
	 */
	public void calculateNumTitles() {
		numTitle = 0;
		String team;
		for (Iterator<String> iterator = this.years.keySet().iterator(); iterator
				.hasNext();) {
			team = iterator.next();
			numTitle += years.get(team).size();

		}

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
		try {
			if (in.readByte() == 1)
				this.name = in.readString();
			if (in.readByte() == 1)
				this.numTitle = in.readInt();

			years = new HashMap<String, ArrayList<String>>();

			Bundle bundle = in.readBundle();
			String team;
			while ((bundle = in.readBundle()) != null) {
				for (Iterator<String> iterator = bundle.keySet().iterator(); iterator
						.hasNext();) {
					team = iterator.next();
					years.put(team,
							bundle.getBundle(team).getStringArrayList(team));
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("ParcelableError", "Ha fallado en el readParcelable");
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
			if (numTitle != -1) {
				dest.writeByte((byte) 1);
				dest.writeInt(numTitle);
			} else {
				dest.writeByte((byte) -1);
			}

			Bundle bundle = new Bundle();
			Bundle bundle2;
			String team;
			for (Iterator<String> iterator = years.keySet().iterator(); iterator
					.hasNext();) {
				bundle2 = new Bundle();
				team = iterator.next();
				bundle2.putStringArrayList("yearsTP", years.get(team));
				bundle2.putString("teamTP", team);
				bundle.putBundle(team, bundle2);
			}
			//
			dest.writeBundle(bundle);

		} catch (Exception e) {
			Log.e("ParcelableError",
					"Se ha producido un error" + e.getMessage());
		}

	}

	public static final Parcelable.Creator<TituloPlayer> CREATOR = new Parcelable.Creator<TituloPlayer>() {
		@Override
		public TituloPlayer createFromParcel(Parcel in) {
			return new TituloPlayer(in);
		}

		@Override
		public TituloPlayer[] newArray(int size) {
			return new TituloPlayer[size];
		}
	};

}
