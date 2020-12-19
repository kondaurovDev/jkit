package com.jkit.db.model;

import lombok.*;

@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DbColumnMeta {
    String label;
    int typeCode;
    boolean isLast;
}