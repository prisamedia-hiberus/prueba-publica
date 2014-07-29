/**
 * 
 */
package com.diarioas.guialigas.dao.model.player;

import java.util.HashMap;
import java.util.Iterator;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * @author robertosanchez
 * 
 */
public class PlayerStats implements Parcelable {

	private String year;
	private HashMap<String, ItemStats> stats;// Competicion --Info

	public PlayerStats(Parcel in) {
		readFromParcel(in);
		stats = new HashMap<String, ItemStats>();
	}

	public PlayerStats(String year) {
		this.year = year;
		stats = new HashMap<String, ItemStats>();
	}

	/**
	 * @return the year
	 */
	public String getYear() {
		return year;
	}

	/**
	 * @param year
	 *            the year to set
	 */
	public void setYear(String year) {
		this.year = year;
	}

	/**
	 * @return the stats
	 */
	public HashMap<String, ItemStats> getStats() {
		return stats;
	}

	public ItemStats getStats(String comp) {
		return stats.get(comp);
	}

	/**
	 * @param comp
	 * @param int1
	 */
	public void addStat(String comp, String team, int goles, int golesEnc,
			int pj, int minutos, int ta, int tr, int numParadas,
			int numAsistencias) {
		if (!stats.containsKey(comp))
			stats.put(comp, new ItemStats());

		stats.get(comp).setGolesEncajados(team, golesEnc);
		stats.get(comp).setGolesAFavor(team, goles);
		stats.get(comp).setPartidosJugados(team, pj);
		stats.get(comp).setMinutos(team, minutos);
		stats.get(comp).setTarjetasAmarillas(team, ta);
		stats.get(comp).setTarjetasRojas(team, tr);
		stats.get(comp).setParadas(team, numParadas);
		stats.get(comp).setAsistencias(team, numAsistencias);

	}

	/**
	 * @param stats
	 *            the stats to set
	 */
	public void setStats(HashMap<String, ItemStats> stats) {
		this.stats = stats;
	}

	public void setTeamStat(String comp, ItemStats item) {
		// if (!stats.containsKey(comp))
		stats.put(comp, item);

		// stats.get(comp).add(item);

	}

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
			this.year = in.readString();

		Bundle bundle = in.readBundle();
		ItemStats item;
		String comp;
		while ((bundle = in.readBundle()) != null) {
			for (Iterator<String> iterator = bundle.keySet().iterator(); iterator
					.hasNext();) {
				comp = iterator.next();
				comp = comp.substring(3, comp.length());
				item = bundle.getParcelable(comp);
				stats.put(comp, item);
			}
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
			if (year != null) {
				dest.writeByte((byte) 1);
				dest.writeString(year);
			} else {
				dest.writeByte((byte) 0);
			}

			Bundle bundle = new Bundle();
			String comp;
			for (Iterator<String> iterator = stats.keySet().iterator(); iterator
					.hasNext();) {
				comp = iterator.next();
				bundle.putParcelable("PST" + comp, stats.get(comp));
			}

			dest.writeBundle(bundle);
		} catch (Exception e) {
			Log.e("ParcelableError",
					"Se ha producido un error" + e.getMessage());
		}

	}

	public static final Parcelable.Creator<PlayerStats> CREATOR = new Parcelable.Creator<PlayerStats>() {
		@Override
		public PlayerStats createFromParcel(Parcel in) {
			return new PlayerStats(in);
		}

		@Override
		public PlayerStats[] newArray(int size) {
			return new PlayerStats[size];
		}
	};
}
