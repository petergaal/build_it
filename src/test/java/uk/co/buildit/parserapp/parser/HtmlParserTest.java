package uk.co.buildit.parserapp.parser;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class HtmlParserTest {

    @Test
    void parsePageTest() throws IOException {
        HtmlParser parser = new HtmlParser();
        String html = "<a href=\"/our-contact.html\">contact us</a>";
        Set<String> links = parser.parsePage(html, "https://wiprodigital.com", new HashSet<>());
        assertEquals(1, links.size());
        assertEquals("/our-contact.html", links.iterator().next());
    }
}