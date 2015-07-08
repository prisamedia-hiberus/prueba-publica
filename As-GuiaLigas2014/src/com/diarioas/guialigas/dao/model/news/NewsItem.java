package com.diarioas.guialigas.dao.model.news;

import java.util.Date;

public class NewsItem {

	private int idCompetition;
	
	private String title;
	private String titleNews;
	private String preTitle;
	private String subTitle;

	private String section;
	private String titleSection;
	private String preTitleSection;

	private String titleFront;
	private String preTitleFront;

	private String urlDetail;
	private String body;
	private String portal;
	private String _abstract;
	private String tags;
	private String typology;
	private String author;
	private String urlComments;
	private Date date;

	private PhotoMediaItem photoThumbnail;
	private PhotoMediaItem photoNormal;
	private PhotoMediaItem photoBig;

	private VideoMediaItem video;

	/**
	 * @return the idCompetition
	 */
	public int getIdCompetition() {
		return idCompetition;
	}

	/**
	 * @param idCompetition the idCompetition to set
	 */
	public void setIdCompetition(int idCompetition) {
		this.idCompetition = idCompetition;
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
	 * @return the titleNews
	 */
	public String getTitleNews() {
		return titleNews;
	}

	/**
	 * @param titleNews
	 *            the titleNews to set
	 */
	public void setTitleNews(String titleNews) {
		this.titleNews = titleNews;
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
	 * @return the subTitle
	 */
	public String getSubTitle() {
		return subTitle;
	}

	/**
	 * @param subTitle
	 *            the subTitle to set
	 */
	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	/**
	 * @return the section
	 */
	public String getSection() {
		return section;
	}

	/**
	 * @param section
	 *            the section to set
	 */
	public void setSection(String section) {
		this.section = section;
	}

	/**
	 * @return the titleSection
	 */
	public String getTitleSection() {
		return titleSection;
	}

	/**
	 * @param titleSection
	 *            the titleSection to set
	 */
	public void setTitleSection(String titleSection) {
		this.titleSection = titleSection;
	}

	/**
	 * @return the preTitleSection
	 */
	public String getPreTitleSection() {
		return preTitleSection;
	}

	/**
	 * @param preTitleSection
	 *            the preTitleSection to set
	 */
	public void setPreTitleSection(String preTitleSection) {
		this.preTitleSection = preTitleSection;
	}

	/**
	 * @return the titleFront
	 */
	public String getTitleFront() {
		return titleFront;
	}

	/**
	 * @param titleFront
	 *            the titleFront to set
	 */
	public void setTitleFront(String titleFront) {
		this.titleFront = titleFront;
	}

	/**
	 * @return the preTitleFront
	 */
	public String getPreTitleFront() {
		return preTitleFront;
	}

	/**
	 * @param preTitleFront
	 *            the preTitleFront to set
	 */
	public void setPreTitleFront(String preTitleFront) {
		this.preTitleFront = preTitleFront;
	}

	/**
	 * @return the urlDetail
	 */
	public String getUrlDetail() {
		return urlDetail;
	}

	/**
	 * @param urlDetail
	 *            the urlDetail to set
	 */
	public void setUrlDetail(String urlDetail) {
		this.urlDetail = urlDetail;
	}

	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body
	 *            the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * @return the portal
	 */
	public String getPortal() {
		return portal;
	}

	/**
	 * @param portal
	 *            the portal to set
	 */
	public void setPortal(String portal) {
		this.portal = portal;
	}

	/**
	 * @return the _abstract
	 */
	public String getAbstract() {
		return _abstract;
	}

	/**
	 * @param _abstract
	 *            the _abstract to set
	 */
	public void setAbstract(String _abstract) {
		this._abstract = _abstract;
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
	 * @return the typology
	 */
	public String getTypology() {
		return typology;
	}

	/**
	 * @param typology
	 *            the typology to set
	 */
	public void setTypology(String typology) {
		this.typology = typology;
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
	 * @return the urlComments
	 */
	public String getUrlComments() {
		return urlComments;
	}

	/**
	 * @param urlComments
	 *            the urlComments to set
	 */
	public void setUrlComments(String urlComments) {
		this.urlComments = urlComments;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the photoThumbnail
	 */
	public PhotoMediaItem getPhotoThumbnail() {
		return photoThumbnail;
	}

	/**
	 * @param photoThumbnail
	 *            the photoThumbnail to set
	 */
	public void setPhotoThumbnail(PhotoMediaItem photoThumbnail) {
		this.photoThumbnail = photoThumbnail;
	}

	/**
	 * @return the photoNormal
	 */
	public PhotoMediaItem getPhotoNormal() {
		return photoNormal;
	}

	/**
	 * @param photoNormal
	 *            the photoNormal to set
	 */
	public void setPhotoNormal(PhotoMediaItem photoNormal) {
		this.photoNormal = photoNormal;
	}

	/**
	 * @return the photoBig
	 */
	public PhotoMediaItem getPhotoBig() {
		return photoBig;
	}

	/**
	 * @param photoBig
	 *            the photoBig to set
	 */
	public void setPhotoBig(PhotoMediaItem photoBig) {
		this.photoBig = photoBig;
	}

	/**
	 * @return the video
	 */
	public VideoMediaItem getVideo() {
		return video;
	}

	/**
	 * @param video
	 *            the video to set
	 */
	public void setVideo(VideoMediaItem video) {
		this.video = video;
	}



}
