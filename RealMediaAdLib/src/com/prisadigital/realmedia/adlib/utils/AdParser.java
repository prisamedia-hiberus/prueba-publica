package com.prisadigital.realmedia.adlib.utils;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.SocketTimeoutException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.util.Xml;

import com.prisadigital.realmedia.adlib.data.AdFormat;
import com.prisadigital.realmedia.adlib.data.AdUnit;

public class AdParser {

    @SuppressWarnings("unused")
    private static final String TAG = "AdParser";

    public static String cleanXml(String xml) {
        // Set Tag
        String END_TAG = "</adXML>";

        // Search Final Tag
        int index = xml.indexOf(END_TAG);

        // Check if found
        if (index != -1) {
            return xml.substring(0, index + END_TAG.length());
        }

        return xml;
    }

    public static String readXmlFromServer(InputStream is) throws IOException {
        ByteArrayOutputStream baos;
        byte buffer[];
        int nBytes;

        // Initialize Stream
        baos = new ByteArrayOutputStream();
        // Create Buffer
        buffer = new byte[512];

        try {
            while ((nBytes = is.read(buffer)) != -1) {
                baos.write(buffer, 0, nBytes);
            }
        } catch (SocketTimeoutException ste) {
            
        } catch(EOFException te) {
            
        }

        // Get Final Output
        String fullXml = new String(baos.toByteArray(), "UTF-8");

        // Close Stream
        baos.close();

        // Return XML
        return fullXml;
    }
    
    public static AdFormat parseXml(Context context, String s, AdUnit adUnit)
            throws IOException, UnableToParseAdException {
        return AdParser.parseXml(context, s, adUnit, false, 0);
    }    

