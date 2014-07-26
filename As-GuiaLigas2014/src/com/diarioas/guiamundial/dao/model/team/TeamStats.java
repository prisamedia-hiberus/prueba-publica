/**
 * 
 */
package com.diarioas.guiamundial.dao.model.team;

import java.util.HashMap;
import java.util.Iterator;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.diarioas.guiamundial.utils.Defines.MatchStats;

/**
 * @author robertosanchez
 * 
 */
public class TeamStats implements Parcelable {

	/**
	 * 
	 */

	private String year;
	private HashMap<String, HashMap<String, Integer>> stats;// Competicion --
															// POS_* Dato

	public TeamStats(Parcel in) {
		readFromParcel(in);
		stats = new HashMap<String, HashMap<String, Integer>>();
	}

	/**
	 * 
	 */
	public TeamStats() {
		stats = new HashMap<String, HashMap<String, Integer>>();
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
	public HashMap<String, HashMap<String, Integer>> getStats() {
		return stats;
	}

	public int getGolesAFavor(String comp) {
		try {
			return this.stats.get(comp).get(MatchStats.POS_GOLES);
		} catch (Exception e) {
			return 0;
		}
	}

	public int getPartidosJugados(String comp) {
		try {
			return this.stats.get(comp).get(MatchStats.POS_PARTIDOS_JUGADOS);
		} catch (Exception e) {
			return 0;
		}
	}

	public int getPartidosGanados(String comp) {
		try {
			return this.stats.get(comp).get(MatchStats.POS_PARTIDOS_GANADOS);
		} catch (Exception e) {
			return 0;
		}
	}

	public int getPartidosEmpatados(String comp) {
		try {
			return this.stats.get(comp).get(MatchStats.POS_PARTIDOS_EMPATADOS);
		} catch (Exception e) {
			return 0;
		}
	}

	public int getPartidosPerdidos(String comp) {
		try {
			return this.stats.get(comp).get(MatchStats.POS_PARTIDOS_PERDIDOS);
		} catch (Exception e) {
			return 0;
		}
	}

	public int getTarjetasAmarillas(String comp) {
		try {
			return this.stats.get(comp).get(MatchStats.POS_TARJETASA);
		} catch (Exception e) {
			return 0;
		}
	}

	public int getTarjetasRojas(String comp) {
		try {
			return this.stats.get(comp).get(MatchStats.POS_TARJETASR);
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * @param stats
	 *            the stats to set
	 */
	public void setStats(HashMap<String, HashMap<String, Integer>> stats) {
		this.stats = stats;
	}

	public void setGolesAFavor(String comp, int numGoles) {
		initCompetition(comp);
		if (numGoles >= 0)
			this.stats.get(comp).put(MatchStats.POS_GOLES, numGoles);
		else
			this.stats.get(comp).put(MatchStats.POS_GOLES, 0);
	}

	public void setPartidosJugados(String comp, int numPartidos) {
		initCompetition(comp);
		if (numPartidos >= 0)
			this.stats.get(comp).put(MatchStats.POS_PARTIDOS_JUGADOS,
					numPartidos);
		else
			this.stats.get(comp).put(MatchStats.POS_PARTIDOS_JUGADOS, 0);
	}

	public void setPartidosGanados(String comp, int numPartidos) {
		initCompetition(comp);
		if (numPartidos >= 0)
			this.stats.get(comp).put(MatchStats.POS_PARTIDOS_GANADOS,
					numPartidos);
		else
			this.stats.get(comp).put(MatchStats.POS_PARTIDOS_GANADOS, 0);
	}

	public void setPartidosEmpatados(String comp, int numPartidos) {
		initCompetition(comp);
		if (numPartidos >= 0)
			this.stats.get(comp).put(MatchStats.POS_PARTIDOS_EMPATADOS,
					numPartidos);
		else
			this.stats.get(comp).put(MatchStats.POS_PARTIDOS_EMPATADOS, 0);
	}

	public void setPartidosPerdidos(String comp, int numPartidos) {
		initCompetition(comp);
		if (numPartidos >= 0)
			this.stats.get(comp).put(MatchStats.POS_PARTIDOS_PERDIDOS,
					numPartidos);
		else
			this.stats.get(comp).put(MatchStats.POS_PARTIDOS_PERDIDOS, 0);
	}

	public void setTarjetasAmarillas(String comp, int numTarjetas) {
		initCompetition(comp);
		if (numTarjetas >= 0)
			this.stats.get(comp).put(MatchStats.POS_TARJETASA, numTarjetas);
		else
			this.stats.get(comp).put(MatchStats.POS_TARJETASA, 0);
	}

	public void setTarjetasRojas(String comp, int numTarjetas) {
		initCompetition(comp);
		if (numTarjetas >= 0)
			this.stats.get(comp).put(MatchStats.POS_TARJETASR, numTarjetas);
		else
			this.stats.get(comp).put(MatchStats.POS_TARJETASR, 0);
	}

	private void initCompetition(String comp) {
		if (this.stats.get(comp) == null) {
			this.stats.put(comp, new HashMap<String, Integer>());
		}
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

		Bundle bundle;
		// = in.readBundle();
		Bundle bundle2;
		String comp, pos;
		HashMap<String, Integer> map2;
		while ((bundle = in.readBundle()) != null) {
			for (Iterator<String> iterator = bundle.keySet().iterator(); iterator
					.hasNext();) {
				comp = iterator.next();
				bundle2 = bundle.getBundle(comp);
				map2 = new HashMap<String, Integer>();
				for (Iterator<String> iterator2 = bundle2.keySet().iterator(); iterator2
						.hasNext();) {
					pos = iterator2.next();
					map2.put(pos, bundle2.getInt(pos));
				}

				stats.put(comp, map2);
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
			Bundle bundle2 = new Bundle();
			String comp, pos;
			for (Iterator<String> iterator = stats.keySet().iterator(); iterator
					.hasNext();) {
				comp = iterator.next();
				for (Iterator<String> iterator2 = stats.get(comp).keySet()
						.iterator(); iterator2.hasNext();) {
					pos = iterator2.next();
					bundle2.putInt(pos, stats.get(comp).get(pos));
				}
				bundle.putBundle(comp, bundle2);
			}

			dest.writeBundle(bundle);
		} catch (Exception e) {
			Log.e("ParcelableError",
					"Se ha producido un error" + e.getMessage());
		}

	}

	public static final Parcelable.Creator<TeamStats> CREATOR = new Parcelable.Creator<TeamStats>() {
		@Override
		public TeamStats createFromParcel(Parcel in) {
			return new TeamStats(in);
		}

		@Override
		public TeamStats[] newArray(int size) {
			return new TeamStats[size];
		}
	};
}
