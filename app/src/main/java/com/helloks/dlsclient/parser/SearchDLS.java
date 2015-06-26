package com.helloks.dlsclient.parser;

import android.os.AsyncTask;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by helloks on 15. 6. 14.
 */
public class SearchDLS extends AsyncTask<String, Void, ArrayList<HashMap<String, String>>> {

    @Override
    protected ArrayList<HashMap<String, String>> doInBackground(String... params) {

        ArrayList<HashMap<String, String>> result = new ArrayList<>();

        try {

            //schoolCode 설정을 위한 첫번째 접속

            HttpURLConnection sCodeSet = (HttpURLConnection) new URL("http://reading.gyo6.net/r/reading/search/schoolCodeSetting.jsp?schoolCode=172").openConnection();
            sCodeSet.connect();

            String fullCookie = sCodeSet.getHeaderField("Set-Cookie");
            String sessionCookie = fullCookie.substring(0, fullCookie.indexOf(";"));

            //검색정보 획득을 위한 두번째 접속

            String requestContent = URLEncoder.encode(params[0], "utf-8");
            String finalRequest = URLEncoder.encode(requestContent, "utf-8");

            URL url_search = new URL("http://reading.gyo6.net/r/reading/search/schoolSearchResult.jsp?currentPage=1&controlNo=&bookInfo=&boxCmd=&pageParamInfo=&prevPageInfo=&division1=ALL&searchCon1=" + finalRequest + "&connect1=A&division2=TITL&searchCon2=&connect2=A&division3=PUBL&searchCon3=&dataType=ALL&lineSize=10");
            HttpURLConnection connection_search = (HttpURLConnection) url_search.openConnection();
            connection_search.setRequestProperty("Cookie", sessionCookie);
            connection_search.connect();

            Source source = new Source(connection_search);
            source.fullSequentialParse();

            Element table_main = source.getFirstElementByClass("result");
            Element tbody = table_main.getAllElements(HTMLElementName.TBODY).get(0);
            List<Element> tr = tbody.getAllElements(HTMLElementName.TR);

            for (Element tr_now : tr) {

                HashMap<String, String> semi_result = new HashMap<>();
                Element detail = tr_now.getAllElements(HTMLElementName.TD).get(0);
                Element detail_id = detail.getAllElements(HTMLElementName.A).get(0);

                semi_result.put("ItemID", detail_id.getAttributeValue("onclick"));
                semi_result.put("ItemName", detail_id.getContent().getTextExtractor().toString());
                semi_result.put("ItemDetail", tr_now.getAllElements(HTMLElementName.TD).get(1).getContent().getTextExtractor().toString());

                result.add(semi_result);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}
