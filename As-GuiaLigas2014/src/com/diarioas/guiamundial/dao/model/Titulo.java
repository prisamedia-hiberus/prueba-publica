/**
 * 
 */
package com.diarioas.guiamundial.dao.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * @author robertosanchez
 * 
 */
public abstract class Titulo implements Parcelable {

	protected String name;

	public Titulo(Parcel in) {
		readFromParcel(in);
	}

	/**
	 * @param name
	 */
	public Titulo(String name) {
		this.name = name;
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
		} catch (Exception e) {
			Log.e("ParcelableError",
					"Se ha producido un error" + e.getMessage());
		}

	}

}
