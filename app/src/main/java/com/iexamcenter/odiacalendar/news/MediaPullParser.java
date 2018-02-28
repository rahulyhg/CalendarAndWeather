package com.iexamcenter.odiacalendar.news;

/**
 * Created by sasikanta on 2/6/2017.
 */


import android.content.ContentValues;

import com.iexamcenter.odiacalendar.sqlite.SqliteHelper;
import com.iexamcenter.odiacalendar.utility.Constant;

import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

/**
 * RSSPullParser reads an RSS feed from the Picasa featured pictures site. It uses
 * several packages from the widely-known XMLPull API.
 */
public class MediaPullParser extends DefaultHandler {
    // Global constants
    // An attribute value indicating that the element contains media content
    private static final String CONTENT = "media:content";

    // An attribute value indicating that the element contains a thumbnail
    private static final String THUMBNAIL = "media:thumbnail";

    // An attribute value indicating that the element contains an item
    private static final String ITEM = "entry";
    // Sets the initial size of the vector that stores data.
    private static final int VECTOR_INITIAL_SIZE = 500;
    // Storage for a single ContentValues for image data
    private static ContentValues mImage;
    private static String[] mRss;
    // A vector that will contain all of the images
    private Vector<ContentValues> mItemData;

    /**
     * A getter that returns the image data Vector
     *
     * @return A Vector containing all of the image data retrieved by the parser
     */
    public Vector<ContentValues> getItemData() {
        return mItemData;
    }

