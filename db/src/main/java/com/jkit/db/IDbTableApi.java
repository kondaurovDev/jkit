package com.jkit.db;

import io.vavr.collection.List;
import io.vavr.control.Try;
import org.joda.time.LocalDateTime;

public interface IDbTableApi<ApiModel, Model> extends IDbTable<Model> {

    ISql.ReadyExpr toExpr(ApiModel apiModel, LocalDateTime ts);

    default Try<List<Integer>> upsertApiModel(
        List<ApiModel> list,
        LocalDateTime ts
    ) {

        return executeMany(list.map(l -> toExpr(l, ts)));

    }

    default Try<Integer> upsertModel(
        ApiModel model,
        LocalDateTime ts
    ) {

        return executeOne(toExpr(model, ts));

    }

}
