package com.helloks.dlsclient.parser;

import android.os.AsyncTask;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by helloks on 15. 7. 17.
 */
public class getBook extends AsyncTask<String, Void, ArrayList<String>> {

    @Override
    protected ArrayList<String> doInBackground(String... params) {

        ArrayList<String> result = new ArrayList<>();

        try {

            //대출상태 확인은 직접 schoolCode값을 지정할 수 있음

            URL url_search = new URL("http://reading.gyo6.net/r/reading/search/bookHoldInfo.jsp?dataType=MA&controlNo=" + params[0] + "&schoolCode=172");

            Source source = new Source(url_search);
            source.fullSequentialParse();

            Element table_main = source.getFirstElementByClass("schoolSearchList");
            Element tbody = table_main.getAllElements(HTMLElementName.TBODY).get(0);
            List<Element> tr = tbody.getAllElements(HTMLElementName.TR);

            for (Element tr_now : tr) {

                String registerId = tr_now.getAllElements(HTMLElementName.TD).get(1).getContent().getTextExtractor().toString();
                String bookStatus = tr_now.getAllElements(HTMLElementName.TD).get(2).getContent().getTextExtractor().toString().trim();
                String bookId = tr_now.getAllElements(HTMLElementName.TD).get(3).getContent().getTextExtractor().toString();
                String bookReturn = tr_now.getAllElements(HTMLElementName.TD).get(5).getContent().getTextExtractor().toString().trim();

                if (bookReturn.length() != 0) {
                    result.add(registerId + " / " + bookStatus + " / " + bookId + " / " + bookReturn);
                } else {
                    result.add(registerId + " / " + bookStatus + " / " + bookId);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}