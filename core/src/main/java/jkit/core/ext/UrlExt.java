package jkit.core.ext;

import io.vavr.Function1;
import io.vavr.control.Either;
import jkit.core.model.Pair;
import jkit.core.model.Url;
import jkit.core.model.UserError;

import java.net.URL;
import java.util.List;
import java.util.regex.Pattern;

public interface UrlExt {

    String slash = "/";

    Pattern slashRegex = Pattern.compile("(?<=\\w)\\/\\/");
    Pattern hostRegex = Pattern.compile("(?<=\\w)\\/\\/");

    static Either<UserError, URL> createURL(String input) {
        return TryExt
            .get(() -> new URL(input), "create URL");
    }

    static Either<UserError, URL> createUrl(Function1<Url.UrlBuilder, Url.UrlBuilder> builder) {
        return createUrl(builder.apply(Url.builder()).build());
    }

    static Either<UserError, URL> createUrl(Url url) {

        String res = url.getBase();

        if (!url.getUrlParts().isEmpty() && !res.endsWith("/"))
            res += "/";

        res += StreamExt.join(
            url.getUrlParts().stream().filter(s -> !s.isEmpty()),
            v -> v,
            "/"
        );

        if (!url.getQueryParams().isEmpty())
            res += "?" + createQueryString(url.getQueryParams());

        res = slashRegex.matcher(res).replaceAll(slash);

        return createURL(res);

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
