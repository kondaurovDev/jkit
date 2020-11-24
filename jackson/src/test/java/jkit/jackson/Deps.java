package jkit.jackson;

import com.fasterxml.jackson.databind.JavaType;
import io.vavr.collection.List;
import io.vavr.control.Option;
import jkit.validate.ValidatorImpl;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.NotNull;

public interface Deps {

    JacksonMain<ObjectMapperExt> jacksonMain = JacksonMain.create(
        ValidatorImpl.of()
    );

    ObjectMapperExt json = jacksonMain.getJson();
    ObjectMapperExt yaml = jacksonMain.getYaml();
    IDsl jsonDsl = jacksonMain.getJsonDSL();

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

        static public final JavaType tt = json.getType(MyUser2.class);
    }

}
