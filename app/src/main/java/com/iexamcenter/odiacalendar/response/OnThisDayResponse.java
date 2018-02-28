package com.iexamcenter.odiacalendar.response;

import android.os.Build;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sasikanta on 5/1/2016.
 */

public class OnThisDayResponse implements Serializable {
    private static final long serialVersionUID = 1L;


    public OnThisDayResponse() {

    }

    @SerializedName("list")
    private ArrayList<WhatToDayList> list;

    public ArrayList<WhatToDayList> getList() {
        return list;
    }

    public class WhatToDayList implements Serializable {
        private static final long serialVersionUID = 7L;

        @SerializedName("id")
        private String id;
        @SerializedName("f_year")
        private String year;
        @SerializedName("f_month")
        private String month;
        @SerializedName("f_day")
        private String day;
        @SerializedName("f_title")
        private String title;
        @SerializedName("f_type")
        private String type;

        @SerializedName("f_source")
        private String wiki;


        public String getWiki() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                return wiki;
            }
            return "";
        }

        public String getId() {
            return id;
        }

        public String getYear() {
            return year;
        }

        public String getMonth() {
            return month;
        }

        public String getDay() {
            return day;
        }

        public String getType() {
            return type;
        }

        public String getTitle() {
            return title;
        }

    }
}