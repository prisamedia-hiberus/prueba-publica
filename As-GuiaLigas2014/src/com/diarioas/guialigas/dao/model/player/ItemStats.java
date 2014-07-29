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

import com.diarioas.guialigas.utils.Defines.MatchStats;

/**
 * @author robertosanchez
 * 
 */
public class ItemStats implements Parcelable {
	private HashMap<String, HashMap<String, Integer>> data;// Team -- POS_* Dato

	public ItemStats(Parcel in) {
		readFromParcel(in);
		data = new HashMap<String, HashMap<String, Integer>>();
	}

	public ItemStats() {
		data = new HashMap<String, HashMap<String, Integer>>();
	}

	/**
	 * @return the data
	 */
	public HashMap<String, HashMap<String, Integer>> getData() {
		return data;
	}

	public int getGolesEncajados(String team) {
		try {
			return this.data.get(team).get(MatchStats.POS_GOLES_ENCAJADOS);
		} catch (Exception e) {
			return 0;
		}
	}

	public int getGolesMarcados(String team) {
		try {
			return this.data.get(team).get(MatchStats.POS_GOLES);
		} catch (Exception e) {
			return 0;
		}
	}

	public int getPartidosJugados(String team) {
		try {
			return this.data.get(team).get(MatchStats.POS_PARTIDOS_JUGADOS);
		} catch (Exception e) {
			return 0;
		}
	}

	public int getMinutos(String team) {
		try {
			return this.data.get(team).get(MatchStats.POS_MINUTOS);
		} catch (Exception e) {
			return 0;
		}
	}

	public int getTarjetasAmarillas(String team) {
		try {
			return this.data.get(team).get(MatchStats.POS_TARJETASA);
		} catch (Exception e) {
			return 0;
		}
	}

	public int getTarjetasRojas(String team) {
		try {
			return this.data.get(team).get(MatchStats.POS_TARJETASR);
		} catch (Exception e) {
			return 0;
		}
	}

	public int getParadas(String team) {
		try {
			return this.data.get(team).get(MatchStats.POS_PARADAS);
		} catch (Exception e) {
			return 0;
		}
	}

	public int getAsistencias(String team) {
		try {
			return this.data.get(team).get(MatchStats.POS_ASISTENCIAS);
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(HashMap<String, HashMap<String, Integer>> data) {
		this.data = data;
	}

	public void setGolesEncajados(String team, int numGoles) {
		initData(team);
		if (numGoles >= 0)
			this.data.get(team).put(MatchStats.POS_GOLES_ENCAJADOS, numGoles);
		else
			this.data.get(team).put(MatchStats.POS_GOLES_ENCAJADOS, 0);
	}

	public void setGolesAFavor(String team, int numGoles) {
		initData(team);
		if (numGoles >= 0)
			this.data.get(team).put(MatchStats.POS_GOLES, numGoles);
		else
			this.data.get(team).put(MatchStats.POS_GOLES, 0);
	}

	public void setPartidosJugados(String team, int numPartidos) {
		initData(team);
		if (numPartidos >= 0)
			this.data.get(team).put(MatchStats.POS_PARTIDOS_JUGADOS,
					numPartidos);
		else
			this.data.get(team).put(MatchStats.POS_PARTIDOS_JUGADOS, 0);
	}

	public void setMinutos(String team, int minutos) {
		initData(team);
		if (minutos >= 0)
			this.data.get(team).put(MatchStats.POS_MINUTOS, minutos);
		else
			this.data.get(team).put(MatchStats.POS_MINUTOS, 0);
	}

	public void setTarjetasAmarillas(String team, int numTarjetas) {
		initData(team);
		if (numTarjetas >= 0)
			this.data.get(team).put(MatchStats.POS_TARJETASA, numTarjetas);
		else
			this.data.get(team).put(MatchStats.POS_TARJETASA, 0);
	}

	public void setTarjetasRojas(String team, int numTarjetas) {
		initData(team);
		if (numTarjetas >= 0)
			this.data.get(team).put(MatchStats.POS_TARJETASR, numTarjetas);
		else
			this.data.get(team).put(MatchStats.POS_TARJETASR, 0);
	}

	public void setParadas(String team, int numParadas) {
		initData(team);
		if (numParadas >= 0)
			this.data.get(team).put(MatchStats.POS_PARADAS, numParadas);
		else
			this.data.get(team).put(MatchStats.POS_PARADAS, 0);
	}

	public void setAsistencias(String team, int numAsistencias) {
		initData(team);
		if (numAsistencias >= 0)
			this.data.get(team).put(MatchStats.POS_ASISTENCIAS, numAsistencias);
		else
			this.data.get(team).put(MatchStats.POS_ASISTENCIAS, 0);
	}

	private void initData(String team) {
		if (this.data.get(team) == null) {
			this.data.put(team, new HashMap<String, Integer>());
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

		Bundle bundle = in.readBundle();
		Bundle bundle2;
		String team, pos;
		HashMap<String, Integer> map2;
		while ((bundle = in.readBundle()) != null) {
			for (Iterator<String> iterator = bundle.keySet().iterator(); iterator
					.hasNext();) {
				team = iterator.next();
				team = team.substring(2, team.length());
				bundle2 = bundle.getBundle(team);
				map2 = new HashMap<String, Integer>();
				for (Iterator<String> iterator2 = bundle2.keySet().iterator(); iterator2
						.hasNext();) {
					pos = iterator2.next();
					map2.put(pos, bundle2.getInt(pos));
				}

				data.put(team, map2);
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

			Bundle bundle = new Bundle();
			Bundle bundle2 = new Bundle();
			String team, pos;
			for (Iterator<String> iterator = data.keySet().iterator(); iterator
					.hasNext();) {
				team = iterator.next();
				for (Iterator<String> iterator2 = data.get(team).keySet()
						.iterator(); iterator2.hasNext();) {
					pos = iterator2.next();
					bundle2.putInt(pos, data.get(team).get(pos));
				}
				bundle.putBundle("IS" + team, bundle2);
			}

			dest.writeBundle(bundle);
		} catch (Exception e) {
			Log.e("ParcelableError",
					"Se ha producido un error" + e.getMessage());
		}

	}

	public static final Parcelable.Creator<ItemStats> CREATOR = new Parcelable.Creator<ItemStats>() {
		@Override
		public ItemStats createFromParcel(Parcel in) {
			return new ItemStats(in);
		}

		@Override
		public ItemStats[] newArray(int size) {
			return new ItemStats[size];
		}
	};
}