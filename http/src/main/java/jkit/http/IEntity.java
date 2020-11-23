package jkit.http;

import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.control.Either;
import jkit.core.ext.TryExt;
import jkit.core.iface.IObjMapper;
import jkit.core.model.UserError;
import lombok.val;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.nio.charset.StandardCharsets;

interface IEntity {

    IObjMapper getObjectMapper();

    default Either<UserError, StringEntity> createStringEntity(String s) {
        return TryExt.get(
            () -> new StringEntity(s, StandardCharsets.UTF_8),
            "Create string entity"
        );
    }

    default Either<UserError, FileEntity> createFileEntity(String path) {
        return TryExt.get(
            () -> new FileEntity(new File(path)),
            "Create file entity"
        );
    }

    default Either<UserError, StringEntity> createJsonEntity(
        Object object
    ) {

        return getObjectMapper().serialize(object)
            .flatMap(this::createStringEntity)
            .map(e -> {
                e.setContentType("application/json");
                return e;
            })
            .mapLeft(e -> e.withError("create json entity"));

    }

    default HttpEntity getFormEntity(List<Tuple2<String, String>> args) {
        val params = args.map(p -> new BasicNameValuePair(p._1, p._2));
        return new UrlEncodedFormEntity(params.toJavaList(), StandardCharsets.UTF_8);
    }

}
