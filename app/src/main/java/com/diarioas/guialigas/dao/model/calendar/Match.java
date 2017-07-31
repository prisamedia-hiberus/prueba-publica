/**
 * 
 */
package com.diarioas.guialigas.dao.model.calendar;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.diarioas.guialigas.dao.model.carrusel.Gol;
import com.diarioas.guialigas.dao.model.carrusel.Tarjeta;

/**
 * @author robertosanchez
 * 
 */
public class Match implements Parcelable {

	private String date;
	private String localId;
	private String localTeamName;
	private String localTeamShieldName;
	private int markerLocalTeam = -1;
	private String awayId;
	private String awayTeamName;
	private String awayTeamShieldName;
	private int markerAwayTeam = -1;
	private String link;
	private String cronica;
	private String dataLink;
	private String place;
	private String state;
	private int stateCode = -1;
	private int minute = -1;
	private ArrayList<String> televisiones;
	private String referee;
	private ArrayList<Gol> goles;
	private ArrayList<Tarjeta> tarjetasRojas;
	private ArrayList<Tarjeta> tarjetasAmarillas;
	private HashMap<String, String> referencias;

	public Match(Parcel in) {
		televisiones = new ArrayList<String>();
		goles = new ArrayList<Gol>();
		tarjetasRojas = new ArrayList<Tarjeta>();
		tarjetasAmarillas = new ArrayList<Tarjeta>();
		referencias = new HashMap<String, String>();
		readFromParcel(in);
	}

