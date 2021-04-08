package uk.co.buildit.parserapp.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class HtmlParser implements ParserInterface {

    private static final int GET_PAGE_TIMEOUT = 10_000;

    public Set<String> parsePage(String url, Set<String> staticContent) throws IOException {
        URL urlObject = new URL(url);
        Document doc = Jsoup.parse(urlObject, GET_PAGE_TIMEOUT);
        return parseContent(doc, staticContent);
    }

    public Set<String> parsePage(String pageHtml, String url, Set<String> staticContent) throws IOException {
        Document doc = Jsoup.parse(pageHtml, url);
        return parseContent(doc, staticContent);
    }

    public Set<String> parseContent(Document doc, Set<String> staticContent) throws IOException {
        Elements linkElements = doc.select("a[href]");
        Set<String> linkItems = new HashSet<>();
        linkElements.forEach(link -> linkItems.add(link.attr("href")));
        Elements images = doc.select("img[src]");
        images.forEach(link -> staticContent.add(link.attr("src")));
        return linkItems;
    }
}
