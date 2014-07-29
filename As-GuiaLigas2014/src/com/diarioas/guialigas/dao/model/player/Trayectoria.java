/**
 * 
 */
package com.diarioas.guialigas.dao.model.player;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * @author robertosanchez
 * 
 */
public class Trayectoria implements Parcelable {

	private int year;
	private String descripcion;
	private ArrayList<ItemTrayectoria> equipos;

	public Trayectoria(Parcel in) {
		readFromParcel(in);
	}

	public Trayectoria(int year, String descripcion) {
		this.year = year;
		this.descripcion = descripcion;
		this.equipos = new ArrayList<ItemTrayectoria>();
	}

	public Trayectoria(int year, String descripcion,
			ArrayList<ItemTrayectoria> equipos) {
		this.year = year;
		this.descripcion = descripcion;
		this.equipos = equipos;
	}

	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @param year
	 *            the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion
	 *            the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return the equipos
	 */
	public ArrayList<ItemTrayectoria> getEquipos() {
		return equipos;
	}

	/**
	 * @param equipos
	 *            the equipos to set
	 */
	public void setEquipos(ArrayList<ItemTrayectoria> equipos) {
		this.equipos = equipos;
	}

	public void addEquipo(String nombre, int partidos, int goles) {
		// this.equipos.add(new ItemTrayectoria(nombre, year, partidos, goles));
		this.equipos.add(new ItemTrayectoria(nombre, partidos, goles));
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
			this.year = in.readInt();

		if (in.readByte() == 1)
			this.descripcion = in.readString();

		if (in.readByte() == 1)
			in.readList(equipos, ItemTrayectoria.class.getClassLoader());
		// this.equipos = in.readArrayList(ItemTrayectoria.class
		// .getClassLoader());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		try {
			if (year != -1) {
				dest.writeByte((byte) 1);
				dest.writeInt(year);
			} else {
				dest.writeByte((byte) -1);
			}
			if (descripcion != null) {
				dest.writeByte((byte) 1);
				dest.writeString(descripcion);
			} else {
				dest.writeByte((byte) 0);
			}

			if (equipos.size() != 0) {
				dest.writeByte((byte) 1);
				dest.writeList(equipos);
			} else {
				dest.writeByte((byte) -1);
			}
		} catch (Exception e) {
			Log.e("ParcelableError",
					"Se ha producido un error" + e.getMessage());
		}

	}

	public static final Parcelable.Creator<Trayectoria> CREATOR = new Parcelable.Creator<Trayectoria>() {
		@Override
		public Trayectoria createFromParcel(Parcel in) {
			return new Trayectoria(in);
		}

		@Override
		public Trayectoria[] newArray(int size) {
			return new Trayectoria[size];
		}
	};
}