	public Match() {
		televisiones = new ArrayList<String>();
		goles = new ArrayList<Gol>();
		tarjetasRojas = new ArrayList<Tarjeta>();
		tarjetasAmarillas = new ArrayList<Tarjeta>();
		referencias = new HashMap<String, String>();
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
	 * @return the localId
	 */
	public String getLocalId() {
		return localId;
	}

	/**
	 * @param localId
	 *            the localId to set
	 */
	public void setLocalId(String localId) {
		this.localId = localId;
	}

	/**
	 * @return the localTeamName
	 */
	public String getLocalTeamName() {
		return localTeamName;
	}

	/**
	 * @param localTeamName
	 *            the localTeamName to set
	 */
	public void setLocalTeamName(String localTeamName) {
		this.localTeamName = localTeamName;
	}

	/**
	 * @return the localTeamShieldImageName
	 */
	public String getLocalTeamShieldName() {
		return localTeamShieldName;
	}

	/**
	 * @param localTeamShieldImageName
	 *            the localTeamShieldImageName to set
	 */
	public void setLocalTeamShieldName(String localTeamShieldName) {
		this.localTeamShieldName = localTeamShieldName;
	}

	/**
	 * @return the markerLocalTeam
	 */
	public int getMarkerLocalTeam() {
		return markerLocalTeam;
	}

	/**
	 * @param markerLocalTeam
	 *            the markerLocalTeam to set
	 */
	public void setMarkerLocalTeam(int markerLocalTeam) {
		this.markerLocalTeam = markerLocalTeam;
	}

	/**
	 * @return the awayId
	 */
	public String getAwayId() {
		return awayId;
	}

	/**
	 * @param awayId
	 *            the awayId to set
	 */
	public void setAwayId(String awayId) {
		this.awayId = awayId;
	}

	/**
	 * @return the awayTeamName
	 */
	public String getAwayTeamName() {
		return awayTeamName;
	}

	/**
	 * @param awayTeamName
	 *            the awayTeamName to set
	 */
	public void setAwayTeamName(String awayTeamName) {
		this.awayTeamName = awayTeamName;
	}

	/**
	 * @return the awayTeamShieldName
	 */
	public String getAwayTeamShieldName() {
		return awayTeamShieldName;
	}

	/**
	 * @param awayTeamShieldName
	 *            the awayTeamShieldName to set
	 */
	public void setAwayTeamShieldName(String awayTeamShieldName) {
		this.awayTeamShieldName = awayTeamShieldName;
	}

	/**
	 * @return the markerAwayTeam
	 */
	public int getMarkerAwayTeam() {
		return markerAwayTeam;
	}

	/**
	 * @param markerAwayTeam
	 *            the markerAwayTeam to set
	 */
	public void setMarkerAwayTeam(int markerAwayTeam) {
		this.markerAwayTeam = markerAwayTeam;
	}

	/**
	 * @return the link
	 */
	public String getLink() {
		return link;
	}

	/**
	 * @param link
	 *            the link to set
	 */
	public void setLink(String url) {
		if (url != null && !url.startsWith("http://")
				&& !url.startsWith("https://"))
			url = "http://" + url;
		this.link = url;
	}

	/**
	 * @return the cronica
	 */
	public String getCronica() {
		return cronica;
	}

	/**
	 * @param cronica
	 *            the cronica to set
	 */
	public void setCronica(String url) {
		if (url != null && !url.startsWith("http://")
				&& !url.startsWith("https://"))
			url = "http://" + url;
		this.cronica = url;
	}

	/**
	 * @return the dataLink
	 */
	public String getDataLink() {
		return dataLink;
	}

	/**
	 * @param dataLink
	 *            the dataLink to set
	 */
	public void setDataLink(String url) {
		if (url != null && !url.startsWith("http://")
				&& !url.startsWith("https://"))
			url = "http://" + url;
		this.dataLink = url;
	}

	/**
	 * @return the place
	 */
	public String getPlace() {
		return place;
	}

	/**
	 * @param readPlace
	 */
	public void setPlace(String place) {
		this.place = place;

	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the stateCode
	 */
	public int getStateCode() {
		return stateCode;
	}

	/**
	 * @param stateCode
	 *            the stateCode to set
	 */
	public void setStateCode(int stateCode) {
		this.stateCode = stateCode;
	}

	/**
	 * @return the minute
	 */
	public int getMinute() {
		return minute;
	}

	/**
	 * @param minute
	 *            the minute to set
	 */
	public void setMinute(int minute) {
		this.minute = minute;
	}

	/**
	 * @return the televisiones
	 */
	public ArrayList<String> getTelevisiones() {
		return televisiones;
	}

	/**
	 * @param televisiones
	 *            the televisiones to set
	 */
	public void setTelevisiones(ArrayList<String> televisiones) {
		this.televisiones = televisiones;
	}

	/**
	 * 
	 * @param name
	 * @param icono
	 */
	public void addTelevision(String id) {
		this.getTelevisiones().add("tv_" + id + ".png");
	}

	/**
	 * @return the referee
	 */
	public String getReferee() {
		return referee;
	}

	/**
	 * @param referee
	 *            the referee to set
	 */
	public void setReferee(String referee) {
		this.referee = referee;
	}

	/**
	 * @return the goles
	 */
	public ArrayList<Gol> getGoles() {
		return goles;
	}

	/**
	 * @param goles
	 *            the goles to set
	 */
	public void setGoles(ArrayList<Gol> goles) {
		this.goles = goles;
	}

	/**
	 * @param gol
	 */
	public void addGol(Gol gol) {
		this.goles.add(gol);

	}

	/**
	 * @return the tarjetasRojas
	 */
	public ArrayList<Tarjeta> getTarjetasRojas() {
		return tarjetasRojas;
	}

	/**
	 * @param tarjetas
	 *            the tarjetasRojas to set
	 */
	public void setTarjetasRojas(ArrayList<Tarjeta> tarjetas) {
		this.tarjetasRojas = tarjetas;
	}

	/**
	 * @param tarjeta
	 */
	public void addTarjetaRoja(Tarjeta tarjeta) {
		this.tarjetasRojas.add(tarjeta);

	}

	/**
	 * @return the tarjetasAmarillas
	 */
	public ArrayList<Tarjeta> getTarjetasAmarillas() {
		return tarjetasAmarillas;
	}

	/**
	 * @param tarjetas
	 *            the tarjetasAmarillas to set
	 */
	public void setTarjetasAmarillas(ArrayList<Tarjeta> tarjetas) {
		this.tarjetasAmarillas = tarjetas;
	}

	/**
	 * @param tarjeta
	 */
	public void addTarjetaAmarilla(Tarjeta tarjeta) {
		this.tarjetasAmarillas.add(tarjeta);

	}

	/**
	 * @return the referencias
	 */
	public HashMap<String, String> getReferencias() {
		return referencias;
	}

	/**
	 * @param referencias
	 *            the referencias to set
	 */
	public void setReferencias(HashMap<String, String> referencias) {
		this.referencias = referencias;
	}

	/**
	 * @param key
	 * @param url
	 */
	public void addReferencia(String key, String url) {
		this.referencias.put(key, url);

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
			this.date = in.readString();
		if (in.readByte() == 1)
			this.localId = in.readString();
		if (in.readByte() == 1)
			this.localTeamName = in.readString();
		if (in.readByte() == 1)
			this.localTeamShieldName = in.readString();
		if (in.readByte() == 1)
			this.markerLocalTeam = in.readInt();
		if (in.readByte() == 1)
			this.awayId = in.readString();
		if (in.readByte() == 1)
			this.awayTeamName = in.readString();
		if (in.readByte() == 1)
			this.awayTeamShieldName = in.readString();
		if (in.readByte() == 1)
			this.markerAwayTeam = in.readInt();
		if (in.readByte() == 1)
			this.link = in.readString();
		if (in.readByte() == 1)
			this.cronica = in.readString();
		if (in.readByte() == 1)
			this.dataLink = in.readString();
		if (in.readByte() == 1)
			this.place = in.readString();
		if (in.readByte() == 1)
			this.state = in.readString();
		if (in.readByte() == 1)
			this.stateCode = in.readInt();
		if (in.readByte() == 1)
			this.minute = in.readInt();
		if (in.readByte() == 1) {
			Bundle bundle = in.readBundle();
			this.televisiones = bundle.getStringArrayList("televisiones");
		}
		if (in.readByte() == 1)
			this.referee = in.readString();
		if (in.readByte() == 1)
			in.readList(goles, Gol.class.getClassLoader());
		if (in.readByte() == 1)
			in.readList(tarjetasRojas, Tarjeta.class.getClassLoader());
		if (in.readByte() == 1)
			in.readList(tarjetasAmarillas, Tarjeta.class.getClassLoader());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		try {
			if (date != null) {
				dest.writeByte((byte) 1);
				dest.writeString(date);
			} else {
				dest.writeByte((byte) 0);
			}
			if (localId != null) {
				dest.writeByte((byte) 1);
				dest.writeString(localId);
			} else {
				dest.writeByte((byte) 0);
			}
			if (localTeamName != null) {
				dest.writeByte((byte) 1);
				dest.writeString(localTeamName);
			} else {
				dest.writeByte((byte) 0);
			}
			if (localTeamShieldName != null) {
				dest.writeByte((byte) 1);
				dest.writeString(localTeamShieldName);
			} else {
				dest.writeByte((byte) 0);
			}
			if (markerLocalTeam != -1) {
				dest.writeByte((byte) 1);
				dest.writeInt(markerLocalTeam);
			} else {
				dest.writeByte((byte) 0);
			}
			if (awayId != null) {
				dest.writeByte((byte) 1);
				dest.writeString(awayId);
			} else {
				dest.writeByte((byte) 0);
			}
			if (awayTeamName != null) {
				dest.writeByte((byte) 1);
				dest.writeString(awayTeamName);
			} else {
				dest.writeByte((byte) 0);
			}
			if (awayTeamShieldName != null) {
				dest.writeByte((byte) 1);
				dest.writeString(awayTeamShieldName);
			} else {
				dest.writeByte((byte) 0);
			}
			if (markerAwayTeam != -1) {
				dest.writeByte((byte) 1);
				dest.writeInt(markerAwayTeam);
			} else {
				dest.writeByte((byte) 0);
			}
			if (link != null) {
				dest.writeByte((byte) 1);
				dest.writeString(link);
			} else {
				dest.writeByte((byte) 0);
			}
			if (cronica != null) {
				dest.writeByte((byte) 1);
				dest.writeString(cronica);
			} else {
				dest.writeByte((byte) 0);
			}
			if (dataLink != null) {
				dest.writeByte((byte) 1);
				dest.writeString(dataLink);
			} else {
				dest.writeByte((byte) 0);
			}
			if (place != null) {
				dest.writeByte((byte) 1);
				dest.writeString(place);
			} else {
				dest.writeByte((byte) 0);
			}
			if (state != null) {
				dest.writeByte((byte) 1);
				dest.writeString(state);
			} else {
				dest.writeByte((byte) 0);
			}

			if (stateCode != -1) {
				dest.writeByte((byte) 1);
				dest.writeInt(stateCode);
			} else {
				dest.writeByte((byte) 0);
			}
			if (minute != -1) {
				dest.writeByte((byte) 1);
				dest.writeInt(minute);
			} else {
				dest.writeByte((byte) 0);
			}
			if (televisiones.size() > 0) {
				dest.writeByte((byte) 1);
				Bundle bundle = new Bundle();
				bundle.putStringArrayList("televisiones", televisiones);
				dest.writeBundle(bundle);
			} else {
				dest.writeByte((byte) 0);
			}

			if (referee != null) {
				dest.writeByte((byte) 1);
				dest.writeString(referee);
			} else {
				dest.writeByte((byte) 0);
			}
			if (goles.size() > 0) {
				dest.writeByte((byte) 1);
				dest.writeList(goles);
			} else {
				dest.writeByte((byte) 0);
			}
			if (tarjetasRojas.size() > 0) {
				dest.writeByte((byte) 1);
				dest.writeList(tarjetasRojas);
			} else {
				dest.writeByte((byte) 0);
			}
			if (tarjetasAmarillas.size() > 0) {
				dest.writeByte((byte) 1);
				dest.writeList(tarjetasAmarillas);
			} else {
				dest.writeByte((byte) 0);
			}

		} catch (Exception e) {
			Log.e("ParcelableError",
					"Se ha producido un error" + e.getMessage());
		}

	}

	public static final Parcelable.Creator<Match> CREATOR = new Parcelable.Creator<Match>() {
		@Override
		public Match createFromParcel(Parcel in) {
			return new Match(in);
		}

		@Override
		public Match[] newArray(int size) {
			return new Match[size];
		}
	};

}
