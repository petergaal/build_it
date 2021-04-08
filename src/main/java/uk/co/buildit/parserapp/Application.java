package uk.co.buildit.parserapp;

import uk.co.buildit.parserapp.parser.HtmlParser;
import uk.co.buildit.parserapp.site.SiteMapBuilder;

import java.io.IOException;
import java.util.Set;

public class Application {

    public static void main(String[] args) throws IOException {
        HtmlParser parser = new HtmlParser();

        SiteMapBuilder siteMapBuilder = new SiteMapBuilder(parser);

        String startUrl = "https://wiprodigital.com";
        siteMapBuilder.buildSiteMap(startUrl);

        Set<String> visitedLinks = siteMapBuilder.getVisitedLinks();
        Set<String> externalLinks = siteMapBuilder.getExternalLinks();
        Set<String> visitedStaticContent = siteMapBuilder.getVisitedLinks();

        System.out.println("Page links:");
        visitedLinks.stream().sorted().forEach(System.out::println);
        System.out.println("");

        System.out.println("Static content:");
        visitedStaticContent.stream().sorted().forEach(System.out::println);
        System.out.println("");

        System.out.println("External links:");
        externalLinks.stream().sorted().forEach(System.out::println);
        System.out.println("");

    }
}
