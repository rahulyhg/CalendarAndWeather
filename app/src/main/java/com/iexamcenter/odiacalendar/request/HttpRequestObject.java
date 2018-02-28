package com.iexamcenter.odiacalendar.request;

import android.content.Context;

import com.iexamcenter.odiacalendar.utility.Constant;
import com.iexamcenter.odiacalendar.utility.PrefManager;

import org.json.JSONObject;

import java.util.Locale;
import java.util.TimeZone;


public class HttpRequestObject extends BaseRequest {
    PrefManager mPref;
    Context mContext;
    private static HttpRequestObject instance = null;

    public HttpRequestObject(Context ctx) {

        super(ctx);
        mContext = ctx;
        mPref = PrefManager.getInstance(mContext);

    }

    public static HttpRequestObject getInstance(Context ctx) {

        if (instance == null)
            instance = new HttpRequestObject(ctx);
        return instance;
    }

    public JSONObject getRequestBody(Object... params) throws Exception {
        int code = Integer.parseInt(params[0].toString());
        mPref.load();
        JSONObject object = baseJson;
        String inputData;
        JSONObject jsonObj;
        TimeZone timezone = TimeZone.getDefault();
        String timezoneid = timezone.getID();
        int dst = timezone.getDSTSavings();
        object.put("SECURITY", "BCgKCAQEApCXg9lIreNBoW2mBiER71uuXc65Dybo0");
        object.put("locale", Locale.getDefault());
        object.put("timezone", timezoneid);
        object.put("dst", dst);
        object.put("timestamp_milli", System.currentTimeMillis());
        object.put("server_id", "" + mPref.getServerId());
        object.put("fcm_id", "" + mPref.getFcmToken());
        switch (code) {

            case Constant.FEEDBACK_API:
                inputData = params[1].toString();
                object.put("jsonapi", Constant.FEEDBACK_API);
                jsonObj = new JSONObject(inputData);
                object.put("input", jsonObj);
                break;
            case Constant.WHATTODAY_API:
                inputData = params[1].toString();
                object.put("jsonapi", Constant.WHATTODAY_API);
                jsonObj = new JSONObject(inputData);
                object.put("input", jsonObj);
                break;




            case Constant.SETUP_WEATHER_API:
                inputData = params[1].toString();
                object.put("jsonapi", Constant.SETUP_WEATHER_API);
                jsonObj = new JSONObject(inputData);
                object.put("input", jsonObj);
                break;
            case Constant.SETUP_FORCAST_API:
                inputData = params[1].toString();
                object.put("jsonapi", Constant.SETUP_FORCAST_API);
                jsonObj = new JSONObject(inputData);
                object.put("input", jsonObj);
                break;

        }

        return object;
    }

}
