package ru.ak.jkit.jkit_core.model;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value(staticConstructor = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Pair<T1, T2> {
    T1 first;
    T2 second;
}
