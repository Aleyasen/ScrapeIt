/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scrapeit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class ScrapeIt {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ScrapeIt sc = new ScrapeIt();
        String query = "Iran";
        String url = createURL(query);
        Document doc = sc.getPageDoc(url);
        List<SearchResult> results = sc.extractSearchResults(doc);
        System.out.println("SearchResults = " + results);
    }

    private static String createURL(String query) {
        query = query.replace(" ", "+");
        String url = "https://www.google.com/search?q=" + query;
        return url;
    }

    public List<SearchResult> extractSearchResults(Document doc) {
        List<SearchResult> results = new ArrayList<>();
        Elements blocks = doc.select("li.g");
        for (Element block : blocks) {
            String url = block.select(".r > a").attr("href");
            String label = block.select(".r > a").text();
            String short_desc = block.select(".st").text();

            SearchResult result = new SearchResult();
            result.URL = url;
            result.label = label;
            result.short_desc = short_desc;
            results.add(result);
        }
        return results;

    }

    public Document getPageDoc(String url) {
        try {
            Document doc = Jsoup
                    .connect(url)
                    .userAgent(
                            "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)")
                    .timeout(5000).get();
            System.out.println(doc);
            return doc;

        } catch (IOException ex) {
            Logger.getLogger(ScrapeIt.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
