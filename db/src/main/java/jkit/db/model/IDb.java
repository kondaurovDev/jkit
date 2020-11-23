package jkit.db.model;

import io.vavr.collection.List;
import jkit.core.ext.TimeExt;
import org.joda.time.Instant;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import lombok.*;
import java.sql.Timestamp;

public interface IDb {

    interface IDbColumn {
        Boolean getIsJson();
        String getColumnName();
        String getJsonKeyName();
    }

    interface IDbColumnHolder {
        IDbColumn getColumn();
    }

    interface IDbColumnCondition
        extends
            IDbColumnHolder,
            IDbColumnWithValue
    {
        String getSql();
    }

    interface IDbColumnWithValue extends IDbColumnHolder {
        Object getValue();

        static Timestamp getFromInstant(Instant dt) {
            return new Timestamp(dt.getMillis());
        }

        default Object getJdbcValue() {
            var v = getValue();
            if (v instanceof Enum) {
                v = ((Enum<?>)v).name();
            } else if (v instanceof Iterable<?>) {
                v = List.ofAll((Iterable<?>)v).toJavaArray();
            } else if (v instanceof LocalDateTime) {
                v = getFromInstant(((LocalDateTime)v).toDateTime().toInstant());
            } else if (v instanceof Instant) {
                v = getFromInstant((Instant)v);
            } else if (v instanceof LocalTime) {
                v = ((LocalTime) v).toString(TimeExt.timeFormat);
            }
            return v;
        }
    }

}
