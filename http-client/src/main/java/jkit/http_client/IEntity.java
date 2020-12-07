package jkit.http_client;

import io.vavr.Tuple2;
import io.vavr.control.Either;
import jkit.core.JKitData;
import jkit.core.ext.TryExt;
import jkit.core.model.UserError;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

import lombok.*;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.stream.Stream;

interface IEntity {

    JKitData.IObjMapper<?> getJsonObjMapper();

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

        return getJsonObjMapper().serialize(object)
            .flatMap(this::createStringEntity)
            .map(e -> {
                e.setContentType("application/json");
                return e;
            })
            .mapLeft(e -> e.withError("create json entity"));

    }

    default HttpEntity getFormEntity(Stream<Tuple2<String, String>> args) {
        val params = args.map(p -> new BasicNameValuePair(p._1, p._2));
        return new UrlEncodedFormEntity(params.collect(Collectors.toList()), StandardCharsets.UTF_8);
    }

}
