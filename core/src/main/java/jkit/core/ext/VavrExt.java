package jkit.core.ext;

import io.vavr.collection.HashMap;
import io.vavr.collection.List;

public interface VavrExt {

    static <K, V> HashMap<K, V> createEmptyMap() {
        return HashMap.empty();
    }

    static <A> List<A> createList(
        Iterable<A> elems
    ) {
        return List.ofAll(elems);
    }

    static <A> List<A> createList(
        A[] elems
    ) {
        return List.of(elems);
    }

}
