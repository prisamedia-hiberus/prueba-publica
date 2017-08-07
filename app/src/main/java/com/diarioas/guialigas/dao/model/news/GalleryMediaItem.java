package com.diarioas.guialigas.dao.model.news;

import java.io.Serializable;
import java.util.ArrayList;

public class GalleryMediaItem implements Serializable {
	private static final long serialVersionUID = 6L;

	private String title;
	private String link;
	private PhotoMediaItem coverPhoto;
	private ArrayList<PhotoMediaItem> photos;

	public GalleryMediaItem() {
		photos = new ArrayList<PhotoMediaItem>();
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
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
	public void setLink(String link) {
		this.link = link;
	}

	/**
	 * @return the coverPhoto
	 */
	public PhotoMediaItem getCoverPhoto() {
		return coverPhoto;
	}

	/**
	 * @param coverPhoto
	 *            the coverPhoto to set
	 */
	public void setCoverPhoto(PhotoMediaItem coverPhoto) {
		this.coverPhoto = coverPhoto;
	}

	/**
	 * @return the photos
	 */
	public ArrayList<PhotoMediaItem> getPhotos() {
		return photos;
	}

	/**
	 * @param photos
	 *            the photos to set
	 */
	public void setPhotos(ArrayList<PhotoMediaItem> photos) {
		this.photos = photos;
	}

	public void addMedia(PhotoMediaItem mediaItem) {
		photos.add(mediaItem);

	}

}
