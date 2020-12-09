package jkit.core.model;

import io.vavr.control.Either;
import jkit.core.ext.UrlExt;
import lombok.*;

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

    public Either<UserError, URL> createURL() {
        return UrlExt.createUrl(this);
    }
}
