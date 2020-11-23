package jkit.core.ext;

import lombok.val;

import java.util.Iterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface StreamExt {

    static <T> Stream<T> getStreamFromIterator(Iterator<T> iterator) {

        // Convert the iterator to Spliterator
        val spliterator = Spliterators
          .spliteratorUnknownSize(iterator, 0);

        // Get a Sequential Stream from spliterator
        return StreamSupport.stream(spliterator, false);
    }

}
