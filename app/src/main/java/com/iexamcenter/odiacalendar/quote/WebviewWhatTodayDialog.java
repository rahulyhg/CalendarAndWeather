package com.iexamcenter.odiacalendar.quote;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.iexamcenter.odiacalendar.MainActivity;
import com.iexamcenter.odiacalendar.R;
import com.iexamcenter.odiacalendar.utility.PrefManager;


public class WebviewWhatTodayDialog extends DialogFragment {
    TextView txt1, txt2;
    PrefManager mPref;
    String lang;
    Resources res;
    static private MainActivity mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = ((MainActivity) getActivity());
    }

    public static WebviewWhatTodayDialog newInstance(MainActivity ctx) {
        WebviewWhatTodayDialog f = new WebviewWhatTodayDialog();
        mContext = ctx;
        return f;
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    WebView webView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (mContext == null)
            return null;
        LayoutInflater inflater = mContext.getLayoutInflater();
        View rootView = inflater.inflate(R.layout.frag_webview_whattoday, null);

        mPref = PrefManager.getInstance(mContext);
        String NEWS_LINK = getArguments().getString("url");
        int page = getArguments().getInt("page", 0);
        // RelativeLayout webViewCont = (RelativeLayout) rootView.findViewById(R.id.webViewCont);
        //int colr = Utility.getInstance(mContext).getPageThemeColors()[page];

        //webViewCont.setBackgroundResource(colr);

        lang = mPref.getMyLanguage();
        webView = (WebView) rootView.findViewById(R.id.webView);
        res = mContext.getResources();
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.loadUrl(NEWS_LINK);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView viewx, String urlx) {
                viewx.loadUrl(urlx);
                return false;
            }
        });


        Dialog dialog = new Dialog(mContext, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        dialog.setContentView(rootView);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

