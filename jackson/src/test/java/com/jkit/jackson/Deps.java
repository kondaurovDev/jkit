package com.jkit.jackson;

import com.fasterxml.jackson.databind.JavaType;
import io.vavr.collection.List;
import io.vavr.control.Option;
import com.jkit.validate.Validator;
import lombok.*;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import javax.validation.constraints.NotNull;

public interface Deps {

    JKitJackson json = JKitJackson.create(
        Validator.of(),
        (module, mapper, factory) ->
            factory
                .createSerde(
                    module,
                    LocalDateTime.class,
                    dt -> dt.toString("dd/MM/YY HH:mm:ss"),
                    node -> {
                        if (node.isLong()) {
                            return new LocalDateTime(node.longValue());
                        } else {
                            throw new Error("Unknown input for date time");
                        }
                    }
                )
                .createSerde(
                    module,
                    LocalTime.class,
                    dt -> dt.toString("HH:mm:ss"),
                    node -> {
                        if (node.isLong()) {
                            return new LocalTime(node.longValue());
                        }

                        String s = node.asText();
                        return LocalTime.parse(s);
                    }
                )
                .configureObjectMapper(mapper)
    );

    @Value
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    class MyUser {
        @NotNull
        String userId;
        @NotNull
        String userName;
        @NotNull
        Iterable<String> groups;
        List<Integer> nums;
    }

    class EitherTest {
        Option<String> option1;
        String option2;
        Boolean option3;
    }

    enum CrudAction {
        create,
        update,
        delete
    }

    @Value
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    class MyTestConfig {
        String name;
        Boolean available;
    }

    @Value
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    class MyUser2 {
        String userName;
        List<Integer> nums;

        static public final JavaType tt = json.getCustomType(MyUser2.class);
    }

}
