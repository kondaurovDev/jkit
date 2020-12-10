package jkit.core.model;

import io.vavr.control.Either;
import jkit.core.ext.StreamExt;
import jkit.core.ext.UrlExt;
import lombok.*;

import java.net.URI;
import java.net.URL;
import java.util.List;

@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class Url {
    String base;
    @Singular("path")
    List<String> urlParts;
    @Singular
    List<Pair<String, String>> queryParams;

    public Either<UserError, URI> createURI() { return UrlExt.createURI(createUrlString()); }
    public Either<UserError, URL> createURL() { return UrlExt.createURL(createUrlString()); }

    public String createUrlString() {

        String res = base;

        if (!urlParts.isEmpty() && !res.endsWith("/"))
            res += "/";

        res += StreamExt.join(
            urlParts.stream().filter(s -> !s.isEmpty()),
            v -> v,
            "/"
        );

        if (!queryParams.isEmpty())
            res += "?" + UrlExt.createQueryString(queryParams);

        res = UrlExt.slashRegex.matcher(res).replaceAll(UrlExt.slash);

        return res;

    }





}
