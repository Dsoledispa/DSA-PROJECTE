package edu.upc.dsa.db.orm.util;

import java.util.List;

public class InsertQuery {
    public final String sql;
    public final List<String> columns;

    public InsertQuery(String sql, List<String> columns) {
        this.sql = sql;
        this.columns = columns;
    }
}