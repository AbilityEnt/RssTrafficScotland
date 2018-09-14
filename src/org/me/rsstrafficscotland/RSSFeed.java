package org.me.rsstrafficscotland;

import java.io.Serializable;

public class RSSFeed implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;
	private String description;
	private String link;
	private String geopoint;			// Instantiates Global Variables
	private String comments;
	private String pubDate;
	private String author;

	public RSSFeed() {
	}

	public RSSFeed(String title, String description, String link,
			String geopoint, String author, String comments, String pubDate) {
		this.title = title;
		this.link = link;
		this.description = description;
		this.geopoint = geopoint;
		this.author = author;
		this.comments = comments;
		this.pubDate = pubDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {				// these lines of code grab the data from rss feed and parse it through along with set methods
		this.link = link;
	}

	public String getGeopoint() {
		return geopoint;
	}

	public void setGeopoint(String geopoint) {
		this.geopoint = geopoint;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getPubdate() {
		return pubDate;
	}

	public void setPubdate(String pubdate) {
		this.pubDate = pubdate;
	}

}
