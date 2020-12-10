package jkit.core.ext;

import io.vavr.control.Either;
import jkit.core.model.Pair;
import jkit.core.model.UserError;

import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.regex.Pattern;

public interface UrlExt {

    String slash = "/";

    Pattern slashRegex = Pattern.compile("(?<=\\w)\\/\\/");

    static Either<UserError, URL> createURL(String url) {
        return TryExt
            .get(() -> new URL(url), "create URL");
    }

    static Either<UserError, URI> createURI(String url) {
        return TryExt
            .get(() -> new URI(url), "create URI");
    }

    static String createQueryString(List<Pair<String, String>> params) {

        return StreamExt.join(
            params.stream().map(t -> t.getFirst() + "=" + t.getSecond()),
            v -> v,
            "&"
        );

    }

    static String getHost(URL url) {
        return String.format(
            "%s://%s%s",
            url.getProtocol(),
            url.getHost(),
            url.getPort() == -1 ? "" : ":" + url.getPort()
        );
    }

}
