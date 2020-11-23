package jkit.core.ext;

import io.vavr.collection.List;
import io.vavr.control.Either;
import jkit.core.model.UserError;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public interface StringExt {

    interface PatternStrings {

        Pattern camel2snake = Pattern.compile("([^_A-Z])([A-Z])");

    }

    static String snake2camel(String snake) {

        if (snake.contains("_")) {
            return List
                .of(snake.split("_"))
                .map(String::toLowerCase)
                .foldLeft("", (t1, t2) -> {
                    if (t1.isEmpty()) {
                        return t2;
                    } else {
                        final String capitalized =
                            t2.substring(0, 1).toUpperCase().concat(t2.substring(1));
                        return t1.concat(capitalized);
                    }

                });
        } else {
            return snake.toLowerCase();
        }

    }

    static String camel2snake(String camel) {
        return PatternStrings.camel2snake
            .matcher(camel)
            .replaceAll("$1_$2")
            .toLowerCase();
    }

    static String unCapitalize(String input) {
        return Character.toLowerCase(input.charAt(0)) +
            (input.length() > 1 ? input.substring(1) : "");
    }

    static String encodeBase64(String source) {
        return Base64.getEncoder().withoutPadding().encodeToString(source.getBytes());
    }

    static Either<UserError, String> decodeBase64(String source) {
        return TryExt.get(() ->
            new String(Base64.getDecoder().decode(source)),
            "decode base64 string"
        ).flatMap(s -> {
            if (s.isEmpty()) {
                return Either.left(UserError.create("Not valid base 64 string"));
            } else {
                return Either.right(s);
            }
        });
    }

    static String limitLength(String s, Integer maxLength) {
        return s.substring(0, Math.min(s.length(), maxLength));
    }

    static String fromInputStream(
        InputStream inputStream
    ) {
        String text = new BufferedReader(
          new InputStreamReader(inputStream, StandardCharsets.UTF_8)
        )
            .lines()
            .collect(Collectors.joining("\n"));
        return text;
    }

}
