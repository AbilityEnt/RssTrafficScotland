package org.me.rsstrafficscotland;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class XmlFeedParser {
	private InputStream urlStream;
	private XmlPullParserFactory factory;	//creates the pull parsing code
	private XmlPullParser parser;

	private List<RSSFeed> rssFeedList; // creates the feed called "rssFeedList"
	private RSSFeed rssFeed;			

	private String urlString;
	private String tagName;

	private String title;	//initialises the title
	private String description;	//initialises the description within the rssfeed
	private String link;	    //initialises the link within the rss feed
	private String geopoint;	//initialises the geopoint which will be called from the rssfeed
	private String author;	//initialises the author from the feed
	private String comments;	//initialises the comments from the feed
	private String pubDate;	//initialises the pubDate from feed

	public static final String ITEM = "item";
	public static final String CHANNEL = "channel";

	public static final String TITLE = "title";
	public static final String DESCRIPTION = "description";
	public static final String LINK = "link";
	public static final String GEOPOINT = "geopoint";
	public static final String AUTHOR = "author";
	public static final String COMMENTS = "comments";
	public static final String PUBLISHEDDATE = "pubDate";

	public XmlFeedParser(String urlString) {
		this.urlString = urlString;
	}

	public static InputStream downloadUrl(String urlString) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setDoInput(true);			
		conn.connect();
		InputStream stream = conn.getInputStream();
		return stream;
	}

	public List<RSSFeed> parse() {
		try {
			@SuppressWarnings("unused")
			int count = 0;
			factory = XmlPullParserFactory.newInstance();
			parser = factory.newPullParser();
			urlStream = downloadUrl(urlString);
			parser.setInput(urlStream, null);					//sets up a pull parser for the rss feed
			int eventType = parser.getEventType();
			boolean done = false;								// if the parser is null
																// then create a new array for the rssfeed to store data
			rssFeed = new RSSFeed();
			rssFeedList = new ArrayList<RSSFeed>();
			while (eventType != XmlPullParser.END_DOCUMENT && !done) {
				tagName = parser.getName();

				switch (eventType) {
					case XmlPullParser.START_DOCUMENT :
						break;
					case XmlPullParser.START_TAG :
						if (tagName.equals(ITEM)) {
							rssFeed = new RSSFeed();
						}
						if (tagName.equals(TITLE)) {
							title = parser.nextText().toString();
						}																	//these lines of code is where the data is processing from the rss feed and parsing to the XML Layout
						if (tagName.equals(DESCRIPTION)) {
							description = parser.nextText().toString();
						}
						if (tagName.equals(LINK)) {
							link = parser.nextText().toString();
						}
						if (tagName.equals(GEOPOINT)) {
							geopoint = parser.nextText().toString();
						}
						if (tagName.equals(AUTHOR)) {
							author = parser.nextText().toString();
						}
						if (tagName.equals(COMMENTS)) {
							comments = parser.nextText().toString();
						}
						if (tagName.equals(PUBLISHEDDATE)) {
							pubDate = parser.nextText().toString();
						}
						break;
					case XmlPullParser.END_TAG :
						if (tagName.equals(CHANNEL)) {
							done = true;
						} else if (tagName.equals(ITEM)) {
							rssFeed = new RSSFeed(title, description, link,
									geopoint, author, comments, pubDate);
							rssFeedList.add(rssFeed);
						}
						break;
				}
				eventType = parser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rssFeedList;
	}
}