    /**
     * This method parses XML in an input stream and stores parts of the data in memory
     *
     * @param inputStream      a stream of data containing XML elements, usually a RSS feed
     * @param progressNotifier a helper class for sending status and logs
     * @throws XmlPullParserException defined by XMLPullParser; thrown if the thread is cancelled.
     * @throws IOException            thrown if an IO error occurs during parsing
     */
    public void parseXml(String[] rssLink, InputStream inputStream,
                         BroadcastNotifier progressNotifier) {
        // Instantiates a parser factory
        try {


            mRss = rssLink;
            XmlPullParserFactory localXmlPullParserFactory = XmlPullParserFactory
                    .newInstance();

            // Turns off namespace handling for the XML input
            localXmlPullParserFactory.setNamespaceAware(false);
            // Instantiates a new pull parser
            XmlPullParser localXmlPullParser = localXmlPullParserFactory
                    .newPullParser();
            // Sets the parser's input stream
            localXmlPullParser.setInput(inputStream, null);

            // Gets the first event in the input sream
            int eventType = localXmlPullParser.getEventType();
            // Sets the number of images read to 1
            int imageCount = 1;
            // Returns if the current event (state) is not START_DOCUMENT
            if (eventType != XmlPullParser.START_DOCUMENT) {
                throw new XmlPullParserException("Invalid RSS");
            }
            // Creates a new store for image URL data
            mItemData = new Vector<ContentValues>(VECTOR_INITIAL_SIZE);
            // Loops indefinitely. The exit occurs if there are no more URLs to process
            boolean insideItem = false;
            int dataCnt = 0;
            String title, pubDate, link, image, video_id;
            video_id = title = pubDate = link = image = "";
            while (true) {

                // Gets the next event in the input stream
                int nextEvent = localXmlPullParser.next();
                // If the current thread is interrupted, throws an exception and returns
                if (Thread.currentThread().isInterrupted()) {
                    throw new XmlPullParserException("Cancelled");
                    // At the end of the feed, exits the loop
                } else if (nextEvent == XmlPullParser.END_DOCUMENT) {
                    break;
                    // At the beginning of the feed, skips the event and continues
                } else if (nextEvent == XmlPullParser.START_DOCUMENT) {
                    continue;
                    // At the start of a tag, gets the tag's name
                } else if (nextEvent == XmlPullParser.START_TAG) {
                    String eventName = localXmlPullParser.getName();
                /*
                 * If this is the start of an individual item, logs it and creates a new
                 * ContentValues
                 */
                    System.out.println("eventName::" + eventName);

                    if (eventName.equalsIgnoreCase(ITEM)) {
                        mImage = new ContentValues();
                        insideItem = true;
                        dataCnt = 0;

                        // If this isn't an item, then checks for other options
                    } else if (eventName.equalsIgnoreCase("title") && insideItem) {
                        title = localXmlPullParser.nextText();
                        ++dataCnt;


                        // If this isn't an item, then checks for other options
                    } else if (eventName.equalsIgnoreCase("published") && insideItem) {
                        ++dataCnt;
                        pubDate = localXmlPullParser.nextText();
                        // If this isn't an item, then checks for other options
                    } else if (eventName.equalsIgnoreCase("link") && insideItem) {
                        ++dataCnt;
                        link = localXmlPullParser.getAttributeValue(null, "href");

                        // link = localXmlPullParser.nextText();


                        // If this isn't an item, then checks for other options
                    } else if (eventName.equalsIgnoreCase("yt:videoId") && insideItem) {
                        ++dataCnt;
                        video_id = localXmlPullParser.nextText();

                        // If this isn't an item, then checks for other options
                    } else if (insideItem) {
                        // Defines keys to store the column names
                        String imageUrlKey;
                        String imageNameKey;

                        // Defines a place to store the filename of a URL,
                        String fileName;
                        // If it's CONTENT
                   /* if (eventName.equalsIgnoreCase(CONTENT)) {
                        // Stores the image URL and image name column names as keys
                        imageUrlKey = "xxx";//DataProviderContract.IMAGE_URL_COLUMN;
                        imageNameKey ="xxxx";// DataProviderContract.IMAGE_PICTURENAME_COLUMN;
                        // If it's a THUMBNAIL
                    } else*/
                        if (eventName.equalsIgnoreCase(THUMBNAIL)) {
                            ++dataCnt;
                            image = localXmlPullParser.getAttributeValue(null, "url");


                        } else {
                            continue;
                        }
                    }
                    System.out.println(":TITLEIMAGELINK:" + eventName);

                }
            /*
             * If it's not an ITEM, and it is an END_TAG, and the current event is an ITEM, and
             * there is data in the current ContentValues
             */
                else if ((nextEvent == XmlPullParser.END_TAG)
                        && (localXmlPullParser.getName().equalsIgnoreCase(ITEM))
                        && (mImage != null)) {
                    // Adds the current ContentValues to the ContentValues storage
                    if (insideItem && dataCnt >= 2) {

                        //URL myUrl = null;
                        //try {
                        //  myUrl = new URL(mRss[2]);


                        //String referrer = myUrl.getProtocol() + "://" + myUrl.getHost();

                        // String story = getStory(link.trim(), referrer, mRss[3], mRss[4]);
                        //System.out.println("::story::"+story);
                        long pubTime = getTimeStamp(pubDate.trim());
                        mImage.put(SqliteHelper.NewsEntry.KEY_NEWS_TITLE, title.trim());
                        mImage.put(SqliteHelper.NewsEntry.KEY_NEWS_CAT, mRss[1].trim());
                        mImage.put(SqliteHelper.NewsEntry.KEY_NEWS_TYPE, mRss[3].trim());
                        mImage.put(SqliteHelper.NewsEntry.KEY_NEWS_CHANNEL, mRss[0].trim());
                        mImage.put(SqliteHelper.NewsEntry.KEY_NEWS_PUB_DATE, pubDate.trim());
                        mImage.put(SqliteHelper.NewsEntry.KEY_NEWS_PUB_TIMESTAMP, pubTime);

                        // mImage.put(SqliteHelper.NewsEntry.KEY_NEWS_PUB_TIMESTAMP, "" + System.currentTimeMillis() / 1000);
                        mImage.put(SqliteHelper.NewsEntry.KEY_NEWS_LINK, link.trim());
                        mImage.put(SqliteHelper.NewsEntry.KEY_NEWS_IMAGE, image.trim());
                        mImage.put(SqliteHelper.NewsEntry.KEY_NEWS_LANG, mRss[4].trim());

                        mImage.put(SqliteHelper.NewsEntry.KEY_NEWS_VIDEO_ID, video_id.trim());

                        System.out.println(":TITLEIMAGELINK:" + title + "::" + image + "::" + link);
                        title = pubDate = link = image = "";
                        if (pubTime < 1L)
                            break;
                        mItemData.add(mImage);


                        //} catch (MalformedURLException e) {
                        //  e.printStackTrace();
                        // }
                    }
                    // Logs progress
                    //DataProviderContract.IMAGE_URL_COLUMN
                    //    progressNotifier.notifyProgress("Parsed Image[" + imageCount + "]:"
                    //          + mImage.getAsString("xxxxxx"));
                    // Clears out the current ContentValues
                    mImage = null;
                    // Increments the count of the number of images stored.
                    imageCount++;

                }
            }

            if (inputStream != null)
                inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {

        }
    }


    public long getTimeStamp(String str_date) {

        Date date = null;
        try {
           /* SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+'HH:mm", Locale.getDefault());
            date = sdf.parse(str_date);
            java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
            Log.e("Timestamp",str_date+":str_dateTimestamp:"+date.getTime()/1000);
            return timeStampDate.getTime() / 1000;
*/
            long currTime = System.currentTimeMillis() / 1000;

            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            date = (Date) formatter.parse(str_date);
            long pubTime = date.getTime() / 1000;
            if (Math.abs(currTime - pubTime) < 60 * 60 * 24 * Constant.DAY_KEEP) {
                return pubTime;
            } else
                return 0L;

        } catch (Exception e0) {
            return System.currentTimeMillis() / 1000;
        }

    }
}
