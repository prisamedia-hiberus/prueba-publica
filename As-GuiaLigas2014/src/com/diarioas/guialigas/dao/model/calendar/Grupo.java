/**
 * 
 */
package com.diarioas.guialigas.dao.model.calendar;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.diarioas.guialigas.dao.model.clasificacion.Clasificacion;

/**
 * @author robertosanchez
 * 
 */
public class Grupo implements Parcelable {
	private String name;
	private boolean defecto;
	private ArrayList<Day> jornadas;
	private Clasificacion clasificacion;

	public Grupo(Parcel in) {
		jornadas = new ArrayList<Day>();
		readFromParcel(in);
	}

	public Grupo() {
		jornadas = new ArrayList<Day>();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the active
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
	 * @return the jornadas
	 */
	public ArrayList<Day> getJornadas() {
		return jornadas;
	}

	/**
	 * @param jornadas
	 *            the jornadas to set
	 */
	public void setJornadas(ArrayList<Day> jornadas) {
		this.jornadas = jornadas;
	}

	public void addJornada(Day jornada) {
		this.jornadas.add(jornada);
	}

	/**
	 * @return the clasificacion
	 */
	public Clasificacion getClasificacion() {
		return clasificacion;
	}

	/**
	 * @param clasificacion
	 *            the clasificacion to set
	 */
	public void setClasificacion(Clasificacion clasificacion) {
		this.clasificacion = clasificacion;
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
		if (in.readByte() == 1)
			this.defecto = (in.readInt() == 1);

		if (in.readByte() == 1)
			in.readList(jornadas, Day.class.getClassLoader());

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
			dest.writeByte((byte) 1);
			if (defecto)
				dest.writeInt(1);
			else
				dest.writeInt(0);
			if (jornadas.size() != 0) {
				dest.writeByte((byte) 1);
				dest.writeList(jornadas);
			} else {
				dest.writeByte((byte) -1);
			}
		} catch (Exception e) {
			Log.e("ParcelableError",
					"Se ha producido un error" + e.getMessage());
		}

	}

	public static final Parcelable.Creator<Grupo> CREATOR = new Parcelable.Creator<Grupo>() {
		@Override
		public Grupo createFromParcel(Parcel in) {
			return new Grupo(in);
		}

		@Override
		public Grupo[] newArray(int size) {
			return new Grupo[size];
		}
	};

}
