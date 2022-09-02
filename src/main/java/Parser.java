import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser<iterationCount> {
    private static Document getPage() throws IOException {
        String url = "https://www.pogoda.spb.ru";
        Document page = Jsoup.parse(new URL(url), 3000);
        return page;
    }

    private static Pattern pattern = Pattern.compile("\\d{2}\\.\\d{2}");

    private static String getDateFromString(String stringDate) throws Exception {
        Matcher matcher = pattern.matcher(stringDate);
        if (matcher.find()) {
            return matcher.group();
        }
        throw new Exception("Can`t extract date from string!");

    }

    private static int printPartValues(Elements value, int index) {
        int iterationCount = 4;
        if (index == 0)
        {
            Element valueLn = value.get(0);
            if (valueLn.text().contains("День"))
            {
                iterationCount = 3;
            }
            else if (valueLn.text().contains("Вечер"))
            {
                iterationCount = 2;
            }
            else if (valueLn.text().contains("Ночь"))
            {
                iterationCount = 1;
            }
        }



        for (int i = 0; i < iterationCount; i++) {
            Element valueLine = value.get(index + i);
            for (Element td : valueLine.select("td")) {
                System.out.println(td.text() + "    ");
            }
            System.out.println();

            }
            return iterationCount;
        }


        public static void main (String[]args) throws Exception {
            Document page = getPage();
            Element tabLeWth = page.select("table[class=wt]").first();
            Elements names = tabLeWth.select("tr[class=wth]");
            Elements values = tabLeWth.select("tr[valign=top]");
            int index = 0;
            for (Element name : names) {
                String dateString = name.select("th[id=dt]").text();
                String date = getDateFromString(dateString);
                System.out.println(date + "   Явления  Температура  Давл  Влажность  Ветер");
                int iterationCount = printPartValues(values, index);
                index = index + iterationCount;

            }

        }
    }
