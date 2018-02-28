package com.iexamcenter.odiacalendar.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class PrefManager {
    private static final String PROPERTY_APP_WHATISTODAY = "PROPERTY_APP_what";

    private static final String PROPERTY_APP_WIDGET_TEMP = "PROPERTY_APP_WIDGET_TEMP";
    private static final String PROPERTY_APP_WIDGET_TEMP_IMG = "PROPERTY_APP_WIDGET_TEMP_IMG";
    private static final String PROPERTY_APP_WIDGET_TEMP_TIME = "PROPERTY_APP_WIDGET_TEMP_TIME";

    private static final String PROPERTY_APP_HOROSCOPE = "PROPERTY_APP_HOROSCOPE";

    private static final String PROPERTY_APP_UPDATES_VERSION = "PROPERTY_APP_UPDATES_VERSION";
    private static final String PROPERTY_APP_UPDATES_TYPE = "PROPERTY_APP_UPDATES_TYPE";
    private static final String PROPERTY_APP_UPDATES_DESC = "PROPERTY_APP_UPDATES_DESC";
    private static final String PROPERTY_APP_UPDATES_TIME = "PROPERTY_APP_UPDATES_LASTTIME";

    private static final String PROPERTY_PUBLIC_KEY_TOKEN = "PUBLIC_KEY_TOKEN";
    private static final String PROPERTY_MY_LANGUAGE = "MY_LANGUAGE";
    private static final String PROPERTY_IS_REMOVED_ADS = "IS_REMOVED_ADS";
    private static final String PROPERTY_API_URL = "API_URL";
    private static final String PROPERTY_WHETHER_CURRENT_CITY = "WHETHER_CURRENT_CITY";

    private static final String PROPERTY_WHETHER_TOKEN = "WHETHER_TOKEN";
    private static final String PROPERTY_PUBLICATION = "PUBLICATION";

    private static final String PROPERTY_THEME = "THEME";
    private static final String PROPERTY_PULL_IMAGE= "PULL_IMAGE";
    private static final String PROPERTY_NEWSUPDATES = "NEWSUPDATES";
    private static final String PROPERTY_REPORTING1 = "REPORTING1";
    private static final String PROPERTY_REPORTING2 = "REPORTING2";
    private static final String PROPERTY_REPORTING3 = "REPORTING3";
    private static final String PROPERTY_REPORTING4 = "REPORTING4";

    private static final String PROPERTY_FIRST_USE = "FIRST_USE";

    private static final String PROPERTY_COUNTRY = "COUNTRY";
    private static final String PROPERTY_COUNTRY_CODE = "COUNTRY_CODE";
    private static final String PROPERTY_SIM_COUNTRY = "SIM_COUNTRY";

    private static final String PROPERTY_LATITUDE = "LATITUDE";
    private static final String PROPERTY_LONGITUDE = "LONGITUDE";
    private static final String PROPERTY_AREA_ADMIN = "AREA_ADMIN";
    private static final String PROPERTY_AREA_SUBADMIN = "AREA_SUBADMIN";

    private static final String PROPERTY_SERVER_API_VERSION = "SERVER_API_VERSION";
    private static final String PROPERTY_SERVER_ID = "SERVER_ID";

    private static final String PROPERTY_FCM_TOKEN = "FCM_TOKEN";

    private static  final String PROPERTY_MY_HOME_PAGE="MY_HOME_PAGE";

    private Context mContext;
    private SharedPreferences pref;
    private Editor editor;
    private int myHomePage,settingReporting1, settingReporting2, settingReporting3, settingReporting4;
    private boolean isTheme,isPullImg,isNewsUpdates,isRemovedAds, isWeatherCurrCity, firstUse;
    private String horoscope, publication, myLanguage, serverId, latitude, longitude, areaAdmin, areaSubadmin, country, countryCode, simCountry;
    private String widgetTemp,widgetTempImg,widgetTempTime,publicKeyToken, whetherToken, apiUrl,whattoday;
    private static PrefManager instance;
    private int appUpdatesVersionCode, appUpdatesType, serverApiVersion;
    private long appUpdateslastTime;
    private String fcmToken,appUpdatesDesc;


    private PrefManager(Context ctx) {
        mContext = ctx;
        pref = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
        editor = pref.edit();
    }

    public static PrefManager getInstance(Context ctx) {

        if (instance == null)
            instance = new PrefManager(ctx);

        return instance;
    }


    public void load() {
        fcmToken = pref.getString(PROPERTY_FCM_TOKEN, "");
        appUpdatesVersionCode = pref.getInt(PROPERTY_APP_UPDATES_VERSION, 0);
        appUpdatesType = pref.getInt(PROPERTY_APP_UPDATES_TYPE, 0);
        appUpdateslastTime = pref.getLong(PROPERTY_APP_UPDATES_TIME, 1l);
        appUpdatesDesc = pref.getString(PROPERTY_APP_UPDATES_DESC, "");

        widgetTemp = pref.getString(PROPERTY_APP_WIDGET_TEMP, "");
        widgetTempImg = pref.getString(PROPERTY_APP_WIDGET_TEMP_IMG, "");
        widgetTempTime = pref.getString(PROPERTY_APP_WIDGET_TEMP_TIME, "");

        horoscope = pref.getString(PROPERTY_APP_HOROSCOPE, "");
        whattoday = pref.getString(PROPERTY_APP_WHATISTODAY, "");
        myHomePage =pref.getInt(PROPERTY_MY_HOME_PAGE, 1);
        publication = pref.getString(PROPERTY_PUBLICATION, "");
        publicKeyToken = pref.getString(PROPERTY_PUBLIC_KEY_TOKEN, "");
        myLanguage = pref.getString(PROPERTY_MY_LANGUAGE, "en");
        apiUrl = pref.getString(PROPERTY_API_URL, Constant.URL_API);
        whetherToken = pref.getString(PROPERTY_WHETHER_TOKEN, Constant.WHETHER_TOKEN);
        serverId = pref.getString(PROPERTY_SERVER_ID, "");
        isRemovedAds = pref.getBoolean(PROPERTY_IS_REMOVED_ADS, false);


        isTheme = pref.getBoolean(PROPERTY_THEME, true);
        isPullImg= pref.getBoolean(PROPERTY_PULL_IMAGE, true);
        isNewsUpdates = pref.getBoolean(PROPERTY_NEWSUPDATES, true);

        isWeatherCurrCity = pref.getBoolean(PROPERTY_WHETHER_CURRENT_CITY, true);

        settingReporting1 = pref.getInt(PROPERTY_REPORTING1, 8 * 60);
        settingReporting2 = pref.getInt(PROPERTY_REPORTING2, 0);
        settingReporting3 = pref.getInt(PROPERTY_REPORTING3, 0);
        settingReporting4 = pref.getInt(PROPERTY_REPORTING4, 0);
        firstUse = pref.getBoolean(PROPERTY_FIRST_USE, true);

        country = pref.getString(PROPERTY_COUNTRY, "INDIA");
        countryCode = pref.getString(PROPERTY_COUNTRY_CODE, "IN");
        simCountry = pref.getString(PROPERTY_SIM_COUNTRY, "in");


        latitude = pref.getString(PROPERTY_LATITUDE, null);
        longitude = pref.getString(PROPERTY_LONGITUDE, null);
        areaAdmin = pref.getString(PROPERTY_AREA_ADMIN, null);
        areaSubadmin = pref.getString(PROPERTY_AREA_SUBADMIN, null);

        serverApiVersion = pref.getInt(PROPERTY_SERVER_API_VERSION, -1);
    }


    public String getWidgetTempImg() {
        return widgetTempImg;
    }

    public void setWidgetTempImg(String widgetTempImg) {
        this.widgetTempImg = widgetTempImg;
        editor.putString(PROPERTY_APP_WIDGET_TEMP_IMG, widgetTempImg);
        editor.commit();
    }
    public String getWidgetTempTime() {
        return widgetTempTime;
    }

    public void setWidgetTempTime(String widgetTempTime) {
        this.widgetTempTime = widgetTempTime;
        editor.putString(PROPERTY_APP_WIDGET_TEMP_TIME, widgetTempTime);
        editor.commit();
    }

    public String getWidgetTemp() {
        return widgetTemp;
    }

    public void setWidgetTemp(String widgetTemp) {
        this.widgetTemp = widgetTemp;
        editor.putString(PROPERTY_APP_WIDGET_TEMP, widgetTemp);
        editor.commit();
    }



    public String getWhattoday() {
        return whattoday;
    }

    public void setWhattoday(String whattoday) {
        this.whattoday = whattoday;
        editor.putString(PROPERTY_APP_WHATISTODAY, whattoday);
        editor.commit();
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
        editor.putString(PROPERTY_FCM_TOKEN, fcmToken);
        editor.commit();
    }
    public String getHoroscope() {
        return horoscope;
    }

    public void setHoroscope(String horoscope) {
        this.horoscope = horoscope;
        editor.putString(PROPERTY_APP_HOROSCOPE, horoscope);
        editor.commit();
    }

    public String getAppUpdatesDesc() {
        return appUpdatesDesc;
    }

    public void setAppUpdatesDesc(String appUpdatesDesc) {
        this.appUpdatesDesc = appUpdatesDesc;
        editor.putString(PROPERTY_APP_UPDATES_DESC, appUpdatesDesc);
        editor.commit();
    }

    public long getAppUpdateslastTime() {
        return appUpdateslastTime;
    }

    public void setAppUpdateslastTime(long appUpdateslastTime) {
        this.appUpdateslastTime = appUpdateslastTime;
        editor.putLong(PROPERTY_APP_UPDATES_TIME, appUpdateslastTime);
        editor.commit();
    }

    public int getAppUpdatesType() {
        return appUpdatesType;
    }

    public void setAppUpdatesType(int appUpdatesType) {
        this.appUpdatesType = appUpdatesType;
        editor.putInt(PROPERTY_APP_UPDATES_TYPE, appUpdatesType);
        editor.commit();
    }

    public int getAppUpdatesVersionCode() {
        return appUpdatesVersionCode;
    }

    public void setAppUpdatesVersionCode(int appUpdatesVersionCode) {
        this.appUpdatesVersionCode = appUpdatesVersionCode;
        editor.putInt(PROPERTY_APP_UPDATES_VERSION, appUpdatesVersionCode);
        editor.commit();
    }

    public String getPublication() {
        return publication;
    }

    public void setPublication(String pub) {
        this.publication = pub;
        editor.putString(PROPERTY_PUBLICATION, pub);
        editor.commit();
    }


    public int getMyHomePage() {
        return myHomePage;
    }

    public void setMyHome(int  index) {
        this.myHomePage = index;
        editor.putInt(PROPERTY_MY_HOME_PAGE, index);
        editor.commit();
    }

    public String getMyLanguage() {
        return myLanguage;
    }

    public void setMyLanguage(String lang) {
        this.myLanguage = lang;
        editor.putString(PROPERTY_MY_LANGUAGE, lang);
        editor.commit();
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
        editor.putString(PROPERTY_SERVER_ID, serverId);
        editor.commit();
    }

    public boolean isRemovedAds() {
        return isRemovedAds;
    }

    public void setRemovedAds(boolean removedAds) {
        isRemovedAds = removedAds;
        editor.putBoolean(PROPERTY_IS_REMOVED_ADS, removedAds);
        editor.commit();
    }

    public String getPublicKeyToken() {
        return "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApCXg9lIreNBoW2mBiER71uuXc65Dybo0qmzcAJ5ndrYs77epGy4Or+ngf1JnL/xLsOVnypS08Y0QesHfSBl9TabeTFgKU6XexLcE6UYTB3Q0J/iofpUBVHSjhE9ppI+P0jXxDsJOO5nbHNSrZOgVAfT8qhR/TUdIoSi5LVFQ+t8VibNbEwpw6KrFK7wBQU894mTkOYdpL9m8Ctemkih0ydE9zF/iimygqr237hKFUQMfIQyvPAhGrr4G190WoXWXrF3J5MjBahQPEj4y9YbEhSQB8HK6eS0a5dBSI3UriB1vpBCUni/3tw6d5NFCdvzHJjoXI+JoIanG3Syf69/baQIDAQAB";
    }

    public void setPublicKeyToken(String publicKeyToken) {
        this.publicKeyToken = publicKeyToken;
        editor.putString(PROPERTY_PUBLIC_KEY_TOKEN, publicKeyToken);
        editor.commit();
    }


    public String getApiUrl() {
        return Constant.URL_API;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
        editor.putString(PROPERTY_API_URL, apiUrl);
        editor.commit();
    }

    public String getWhetherToken() {
        return whetherToken;
    }

    public void setWhetherToken(String whetherToken) {
        this.whetherToken = whetherToken;
        editor.putString(PROPERTY_WHETHER_TOKEN, whetherToken);
        editor.commit();
    }

    public boolean isWeatherCurrCity() {
        return isWeatherCurrCity;
    }

    public void isWeatherCurrCity(boolean booll) {
        this.isWeatherCurrCity = booll;
        editor.putBoolean(PROPERTY_WHETHER_CURRENT_CITY, booll);
        editor.commit();
    }


    public int getSettingReporting1() {
        return settingReporting1;
    }

    public void setSettingReporting1(int settingReporting1) {
        this.settingReporting1 = settingReporting1;
        editor.putInt(PROPERTY_REPORTING1, settingReporting1);
        editor.commit();
    }

    public int getSettingReporting2() {
        return settingReporting2;
    }

    public void setSettingReporting2(int settingReporting2) {
        this.settingReporting2 = settingReporting2;
        editor.putInt(PROPERTY_REPORTING2, settingReporting2);
        editor.commit();
    }

    public int getSettingReporting3() {
        return settingReporting3;
    }

    public void setSettingReporting3(int settingReporting3) {
        this.settingReporting3 = settingReporting3;
        editor.putInt(PROPERTY_REPORTING3, settingReporting3);
        editor.commit();
    }

    public int getSettingReporting4() {
        return settingReporting4;
    }

    public void setSettingReporting4(int settingReporting4) {
        this.settingReporting4 = settingReporting4;
        editor.putInt(PROPERTY_REPORTING4, settingReporting4);
        editor.commit();
    }

    public String getSimCountry() {
        return simCountry;
    }


    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
        editor.putString(PROPERTY_COUNTRY_CODE, countryCode);
        editor.commit();
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
        editor.putString(PROPERTY_COUNTRY, country);
        editor.commit();
    }

    public boolean isNewsUpdates() {
        return isNewsUpdates;
    }

    public void setNewsUpdates(boolean isNewsUpdates) {
        this.isNewsUpdates = isNewsUpdates;
        editor.putBoolean(PROPERTY_NEWSUPDATES, isNewsUpdates);
        editor.commit();
    }

    public boolean isTheme() {
        return isTheme;
    }

    public void setTheme(boolean theme) {
        this.isTheme = theme;
        editor.putBoolean(PROPERTY_THEME, isTheme);
        editor.commit();
    }


    public boolean isPullImg() {
        return isPullImg;
    }

    public void setPullImg(boolean isPullImg) {
        this.isPullImg = isPullImg;
        editor.putBoolean(PROPERTY_PULL_IMAGE, isPullImg);
        editor.commit();
    }

    public boolean isFirstUse() {
        return firstUse;
    }

    public void setFirstUse(boolean firstUse) {
        this.firstUse = firstUse;
        editor.putBoolean(PROPERTY_FIRST_USE, firstUse);
        editor.commit();
    }

    public int getServerApiVersion() {
        return serverApiVersion;
    }


    public String getAreaAdmin() {
        return areaAdmin;
    }

    public void setAreaAdmin(String areaAdmin) {
        this.areaAdmin = areaAdmin;
        editor.putString(PROPERTY_AREA_ADMIN, areaAdmin);
        editor.commit();
    }


    public String getAreaSubadmin() {
        return areaSubadmin;
    }

    public void setAreaSubadmin(String areaSubadmin) {
        this.areaSubadmin = areaSubadmin;
        editor.putString(PROPERTY_AREA_SUBADMIN, areaSubadmin);
        editor.commit();
    }

    public String getLatitude() {

        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
        editor.putString(PROPERTY_LATITUDE, latitude);
        editor.commit();
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
        editor.putString(PROPERTY_LONGITUDE, longitude);
        editor.commit();
    }

    public int getNotified(String notif) {
        return pref.getInt(notif, -1);
    }

    public void setNotified(String notif, int val) {
        editor.putInt(notif, val);
        editor.commit();
    }

    public long getNewsUpdateTime(String key) {
        return pref.getLong(key, -1L);
    }

    public void setNewsUpdateTime(String key, long val) {
        editor.putLong(key, val);
        editor.commit();
    }
}
