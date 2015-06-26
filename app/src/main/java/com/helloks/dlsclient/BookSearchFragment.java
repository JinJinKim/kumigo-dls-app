package com.helloks.dlsclient;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.helloks.dlsclient.parser.SearchDLS;

import java.util.ArrayList;
import java.util.HashMap;

public class BookSearchFragment extends ListFragment {

    public static final String TAG = "search";

    EditText search_param;
    ListView search_result_lv;
    SimpleAdapter lv_adapter;
    ArrayList<HashMap<String, String>> lv_content = new ArrayList<>();
    Button.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button:

                    String keyword = search_param.getText().toString();

                    if (keyword != null && keyword.length() != 0) {

                        String[] params = {search_param.getText().toString()};
                        new SearchDLS() {

                            ProgressDialog pDialog;

                            protected void onPreExecute() {
                                super.onPreExecute();
                                lv_content.clear();
                                pDialog = ProgressDialog.show(getActivity(), null, "책 정보를 받아오는 중입니다", true, false);
                            }

                            protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
                                lv_content.addAll(result);
                                lv_adapter.notifyDataSetChanged();
                                pDialog.dismiss();

                                if (lv_content.size() == 0) {
                                    Toast.makeText(getActivity(), "찾으시는 책이 없습니다.", Toast.LENGTH_LONG).show();
                                }

                            }
                        }.execute(params);
                    } else {
                        Toast.makeText(getActivity(), "검색어를 입력하셔야 합니다.", Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    };

    public BookSearchFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_book_search, container, false);

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button search_btn = (Button) view.findViewById(R.id.button);
        search_btn.setOnClickListener(listener);
        search_param = (EditText) view.findViewById(R.id.bookName);
        search_result_lv = getListView();

        lv_adapter = new SimpleAdapter(getActivity(), lv_content, android.R.layout.simple_list_item_2, new String[]{"ItemName", "ItemDetail"}, new int[]{android.R.id.text1, android.R.id.text2});
        search_result_lv.setAdapter(lv_adapter);

        if (savedInstanceState != null) {
            lv_content.addAll((ArrayList<HashMap<String, String>>) savedInstanceState.getSerializable("content"));
            search_param.setText(savedInstanceState.getString("keyword"));

            lv_adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("content", lv_content);
        outState.putString("keyword", search_param.getText().toString());

    }


}
