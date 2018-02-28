package com.iexamcenter.odiacalendar.quote;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


import com.iexamcenter.odiacalendar.MainActivity;
import com.iexamcenter.odiacalendar.R;
import com.iexamcenter.odiacalendar.utility.Constant;
import com.iexamcenter.odiacalendar.utility.PrefManager;
import com.iexamcenter.odiacalendar.utility.Utility;

import java.util.ArrayList;


public class QuoteListDialog extends DialogFragment {
    TextView txt1;
    static String quoteKey;
    static MainActivity mContext;
    static PrefManager mPref;
    static ArrayList<MyQuoteListResponse.MyQuoteList> mylist = new ArrayList<>();
    QuoteListAdapter mTodayListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (MainActivity) getActivity();
    }

    public static QuoteListDialog newInstance(String tag, ArrayList<MyQuoteListResponse.MyQuoteList> mylist1) {
        QuoteListDialog f = new QuoteListDialog();
        quoteKey = tag;
        mPref = PrefManager.getInstance(mContext);
        mylist.clear();
        mylist.addAll(mylist1);
        return f;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if (mContext == null)
            return null;
        LayoutInflater inflater = mContext.getLayoutInflater();
        View rootView = inflater.inflate(R.layout.frag_quote_info, null);


        txt1 = (TextView) rootView.findViewById(R.id.txt1);

        if (!quoteKey.toUpperCase().contains("QUOTE"))
            txt1.setText(quoteKey.toUpperCase() + " QUOTE");
        else
            txt1.setText(quoteKey.toUpperCase());
        int colr = Utility.getInstance(mContext).getPageThemeColors()[Constant.PAGE_NOTF];

        rootView.setBackgroundResource(colr);
        RecyclerView todayListView = (RecyclerView) rootView.findViewById(R.id.today);
        mPref = PrefManager.getInstance(mContext);
        // mylist = new ArrayList<>();

        mTodayListAdapter = new QuoteListAdapter(mContext, mylist);
        mTodayListAdapter.getFilter().filter(quoteKey);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        todayListView.setLayoutManager(llm);
        todayListView.setHasFixedSize(false);
        todayListView.setAdapter(mTodayListAdapter);
        //   MyTask obj = new MyTask();
        //  obj.execute();
        Dialog dialog = new Dialog(mContext, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        dialog.setContentView(rootView);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;

    }


}

