package jkit.db;

import jkit.core.model.UserError;
import jkit.db.sql.MergeExpr;
import jkit.db.sql.UpdateExpr;
import jkit.db.table.ProductTable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import lombok.*;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductTest implements Deps, ProductTable {

    ProductTable.Table table = ProductTable.Table.of(dbModule.getDbWrapper());

    @BeforeAll
    void init() {
        dbModule.runMigration().getOrElseThrow(UserError::toError);
    }

    @Test
    void testGetUpsertSql() {

        var actual = MergeExpr.builder()
            .field(Field.name.value("apple"))
            .field(Field.amount.value("22223"))
            .where(Field.name)
            .build()
            .createExpr(tableInfo.getTableName());

        assertNotEquals("", actual.getSql());

    }

    @Test
    void getUpdateSql() {

        var actual = UpdateExpr.builder()
            .field(Field.name.value("orange"))
            .where(Field.id.condition(1))
            .build()
            .createExpr(tableInfo.getTableName());

        assertNotEquals("", actual.getSql());

    }

    @Test
    void getList() {

        var actual = table.getList();

        assertTrue(actual.isRight());

    }

    @Test
    void upsertTest() {

        val actual = table.create("apple");

        assertTrue(actual.isRight());

    }

}