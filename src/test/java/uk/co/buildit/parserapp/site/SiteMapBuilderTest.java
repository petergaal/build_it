package uk.co.buildit.parserapp.site;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import uk.co.buildit.parserapp.parser.ParserInterface;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SiteMapBuilderTest {

    @Mock
    ParserInterface parserInterface;

    @Test
    void pageWithNoLinksShouldParseOnlyTheStartUrl() throws IOException {

        when(parserInterface.parsePage(any(), any())).thenReturn(Collections.emptySet());

        SiteMapBuilder siteMapBuilder = new SiteMapBuilder(parserInterface);

        siteMapBuilder.buildSiteMap("any site");

        verify(parserInterface, times(1)).parsePage(eq("any site"), any());

        assertEquals(Collections.emptySet(), siteMapBuilder.getExternalLinks());
        assertEquals(Collections.singleton("any site"), siteMapBuilder.getVisitedLinks());
        assertEquals(Collections.emptySet(), siteMapBuilder.getUnVisitedLinks());
        assertEquals(Collections.emptySet(), siteMapBuilder.getVisitedStaticContent());
    }

    @Test
    void siteMapBuilderShouldReturnStaticContentWhenParserReturnsStaticContent() throws IOException {

        doAnswer(invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            ((Set<String>)args[1]).add("static content");
            return Collections.emptySet();
        }).when(parserInterface).parsePage(any(), any());

        SiteMapBuilder siteMapBuilder = new SiteMapBuilder(parserInterface);

        siteMapBuilder.buildSiteMap("any site");

        verify(parserInterface, times(1)).parsePage(eq("any site"), any());

        assertEquals(Collections.emptySet(), siteMapBuilder.getExternalLinks());
        assertEquals(Collections.singleton("any site"), siteMapBuilder.getVisitedLinks());
        assertEquals(Collections.emptySet(), siteMapBuilder.getUnVisitedLinks());
        assertEquals(Collections.singleton("static content"), siteMapBuilder.getVisitedStaticContent());
    }

    @Test
    void shouldReturnTwoLinks() throws IOException {

        when(parserInterface.parsePage(eq("https://startpage"), any())).thenReturn(Collections.singleton("https://startpage/second"));
        when(parserInterface.parsePage(eq("https://startpage/second"), any())).thenReturn(Collections.emptySet());

        SiteMapBuilder siteMapBuilder = new SiteMapBuilder(parserInterface);

        siteMapBuilder.buildSiteMap("https://startpage");

        verify(parserInterface, times(1)).parsePage(eq("https://startpage"), any());
        verify(parserInterface, times(1)).parsePage(eq("https://startpage/second"), any());

        assertEquals(Collections.emptySet(), siteMapBuilder.getExternalLinks());
        assertEquals(Stream.of("https://startpage", "https://startpage/second")
                .collect(Collectors.toCollection(HashSet::new)), siteMapBuilder.getVisitedLinks());
        assertEquals(Collections.emptySet(), siteMapBuilder.getUnVisitedLinks());
        assertEquals(Collections.emptySet(), siteMapBuilder.getVisitedStaticContent());
    }

    @Test
    void shouldHandleCircularReferences() throws IOException {

        when(parserInterface.parsePage(eq("https://startpage"), any())).thenReturn(Collections.singleton("https://startpage/second"));
        when(parserInterface.parsePage(eq("https://startpage/second"), any())).thenReturn(Collections.singleton("https://startpage"));

        SiteMapBuilder siteMapBuilder = new SiteMapBuilder(parserInterface);

        siteMapBuilder.buildSiteMap("https://startpage");

        verify(parserInterface, times(1)).parsePage(eq("https://startpage"), any());
        verify(parserInterface, times(1)).parsePage(eq("https://startpage/second"), any());

        assertEquals(Collections.emptySet(), siteMapBuilder.getExternalLinks());
        assertEquals(Stream.of("https://startpage", "https://startpage/second")
                .collect(Collectors.toCollection(HashSet::new)), siteMapBuilder.getVisitedLinks());
        assertEquals(Collections.emptySet(), siteMapBuilder.getUnVisitedLinks());
        assertEquals(Collections.emptySet(), siteMapBuilder.getVisitedStaticContent());
    }

    @Test
    void shouldHandleExternalLinks() throws IOException {

        when(parserInterface.parsePage(eq("https://startpage"), any())).thenReturn(Collections.singleton("https://startpage/second"));
        when(parserInterface.parsePage(eq("https://startpage/second"), any())).thenReturn(Collections.singleton("https://external.page"));

        SiteMapBuilder siteMapBuilder = new SiteMapBuilder(parserInterface);

        siteMapBuilder.buildSiteMap("https://startpage");

        verify(parserInterface, times(1)).parsePage(eq("https://startpage"), any());
        verify(parserInterface, times(1)).parsePage(eq("https://startpage/second"), any());

        assertEquals(Collections.singleton("https://external.page"), siteMapBuilder.getExternalLinks());
        assertEquals(Stream.of("https://startpage", "https://startpage/second")
                .collect(Collectors.toCollection(HashSet::new)), siteMapBuilder.getVisitedLinks());
        assertEquals(Collections.emptySet(), siteMapBuilder.getUnVisitedLinks());
        assertEquals(Collections.emptySet(), siteMapBuilder.getVisitedStaticContent());
    }
}
