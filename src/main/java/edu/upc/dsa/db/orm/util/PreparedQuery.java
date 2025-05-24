package edu.upc.dsa.db.orm.util;

import java.util.List;

public class PreparedQuery {
    public final String sql;
    public final List<String> columns;

    public PreparedQuery(String sql, List<String> columns) {
        this.sql = sql;
        this.columns = columns;
    }
}