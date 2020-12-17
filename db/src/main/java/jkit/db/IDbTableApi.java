package jkit.db;

import io.vavr.collection.List;
import io.vavr.control.Either;
import jkit.core.model.JKitError;
import org.joda.time.LocalDateTime;

public interface IDbTableApi<ApiModel, Model> extends IDbTable<Model> {

    ISql.ReadyExpr toExpr(ApiModel apiModel, LocalDateTime ts);

    default Either<JKitError, List<Integer>> upsertApiModel(
        List<ApiModel> list,
        LocalDateTime ts
    ) {

        return executeMany(list.map(l -> toExpr(l, ts)));

    }

    default Either<JKitError, Integer> upsertModel(
        ApiModel model,
        LocalDateTime ts
    ) {

        return executeOne(toExpr(model, ts));

    }

}
