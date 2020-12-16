package jkit.jackson;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jkit.core.JKitValidate;
import lombok.*;

@Value(staticConstructor = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class JKitJackson
    implements IDeserialize, ITransform, IDsl {

    static IFactory factory = new IFactory() {};

    ObjectMapper objectMapper;
    JKitValidate.IValidator validator;

    public static JKitJackson create(
        JKitValidate.IValidator validator,
        IFactory.MapperBuilder buildMapper
    ) {
        val simpleModule = new SimpleModule();
        val mapper = factory.createMapper();
        return JKitJackson.of(
            buildMapper.build(simpleModule, mapper, factory),
            validator
        );
    }

    public ObjectNode newObj() {
        return objectMapper
            .createObjectNode();
    }

}
