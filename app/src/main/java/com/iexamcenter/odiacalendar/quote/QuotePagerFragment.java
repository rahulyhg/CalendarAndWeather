package com.iexamcenter.odiacalendar.quote;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.iexamcenter.odiacalendar.MainActivity;
import com.iexamcenter.odiacalendar.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by sasikanta on 11/14/2017.
 * QuotePagerFragment
 */

public class QuotePagerFragment extends Fragment {
    public static String ARG_POSITION;

    ViewGroup rootView;
    int num;
    int pageType = 1;
    MainActivity activity;
    String[] allAuth, myCat;
    RecyclerView recyclerView;
    List<String> qAuthor, qCategory, qIndianAuth;

    public static QuotePagerFragment newInstance() {

        return new QuotePagerFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_quote_list, container, false);
        setRetainInstance(false);
        setHasOptionsMenu(true);
        Bundle bundle = getArguments();
        num = bundle != null ? bundle.getInt(ARG_POSITION, 0) : 0;
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arg = getArguments();

        // myAuth = context.getResources().getStringArray(R.array.quote_indian);
        myCat = activity.getResources().getStringArray(R.array.cat);
        allAuth = activity.getResources().getStringArray(R.array.auth);
        //  qIndianAuth = Arrays.asList(myAuth);
        qIndianAuth=new ArrayList<>();
        qCategory = Arrays.asList(myCat);
        qAuthor = Arrays.asList(allAuth);
        Iterator<String> it = qAuthor.iterator();
        while (it.hasNext()){
            String item = it.next();
            String[] tmp=item.split(Pattern.quote("|"));
            if(Integer.parseInt(tmp[0])==1){
                qIndianAuth.add(item);
            }
        }

        final Spinner filterSpinner = view.findViewById(R.id.filterSpinner);
        TextView spinnerLbl = view.findViewById(R.id.spinnerLbl);
        spinnerLbl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterSpinner.performClick();
            }
        });
        ArrayAdapter adapter = ArrayAdapter.createFromResource(activity, R.array.quote_filter, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        filterSpinner.setAdapter(adapter);
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        recyclerView.setLayoutManager(new GridLayoutManager(activity, 3));
                        recyclerView.setAdapter(new MyQuoteRecyclerViewAdapter(qCategory, activity,0));
                        break;
                    case 1:
                        recyclerView.setLayoutManager(new GridLayoutManager(activity,3));
                        recyclerView.setAdapter(new MyQuoteRecyclerViewAdapter(qAuthor, activity,1));
                        break;
                    case 2:
                        recyclerView.setLayoutManager(new GridLayoutManager(activity,3));
                        recyclerView.setAdapter(new MyQuoteRecyclerViewAdapter(qIndianAuth, activity,1));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        recyclerView = view.findViewById(R.id.list1);


    }

    protected void setUp(View view) {


    }
}
