package com.jieweifu.common.dbservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class Insert {
    private final static Logger logger = LoggerFactory.getLogger(Insert.class);

    private JdbcTemplate jdbcTemplate;
    private List<String> columns = new ArrayList<>();
    private String tableName = "";
    private MapSqlParameterSource parameters = new MapSqlParameterSource();

    Insert(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Insert into(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public <T> Insert from(Class<T> clazz) {
        this.tableName = DB.getTableNameByClass(clazz);
        return this;
    }

    public Insert set(Map<String, Object> columnValues) {
        columnValues.forEach((column, value) -> {
            columns.add(column);
            parameters.addValue(column, value);
        });
        return this;
    }

    public Insert set(String column, Object value) {
        columns.add(column);
        parameters.addValue(column, value);
        return this;
    }

    public <T> Insert save(T t) {
        Class<?> clazz = t.getClass();
        String tableName = clazz.getSimpleName();
        Entity entity = clazz.getAnnotation(Entity.class);
        if (entity != null && !entity.tableName().isEmpty()) {
            tableName = entity.tableName();
        }
        this.tableName = tableName;
        try {
            set(t);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return this;
    }

    private <T> Insert set(T t) throws IllegalAccessException {
        Class<?> clazz = t.getClass();
        for (Field f : clazz.getDeclaredFields()) {
            f.setAccessible(true);
            String dbName = f.getName();
            Column column = f.getAnnotation(Column.class);
            if (column != null && !column.primaryKey() && column.insert() && f.get(t) != null) {
                if (!column.columnName().isEmpty()) {
                    dbName = column.columnName();
                }
                set(dbName, f.get(t));
            }
            if (column == null && f.get(t) != null) {
                set(dbName, f.get(t));
            }
        }
        return this;
    }

    private String buildSQL() {
        List<String> columnReplaces = new ArrayList<>();
        this.columns.forEach(column -> columnReplaces.add(":" + column));

        String sql = "INSERT INTO " +
                tableName +
                "(" +
                "`" +
                String.join("`, `", columns) +
                "`" +
                ")" +
                " VALUES (" +
                String.join(", ", columnReplaces) +
                ")";

        DB.writeLog(logger, sql, parameters.getValues());

        return sql;
    }

    @Override
    public String toString() {
        return buildSQL();
    }

    public Integer execute() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(buildSQL(), parameters, keyHolder);
        return keyHolder.getKey().intValue();
    }
}
