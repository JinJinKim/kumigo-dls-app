package com.helloks.dlsclient.parser;

import android.os.AsyncTask;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class getPopBooks extends AsyncTask<String, Void, ArrayList<HashMap<String, String>>> {

    @Override
    protected ArrayList<HashMap<String, String>> doInBackground(String... params) {

        ArrayList<HashMap<String, String>> result = new ArrayList<>();

        try {

            //schoolCode 설정을 위한 첫번째 접속

            HttpURLConnection sCodeSet = (HttpURLConnection) new URL("http://reading.gyo6.net/r/reading/search/schoolCodeSetting.jsp?schoolCode=172").openConnection();
            sCodeSet.connect();

            String fullCookie = sCodeSet.getHeaderField("Set-Cookie");
            String sessionCookie = fullCookie.substring(0, fullCookie.indexOf(";"));

            //인기도서 목록 획득을 위한 두번째 접속

            URL url_search = new URL("http://reading.gyo6.net/r/reading/search/schoolBestBookData.jsp");
            HttpURLConnection connection_search = (HttpURLConnection) url_search.openConnection();
            connection_search.setRequestProperty("Cookie", sessionCookie);
            connection_search.connect();

            Source source = new Source(connection_search);
            source.fullSequentialParse();

            Element table_main = source.getFirstElementByClass("libraryPopularList");
            Element tbody = table_main.getAllElements(HTMLElementName.TBODY).get(0);
            List<Element> tr = tbody.getAllElements(HTMLElementName.TR);

            for (Element tr_now : tr) {

                HashMap<String, String> semi_result = new HashMap<>();
                Element detail = tr_now.getAllElements(HTMLElementName.TD).get(1);
                Element detail_id = detail.getAllElements(HTMLElementName.A).get(0);

                semi_result.put("ItemID", detail_id.getAttributeValue("onclick"));
                semi_result.put("Item", detail_id.getContent().getTextExtractor().toString());

                result.add(semi_result);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}
