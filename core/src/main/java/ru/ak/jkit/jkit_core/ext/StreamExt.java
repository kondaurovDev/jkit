package ru.ak.jkit.jkit_core.ext;

import io.vavr.CheckedFunction0;
import io.vavr.Function1;
import io.vavr.control.Try;
import lombok.val;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface StreamExt {

    static Try<String> inputStreamToString(
        InputStream inputStream
    ) {
        return StreamExt
            .readAllBytes(inputStream)
            .map(bytes -> new String(bytes, StandardCharsets.UTF_8));
    }

    static <T> Stream<T> getStreamFromIterator(Iterator<T> iterator) {

        // Convert the iterator to Spliterator
        val spliterator = Spliterators
          .spliteratorUnknownSize(iterator, 0);

        // Get a Sequential Stream from spliterator
        return StreamSupport.stream(spliterator, false);
    }

    static Try<byte[]> readAllBytes(InputStream inputStream) {
        return Try
            .of(() -> {
                val buffer = new ByteArrayOutputStream();
                int nRead;
                val data = new byte[16384];

                while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                  buffer.write(data, 0, nRead);
                }

                return buffer.toByteArray();
            })
            .onFailure(e -> IOExt.debug("read input stream", e));

    }

    static <A> String join(
        Stream<A> stream,
        Function1<A, String> handle,
        String joinBy
    ) {
        return stream
            .map(handle).collect(Collectors.joining(joinBy));
    }

    static <A, B> HashMap<String, B> streamToMap(
        Stream<A> input,
        Function1<A, String> getKey,
        Function1<A, B> getValue
    ) {
            return input
                .collect(Collectors.toMap(getKey, getValue, (prev, next) -> next, HashMap::new));
    }

    static ByteArrayInputStream fromBytes(byte[] input) {
        return new ByteArrayInputStream(input);
    }

    static CheckedFunction0<FileInputStream> fromFile(File file) {
        return () -> new FileInputStream(file);
    }

}
