package com.jkit.db;

import com.jkit.db.sql.MergeExpr;
import com.jkit.db.sql.UpdateExpr;
import com.jkit.db.table.ProductTable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import lombok.*;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductTest implements Deps, ProductTable {

    ProductTable.Table table = ProductTable.Table.of(dbModule.getDbWrapper());

    @BeforeAll
    void init() {
        dbModule.runMigration().getOrElseThrow(e -> (Error)e);
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

        assertTrue(actual.isSuccess());

    }

    @Test
    void upsertTest() {

        val actual = table.create("apple");

        assertTrue(actual.isSuccess());

    }

}