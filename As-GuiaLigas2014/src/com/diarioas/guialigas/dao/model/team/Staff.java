/**
 * 
 */
package com.diarioas.guialigas.dao.model.team;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * @author robertosanchez
 * 
 */
public class Staff implements Parcelable {

	private String charge;
	private String name;
	private String born;
	private String contract;
	private String photo;
	private String history;

	public Staff(Parcel in) {
		readFromParcel(in);
	}

	public Staff() {

	}

	/**
	 * @return the charge
	 */
	public String getCharge() {
		return charge;
	}

	/**
	 * @param charge
	 *            the charge to set
	 */
	public void setCharge(String charge) {
		this.charge = charge;
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
	 * @return the born
	 */
	public String getBorn() {
		return born;
	}

	/**
	 * @param born
	 *            the born to set
	 */
	public void setBorn(String born) {
		this.born = born;
	}

	/**
	 * @return the contract
	 */
	public String getContract() {
		return contract;
	}

	/**
	 * @param contract
	 *            the contract to set
	 */
	public void setContract(String contract) {
		this.contract = contract;
	}

	/**
	 * @return the photo
	 */
	public String getPhoto() {
		return photo;
	}

	/**
	 * @param photo
	 *            the photo to set
	 */
	public void setPhoto(String photo) {
		this.photo = photo;
	}

	/**
	 * @return the history
	 */
	public String getHistory() {
		return history;
	}

	/**
	 * @param history
	 *            the history to set
	 */
	public void setHistory(String history) {
		this.history = history;
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
	protected void readFromParcel(Parcel in) {

		if (in.readByte() == 1)
			this.charge = in.readString();
		if (in.readByte() == 1)
			this.name = in.readString();
		// if (in.readByte() == 1)
		// this.born = in.readString();
		// if (in.readByte() == 1)
		// this.contract = in.readString();
		if (in.readByte() == 1)
			this.photo = in.readString();
		if (in.readByte() == 1)
			this.history = in.readString();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		try {
			if (charge != null) {
				dest.writeByte((byte) 1);
				dest.writeString(charge);
			} else {
				dest.writeByte((byte) 0);
			}
			if (name != null) {
				dest.writeByte((byte) 1);
				dest.writeString(name);
			} else {
				dest.writeByte((byte) 0);
			}
			// if (born != null) {
			// dest.writeByte((byte) 1);
			// dest.writeString(born);
			// } else {
			// dest.writeByte((byte) 0);
			// }
			// if (contract != null) {
			// dest.writeByte((byte) 1);
			// dest.writeString(contract);
			// } else {
			// dest.writeByte((byte) 0);
			// }
			if (photo != null) {
				dest.writeByte((byte) 1);
				dest.writeString(photo);
			} else {
				dest.writeByte((byte) 0);
			}
			if (history != null) {
				dest.writeByte((byte) 1);
				dest.writeString(history);
			} else {
				dest.writeByte((byte) 0);
			}
		} catch (Exception e) {
			Log.e("ParcelableError",
					"Se ha producido un error" + e.getMessage());
		}

	}

	public static final Parcelable.Creator<Staff> CREATOR = new Parcelable.Creator<Staff>() {
		@Override
		public Staff createFromParcel(Parcel in) {
			return new Staff(in);
		}

		@Override
		public Staff[] newArray(int size) {
			return new Staff[size];
		}
	};

}
