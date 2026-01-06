package com.example.doma.jdbc;

import org.seasar.doma.jdbc.UnknownColumnHandler;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.query.Query;

import java.util.function.Supplier;


public final class UnknownColumnIgnoreHandler implements UnknownColumnHandler {

    @Override
    public void handle(Query query, EntityType<?> entityType, String unknownColumnName, Supplier<String> informationSupplier) {
        // do nothing
    }
}
