package com.diarioas.guiamundial.dao.reader.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.diarioas.guiamundial.dao.model.news.PhotoMediaItem;

public class ParseXMLGallery {

	public ArrayList<PhotoMediaItem> parsePlistDetailGallery(String source) {
		InputStream in = null;
		try {
			in = new ByteArrayInputStream(source.getBytes());
			XmlPullParser parser;

			parser = XmlPullParserFactory.newInstance().newPullParser();

			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
			parser.nextTag();
			return readFeed(parser);
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return null;
	}

	private static final String ns = null;

	private ArrayList<PhotoMediaItem> readFeed(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		ArrayList<PhotoMediaItem> photos = new ArrayList<PhotoMediaItem>();

		parser.require(XmlPullParser.START_TAG, ns, "rss");
		parser.nextTag();
		parser.require(XmlPullParser.START_TAG, ns, "channel");
		while (parser.nextTag() != XmlPullParser.END_TAG) {
			if (parser.getName().equalsIgnoreCase("item")) {
				photos.add(readItem(parser));
			} else {
				skip(parser);
			}
		}
		return photos;
	}

	private PhotoMediaItem readItem(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		PhotoMediaItem photo = null;
		String caption = null;
		while (parser.nextTag() != XmlPullParser.END_TAG) {
			if (parser.getName().equalsIgnoreCase("media:thumbnail")) {
				photo = readPhoto(parser);
			} else if (parser.getName().equalsIgnoreCase("title")) {
				caption = readText(parser);
			} else {
				skip(parser);
			}
		}
		if (photo != null && caption != null && !caption.equalsIgnoreCase(""))
			photo.setCaption(caption);
		return photo;
	}

	private PhotoMediaItem readPhoto(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		PhotoMediaItem photo = new PhotoMediaItem();
		for (int i = 0; i < parser.getAttributeCount(); i++) {
			if (parser.getAttributeName(i).equalsIgnoreCase("url")) {
				photo.setUrl(parser.getAttributeValue(i));
			} else if (parser.getAttributeName(i).equalsIgnoreCase("width")) {
				photo.setWidth(Integer.valueOf(parser.getAttributeValue(i)));
			} else if (parser.getAttributeName(i).equalsIgnoreCase("height")) {
				photo.setHeight(Integer.valueOf(parser.getAttributeValue(i)));
			}
		}
		parser.next();
		return photo;
	}

	private String readText(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}
		return result;
	}

	private void skip(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}
		int depth = 1;
		while (depth != 0) {
			switch (parser.next()) {
			case XmlPullParser.END_TAG:
				depth--;
				break;
			case XmlPullParser.START_TAG:
				depth++;
				break;
			}
		}
	}
}
