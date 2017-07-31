/**
 * 
 */
package com.diarioas.guialigas.dao.model.player;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * @author robertosanchez
 * 
 */
public class ItemTrayectoria implements Parcelable {
	private String nombre;
	// private int year = -1;
	private int partidos = -1;
	private int goles = -1;

	public ItemTrayectoria(Parcel in) {
		readFromParcel(in);
	}

	public ItemTrayectoria(String nombre, int partidos, int goles) {
		// public ItemTrayectoria(String nombre, int year, int partidos, int
		// goles) {
		this.nombre = nombre;
		// this.year = year;
		this.partidos = partidos;
		this.goles = goles;

	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre
	 *            the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	// /**
	// * @return the year
	// */
	// public int getYear() {
	// return year;
	// }
	//
	// /**
	// * @param year
	// * the year to set
	// */
	// public void setYear(int year) {
	// this.year = year;
	// }

	/**
	 * @return the partidos
	 */
	public int getPartidos() {
		return partidos;
	}

	/**
	 * @param partidos
	 *            the partidos to set
	 */
	public void setPartidos(int partidos) {
		this.partidos = partidos;
	}

	/**
	 * @return the goles
	 */
	public int getGoles() {
		return goles;
	}

	/**
	 * @param goles
	 *            the goles to set
	 */
	public void setGoles(int goles) {
		this.goles = goles;
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
			this.nombre = in.readString();
		// if (in.readByte() == 1)
		// this.year = in.readInt();
		if (in.readByte() == 1)
			this.partidos = in.readInt();
		if (in.readByte() == 1)
			this.goles = in.readInt();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		try {
			if (nombre != null) {
				dest.writeByte((byte) 1);
				dest.writeString(nombre);
			} else {
				dest.writeByte((byte) 0);
			}
			// if (year != -1) {
			// dest.writeByte((byte) 1);
			// dest.writeInt(year);
			// } else {
			// dest.writeByte((byte) -1);
			// }

			if (partidos != -1) {
				dest.writeByte((byte) 1);
				dest.writeInt(partidos);
			} else {
				dest.writeByte((byte) -1);
			}
			if (goles != -1) {
				dest.writeByte((byte) 1);
				dest.writeInt(goles);
			} else {
				dest.writeByte((byte) -1);
			}

		} catch (Exception e) {
			Log.e("ParcelableError",
					"Se ha producido un error" + e.getMessage());
		}
	}

	public static final Parcelable.Creator<ItemTrayectoria> CREATOR = new Parcelable.Creator<ItemTrayectoria>() {
		@Override
		public ItemTrayectoria createFromParcel(Parcel in) {
			return new ItemTrayectoria(in);
		}

		@Override
		public ItemTrayectoria[] newArray(int size) {
			return new ItemTrayectoria[size];
		}
	};

}
