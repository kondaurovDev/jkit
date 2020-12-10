package jkit.core.ext;

import jkit.core.model.Url;
import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UrlExtTest {

    @Test
    void createUrl1() {

        val actual = Url
            .createUrl(url -> url
                .base("http://alex.com")
                .path("some")
                .path("path")
            ).createUrlString();

        assertEquals(actual, "http://alex.com/some/path");

    }

    @Test
    void createUrl2() {

        val actual = Url
            .createUrl(url -> url
                .base("http://alex.com/a/")
                .path("some")
                .path("path")
            ).createUrlString();

        assertEquals(actual, "http://alex.com/a/some/path");

    }

    void getHost(String expected, String url) {
        assertEquals(expected, UrlExt.getHost(UrlExt.createURL(url).get()));
    }

    @Test
    void extractHostFromReferrer() {
        getHost("http://localhost:8080", "http://localhost:8080/admin/noAccess");
        getHost("http://localhost:8080", "http://localhost:8080");
        getHost("http://localhost:8080", "http://localhost:8080/");
        getHost("http://arr2-web", "http://arr2-web/admin");
    }

}