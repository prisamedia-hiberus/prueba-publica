package com.diarioas.guialigas.dao.model.news;

import java.util.ArrayList;


public class SectionNewsTagSections extends SectionNews{

	private int numItems;
	private ArrayList<NewsItemTag> items;

	public SectionNewsTagSections() {
		items = new ArrayList<NewsItemTag>();
	}

	/**
	 * @return the numItems
	 */
	public int getNumItems() {
		return numItems;
	}

	/**
	 * @param numItems the numItems to set
	 */
	public void setNumItems(int numItems) {
		this.numItems = numItems;
	}

	/**
	 * @return the items
	 */
	public ArrayList<NewsItemTag> getItems() {
		return items;
	}

	public NewsItemTag getItem(int pos) {
		return items.get(pos);
	}
	/**
	 * @param items the items to set
	 */
	public void setItems(ArrayList<NewsItemTag> items) {
		this.items = items;
	}

	public void addItem(NewsItemTag item) {
		this.items.add(item);
		
	}
}
