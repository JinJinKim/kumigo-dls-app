package com.helloks.dlsclient;


import android.app.ListFragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.helloks.dlsclient.parser.getPopBooks;

import java.util.ArrayList;
import java.util.HashMap;


public class PopBooksFragment extends ListFragment {

    public static final String TAG = "pop";

    SimpleAdapter simpleAdapter;
    ListView listView;
    ArrayList<HashMap<String, String>> lv_content = new ArrayList<>();


    public PopBooksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pop_books, container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("content", lv_content);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = getListView();

        simpleAdapter = new SimpleAdapter(getActivity(), lv_content, android.R.layout.simple_list_item_1, new String[]{"Item"}, new int[]{android.R.id.text1});
        listView.setAdapter(simpleAdapter);

        if (savedInstanceState != null) {
            lv_content.addAll((ArrayList<HashMap<String, String>>) savedInstanceState.getSerializable("content"));
            simpleAdapter.notifyDataSetChanged();
        } else {
            new getPopBooks() {

                ProgressDialog pDialog;

                protected void onPreExecute() {
                    super.onPreExecute();
                    lv_content.clear();
                    pDialog = ProgressDialog.show(getActivity(), null, "책 정보를 받아오는 중입니다", true, false);
                }

                protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
                    lv_content.addAll(result);
                    simpleAdapter.notifyDataSetChanged();
                    pDialog.dismiss();
                }
            }.execute();
        }

    }


}