    // Parse XML retreived from Real Media
    public static AdFormat parseXml(Context context, String s, AdUnit adUnit, boolean isRedirect, int positionRedirect)
            throws IOException, UnableToParseAdException {
        AdFormat adFormat = null;
        boolean done;
        int eventType;
        boolean inAdFormat;
        int screenWidth;
        int distanceToWidth = Integer.MAX_VALUE;

        try {
            if (s != null) {
                // Create Parser
                XmlPullParser parser = Xml.newPullParser();
                // Set Input
                parser.setInput(new StringReader(s));

                // Initialize
                done = false;
                inAdFormat = false;
                screenWidth = context.getResources().getDisplayMetrics().widthPixels;
                // Get First Type
                eventType = parser.getEventType();

                // Iterate
                while (eventType != XmlPullParser.END_DOCUMENT && !done) {
                    // Get Name
                    String name = parser.getName();

                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            if (name.equalsIgnoreCase("adFormat_android")) {
                                // Set flags
                                inAdFormat = true;
                                distanceToWidth = Integer.MAX_VALUE;

                                if (!isRedirect) {
                                    // Create Object
                                    adFormat = new AdFormat();
                                } else {
                                    // Update previous adFormat Object
                                    adFormat = adUnit.getAdFormat(positionRedirect);
                                }

                                // Get Attr. Medio and store it
                                String medio = AdParser.getAttributeValue(parser, "medio");
                                adFormat.setMedio(medio);
                            } else if (inAdFormat && name.equalsIgnoreCase("type")) {
                                // Get Next text and store it
                                String effect = AdParser.getAttributeValue(parser, "efecto");
                                String type = parser.nextText();

                                // Check Type
                                if (type.equalsIgnoreCase("image"))
                                    adFormat.setType(AdFormat.TYPE_IMAGE);
                                else if (type.equalsIgnoreCase("gif"))
                                    adFormat.setType(AdFormat.TYPE_GIF);
                                else if (type.equalsIgnoreCase("video"))
                                    adFormat.setType(AdFormat.TYPE_VIDEO);
                                else if (type.equalsIgnoreCase("html5"))
                                    adFormat.setType(AdFormat.TYPE_HTML5);
                                else if (type.equalsIgnoreCase("preroll"))
                                    adFormat.setType(AdFormat.TYPE_PREROLL);
                                else if (type.equalsIgnoreCase("xml"))
                                    adFormat.setType(AdFormat.TYPE_XML);

                                // Initialize Effect
                                adFormat.setEffect(AdFormat.EFFECT_NONE);

                                // Check Effect
                                if (effect.equalsIgnoreCase("1"))
                                    adFormat.setEffect(AdFormat.EFFECT_NONE);
                                else if (effect.equalsIgnoreCase("2"))
                                    adFormat.setEffect(AdFormat.EFFECT_EXPAND_CLICK);
                                else if (effect.equalsIgnoreCase("3a"))
                                    adFormat.setEffect(AdFormat.EFFECT_EXPAND_DRAG_DOWN);
                                else if (effect.equalsIgnoreCase("3b"))
                                    adFormat.setEffect(AdFormat.EFFECT_EXPAND_DRAG_UP);
                                else if (effect.equalsIgnoreCase("4"))
                                    adFormat.setEffect(AdFormat.EFFECT_FULLSCREEN);
                                else if (effect.equalsIgnoreCase("5"))
                                    adFormat.setEffect(AdFormat.EFFECT_FULLSCREEN_FROM_TOP);

                            } else if (inAdFormat && name.equalsIgnoreCase("adUrl")) {
                                // Get Size
                                String width = AdParser.getAttributeValue(parser, "width");
                                String height = AdParser.getAttributeValue(parser, "height");

                                // FIXME Luis: he movido esto fuera. Para que siempre se setee la
                                // nueva url. No veo el caso en el que no haga falta
                                // Get Text and store it
                                String url = parser.nextText();
                                adFormat.setUrl(url);
                                try {
                                    if (!isRedirect) {
                                        if (!width.equals("") && !height.equals("")) {
                                            // Cast Strings
                                            int iWidth = Integer.parseInt(width);
                                            int iHeight = Integer.parseInt(height);
                                            int distScreen = Math.abs(iWidth - screenWidth);
    
                                            // We need to get only the size more close to the width of
                                            // the
                                            // screen
                                            if (distScreen < distanceToWidth) {
                                                // Update minDistance
                                                distanceToWidth = distScreen;
    
                                                // Store the new size
                                                adFormat.setWidth(iWidth);
                                                adFormat.setHeight(iHeight);
    
                                            }
                                        } else {
                                            // Cast Strings
                                            int iWidth = Integer.parseInt(width);
                                            int iHeight = Integer.parseInt(height);
                                            // Store the new size
                                            adFormat.setWidth(iWidth);
                                            adFormat.setHeight(iHeight);
                                        }
                                    }
                                } catch (NumberFormatException nfe) {
                                }

                            } else if (inAdFormat && name.equalsIgnoreCase("adPixel")) {
                                // Get URL
                                String url = parser.nextText();
                                // Add to Format
                                adFormat.addPixel(url);
                            } else if (inAdFormat && name.equalsIgnoreCase("adClick")) {
                                // Get URL
                                String url = parser.nextText();
                                // Add to Format
                                adFormat.setClickUrl(url);
                            } else if (inAdFormat && name.equalsIgnoreCase("adDuration")) {
                                // AdDuration only when it is not a redirect or if it is a redirect and a video
                                if (!isRedirect || (isRedirect && adFormat.getType() == AdFormat.TYPE_VIDEO)) {
                                    // Get duration
                                    String duration = parser.nextText();
    
                                    try {
                                        // Cast
                                        int iDuration = Integer.parseInt(duration);
                                        // Add to Format
                                        adFormat.setDuration(iDuration);
                                    } catch (NumberFormatException nfe) {
                                        // Default Duration
                                        adFormat.setDuration(0);
                                    }
                                }
                            } else if (inAdFormat && name.equalsIgnoreCase("autoMute")
                                    && !isRedirect) {
                                // Get Bool
                                String bool = parser.nextText();
                                // Add to Format
                                adFormat.setAutomute(Boolean.parseBoolean(bool));
                            } else if (inAdFormat && name.equalsIgnoreCase("autoplay")
                                    && !isRedirect) {
                                // Get Bool
                                String bool = parser.nextText();
                                // Add to Format
                                adFormat.setAutoplay(Boolean.parseBoolean(bool));
                            } else if (inAdFormat && name.equalsIgnoreCase("OpenInNavigator")
                                    && !isRedirect) {
                                // Get Bool
                                String bool = parser.nextText();
                                // Add to Format
                                adFormat.setOpenInNavigator(Boolean.parseBoolean(bool));
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            if (name.equalsIgnoreCase("adXML")) {
                                done = true;
                            } else if (name.equalsIgnoreCase("adFormat_android")) {
                                // Set flag
                                inAdFormat = false;

                                if (!isRedirect) {
                                    // Save AdFormat
                                    switch (adFormat.getEffect()) {
                                        case AdFormat.EFFECT_EXPAND_CLICK:
                                        case AdFormat.EFFECT_EXPAND_DRAG_DOWN:
                                        case AdFormat.EFFECT_EXPAND_DRAG_UP:
                                            adUnit.setAdFormat(adFormat, AdUnit.AD_FORMAT_BASIC);
                                            break;
                                        case AdFormat.EFFECT_FULLSCREEN:
                                        case AdFormat.EFFECT_FULLSCREEN_FROM_TOP:
                                        case AdFormat.EFFECT_NONE:
                                            if (adUnit.isAdFormat(AdUnit.AD_FORMAT_BASIC)) {
                                                adUnit.setAdFormat(adFormat,
                                                        AdUnit.AD_FORMAT_EXPANDED);
                                            } else {
                                                adUnit.setAdFormat(adFormat, AdUnit.AD_FORMAT_BASIC);
                                            }
                                            break;
                                    }
                                }
                            }
                            break;
                    }

                    // Next Event
                    eventType = parser.next();
                }
            }
        } catch (XmlPullParserException xppe) {
            // Launch our exception
            throw new UnableToParseAdException(xppe.toString());
        }

        return adFormat;
    }

    // Get the value of an attribute from the actual parser stream
    private static String getAttributeValue(XmlPullParser parser, String attrName) {
        // Get Attribute
        int attrCount = parser.getAttributeCount();

        // Check if there is one or more attributes
        if (attrCount > 0) {
            // Search Attribute
            for (int i = 0; i < attrCount; i++) {
                // Get Attr. Name
                String name = parser.getAttributeName(i);

                // Check name
                if (name.equalsIgnoreCase(attrName))
                    return parser.getAttributeValue(i);
            }
        }

        return "";
    }

    // Parser Exception
    public static class UnableToParseAdException extends Exception {
        private static final long serialVersionUID = 240757360233547586L;

        public UnableToParseAdException(String s) {
            super(s);
        }
    }
}
