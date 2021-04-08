package uk.co.buildit.parserapp.parser;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Set;

public interface ParserInterface {
    Set<String> parsePage(String url, Set<String> staticContent) throws IOException;
    Set<String> parsePage(String pageHtml, String url, Set<String> staticContent) throws IOException;
    Set<String> parseContent(Document doc, Set<String> staticContent) throws IOException;
}
