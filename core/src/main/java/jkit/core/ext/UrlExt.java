package jkit.core.ext;

import io.vavr.Tuple2;
import io.vavr.collection.List;

import java.net.URL;
import java.util.regex.Pattern;

public interface UrlExt {

    String slash = "/";

    Pattern slashRegex = Pattern.compile("(?<=\\w)\\/\\/");
    Pattern hostRegex = Pattern.compile("(?<=\\w)\\/\\/");

    static String createUrl(List<String> urlParts) {

        String res = urlParts
            .filter(s -> !s.isEmpty())
            .mkString("/");

        return slashRegex.matcher(res).replaceAll(slash);

    }

    static String createQueryString(List<Tuple2<String, String>> params) {

        return params
            .map(t -> t._1 + "=" + t._2)
            .mkString("&");

    }

    static String getHost(String input) {

        return TryExt.get(() -> new URL(input), "as").fold(
            err -> null,
            url -> String.format(
                "%s://%s%s",
                url.getProtocol(),
                url.getHost(),
                url.getPort() == -1 ? "" : ":" + url.getPort()
            )
        );
    }

}
