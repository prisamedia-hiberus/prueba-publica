/**
 * 
 */
package com.diarioas.guiamundial.dao.model.carrusel;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * @author robertosanchez
 * 
 */
public class PlayerOnField implements Parcelable {
	private int id = -1;
	private String name;
	private String url;
	private String urlPhoto;
	private int dorsal = -1;
	private String puesto;
	private int position = -1;
	private String dem;

	public PlayerOnField() {

	}

	public PlayerOnField(Parcel in) {
		readFromParcel(in);

	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
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
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the urlPhoto
	 */
	public String getUrlPhoto() {
		return urlPhoto;
	}

	/**
	 * @param urlPhoto
	 *            the urlPhoto to set
	 */
	public void setUrlPhoto(String urlPhoto) {
		this.urlPhoto = urlPhoto;
	}

	/**
	 * @return the dorsal
	 */
	public int getDorsal() {
		return dorsal;
	}

	/**
	 * @param dorsal
	 *            the dorsal to set
	 */
	public void setDorsal(int dorsal) {
		this.dorsal = dorsal;
	}

	/**
	 * @return the puesto
	 */
	public String getPuesto() {
		return puesto;
	}

	/**
	 * @param puesto
	 *            the puesto to set
	 */
	public void setPuesto(String puesto) {
		this.puesto = puesto;
	}

	/**
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * @return the dem
	 */
	public String getDem() {
		return dem;
	}

	/**
	 * @param dem
	 *            the dem to set
	 */
	public void setDem(String dem) {
		this.dem = dem;
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
			this.id = in.readInt();
		if (in.readByte() == 1)
			this.name = in.readString();
		if (in.readByte() == 1)
			this.url = in.readString();
		if (in.readByte() == 1)
			this.urlPhoto = in.readString();
		if (in.readByte() == 1)
			this.dorsal = in.readInt();
		if (in.readByte() == 1)
			this.puesto = in.readString();
		if (in.readByte() == 1)
			this.position = in.readInt();
		if (in.readByte() == 1)
			this.dem = in.readString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		try {
			if (id != -1) {
				dest.writeByte((byte) 1);
				dest.writeInt(id);
			} else {
				dest.writeByte((byte) 0);
			}
			if (name != null) {
				dest.writeByte((byte) 1);
				dest.writeString(name);
			} else {
				dest.writeByte((byte) 0);
			}
			if (url != null) {
				dest.writeByte((byte) 1);
				dest.writeString(url);
			} else {
				dest.writeByte((byte) 0);
			}
			if (urlPhoto != null) {
				dest.writeByte((byte) 1);
				dest.writeString(urlPhoto);
			} else {
				dest.writeByte((byte) 0);
			}
			if (dorsal != -1) {
				dest.writeByte((byte) 1);
				dest.writeInt(dorsal);
			} else {
				dest.writeByte((byte) 0);
			}
			if (puesto != null) {
				dest.writeByte((byte) 1);
				dest.writeString(puesto);
			} else {
				dest.writeByte((byte) 0);
			}
			if (position != -1) {
				dest.writeByte((byte) 1);
				dest.writeInt(position);
			} else {
				dest.writeByte((byte) 0);
			}
			if (dem != null) {
				dest.writeByte((byte) 1);
				dest.writeString(dem);
			} else {
				dest.writeByte((byte) 0);
			}
		} catch (Exception e) {
			Log.e("ParcelableError",
					"Se ha producido un error" + e.getMessage());
		}

	}

	public static final Parcelable.Creator<PlayerOnField> CREATOR = new Parcelable.Creator<PlayerOnField>() {
		@Override
		public PlayerOnField createFromParcel(Parcel in) {
			return new PlayerOnField(in);
		}

		@Override
		public PlayerOnField[] newArray(int size) {
			return new PlayerOnField[size];
		}
	};

}
