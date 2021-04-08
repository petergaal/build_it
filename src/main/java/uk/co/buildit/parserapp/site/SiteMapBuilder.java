package uk.co.buildit.parserapp.site;

import uk.co.buildit.parserapp.parser.ParserInterface;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class SiteMapBuilder {

    public static final int LIMIT = 10;

    private ParserInterface parser;

    public Set<String> getVisitedLinks() {
        return visitedLinks;
    }

    public Set<String> getUnVisitedLinks() {
        return unVisitedLinks;
    }

    public Set<String> getExternalLinks() {
        return externalLinks;
    }

    public Set<String> getVisitedStaticContent() {
        return visitedStaticContent;
    }

    private Set<String> visitedLinks;
    private Set<String> unVisitedLinks;
    private Set<String> externalLinks;
    private Set<String> visitedStaticContent;

    public SiteMapBuilder(ParserInterface parser) {
        this.parser = parser;
    }

    public void buildSiteMap(String startUrl) throws IOException {

        visitedLinks = new HashSet<>();
        unVisitedLinks= new HashSet<>();
        externalLinks = new HashSet<>();
        visitedStaticContent = new HashSet<>();

        unVisitedLinks.add(startUrl);

        int count = 0;
        while (!unVisitedLinks.isEmpty() && count < LIMIT)
        {
            String nextUrl = unVisitedLinks.iterator().next();
            System.out.printf("%d %s%n", count, nextUrl);
            Set<String> staticContent = new HashSet<>();
            Set<String> parsedLinks = parser.parsePage(nextUrl, staticContent);
            visitedLinks.add(nextUrl);
            unVisitedLinks.remove(nextUrl);

            for (String link: parsedLinks) {
                if (!link.startsWith(startUrl)) {
                    externalLinks.add(link);
                } else if (!visitedLinks.contains(link)) {
                    unVisitedLinks.add(link);
                }
            }
            visitedStaticContent.addAll(staticContent);
            count++;
        }

    }
}
