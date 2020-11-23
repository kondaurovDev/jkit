package jkit.db.model;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DbColumnMeta {
    String label;
    int typeCode;
    boolean isLast;
}