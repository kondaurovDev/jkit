package com.jkit.jackson;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jkit.core.JKitData;
import lombok.*;

@Value(staticConstructor = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class JKitJackson<V extends JKitData.IValidator>
    implements IDeserialize, ITransform, IDsl {

    static IFactory factory = new IFactory() {};

    ObjectMapper objectMapper;
    V validator;

    public static <V extends JKitData.IValidator> JKitJackson<V> create(
        V validator,
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
