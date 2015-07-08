package com.diarioas.guialigas.dao.model.news;

import java.io.Serializable;
import java.util.ArrayList;

public class NewsItemTag implements Serializable {

	private static final long serialVersionUID = 4L;

	private boolean isNativeAds = false;

	private String link;
	private int timeStamp = 0;
	private String preTitle;
	private String typology;
	private String title;
	private String subtitle;
	private String trackKey;
	private String section = null;
	private String nativeMiniLogoUrl;
	private String author;
	private String abstract_;
	private String tags;
	private String json;
	private String body;
	private ArrayList<NewsItemMedia> photos;
	private ArrayList<NewsItemMedia> videos;

	private int jumpToWeb = 0;

	public NewsItemTag() {
		photos = new ArrayList<NewsItemMedia>();
		videos = new ArrayList<NewsItemMedia>();
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
	 * @return the timeStamp
	 */
	public int getTimeStamp() {
		return timeStamp;
	}

	/**
	 * @param timeStamp
	 *            the timeStamp to set
	 */
	public void setTimeStamp(int timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 * @return the preTitle
	 */
	public String getPreTitle() {
		return preTitle;
	}

	/**
	 * @param preTitle
	 *            the preTitle to set
	 */
	public void setPreTitle(String preTitle) {
		this.preTitle = preTitle;
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
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @param author
	 *            the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @return the abstract_
	 */
	public String getAbstract_() {
		return abstract_;
	}

	/**
	 * @param abstract_
	 *            the abstract_ to set
	 */
	public void setAbstract_(String abstract_) {
		this.abstract_ = abstract_;
	}

	/**
	 * @return the tags
	 */
	public String getTags() {
		return tags;
	}

	/**
	 * @param tags
	 *            the tags to set
	 */
	public void setTags(String tags) {
		this.tags = tags;
	}

	/**
	 * @return the json
	 */
	public String getJson() {
		return json;
	}

	/**
	 * @param json
	 *            the json to set
	 */
	public void setJson(String json) {
		this.json = json;
	}

	/**
	 * /**
	 * 
	 * @return the photos
	 */
	public ArrayList<NewsItemMedia> getPhotos() {
		return photos;
	}

	/**
	 * @param photos
	 *            the photos to set
	 */
	public void setPhotos(ArrayList<NewsItemMedia> photos) {
		this.photos = photos;
	}

	/**
	 * @param photo
	 *            the photo to add
	 */
	public void addPhoto(NewsItemMedia photo) {
		this.photos.add(photo);
	}

	/**
	 * @return the videos
	 */
	public ArrayList<NewsItemMedia> getVideos() {
		return videos;
	}

	/**
	 * @param videos
	 *            the videos to set
	 */
	public void setVideos(ArrayList<NewsItemMedia> videos) {
		this.videos = videos;
	}

	/**
	 * @param video
	 *            the video to add
	 */
	public void addVideo(NewsItemMedia video) {
		this.videos.add(video);
	}

	public NewsItemMedia getThumbnailPhoto() {
		for (NewsItemMedia photo : photos) {
			if (photo.getCategory().equalsIgnoreCase("thumbnail"))
				return photo;
		}

		return null;
	}

	public NewsItemMedia getNewsPhoto() {
		for (NewsItemMedia photo : photos) {
			if (photo.getCategory().equalsIgnoreCase("normal"))
				return photo;
		}

		return null;
	}

	public NewsItemMedia getBigPhoto() {
		for (NewsItemMedia photo : photos) {
			if (photo.getCategory().equalsIgnoreCase("big"))
				return photo;
		}

		return null;
	}

	public NewsItemMedia getVideoPhoto() {
		for (NewsItemMedia video : videos) {
			return video;
		}

		return null;
	}

	public int getJumpToWeb() {
		return jumpToWeb;
	}

	public void setJumpToWeb(int jumpToWeb) {
		this.jumpToWeb = jumpToWeb;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getTrackKey() {
		return trackKey;
	}

	public void setTrackKey(String trackKey) {
		this.trackKey = trackKey;
	}

	public String getNativeMiniLogoUrl() {
		return nativeMiniLogoUrl;
	}

	public void setNativeMiniLogoUrl(String nativeMiniLogoUrl) {
		this.nativeMiniLogoUrl = nativeMiniLogoUrl;
	}

	public boolean isNativeAds() {
		return isNativeAds;
	}

	public void setNativeAds(boolean isNativeAds) {
		this.isNativeAds = isNativeAds;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getTypology() {
		return typology;
	}

	public void setTypology(String typology) {
		this.typology = typology;
	}
}
