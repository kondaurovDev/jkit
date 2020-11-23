package jkit.core.ext;

import io.vavr.collection.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UrlExtTest {

    @Test
    void createUrl1() {

        final String actual = UrlExt
            .createUrl(List.of("http://alex.com", "some", "path"));

        assertEquals(actual, "http://alex.com/some/path");

    }

    @Test
    void createUrl2() {

        final String actual = UrlExt
            .createUrl(List.of("http://alex.com/a/", "some", "path"));

        assertEquals(actual, "http://alex.com/a/some/path");

    }

    @Test
    void extractHostFromReferrer() {

        assertEquals("http://localhost:8080", UrlExt.getHost("http://localhost:8080/admin/noAccess"));
        assertEquals("http://localhost:8080", UrlExt.getHost("http://localhost:8080"));
        assertEquals("http://localhost:8080", UrlExt.getHost("http://localhost:8080/"));
        assertEquals("http://arr2-web", UrlExt.getHost("http://arr2-web/admin"));

    }

}