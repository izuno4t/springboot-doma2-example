package com.example.doma.jdbc;

import org.seasar.doma.jdbc.UnknownColumnHandler;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.query.Query;

public final class UnknownColumnIgnoreHandler implements UnknownColumnHandler {

  @Override
  public void handle(Query query, EntityType<?> entityType, String unknownColumnName) {
    // Ignore unknown columns - no action needed
  }
}
