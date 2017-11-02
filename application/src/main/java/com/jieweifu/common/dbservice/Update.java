package com.jieweifu.common.dbservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class Update {
    private final static Logger logger = LoggerFactory.getLogger(Update.class);

    private JdbcTemplate jdbcTemplate;
    private List<String> where = new ArrayList<>();
    private Integer pageSize = null;
    private List<String> columns = new ArrayList<>();
    private String tableName = "";
    private MapSqlParameterSource parameters = new MapSqlParameterSource();

    Update(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Update table(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public <T> Update table(Class<T> clazz) {
        this.tableName = DB.getTableNameByClass(clazz);
        return this;
    }

    public Update set(Map<String, Object> columnValues) {
        columnValues.forEach((column, value) -> {
            columns.add(column);
            parameters.addValue(column, value);
        });
        return this;
    }

    public Update set(String column, Object value) {
        columns.add(column);
        parameters.addValue(column, value);
        return this;
    }

    public <T> Update save(T t) {
        Class<?> clazz = t.getClass();
        String tableName = clazz.getSimpleName();
        Entity entity = clazz.getAnnotation(Entity.class);
        if (entity != null && !entity.tableName().isEmpty()) {
            tableName = entity.tableName();
        }
        this.tableName = tableName;
        set(t, true);
        return this;
    }

    public <T> Update set(T t, boolean needPrimary) {
        Class<?> clazz = t.getClass();
        for (Field f : clazz.getDeclaredFields()) {
            try {
                f.setAccessible(true);
                String dbName = f.getName();
                Column column = f.getAnnotation(Column.class);
                if (column != null && column.update() && f.get(t) != null && !column.primaryKey()) {
                    if (!column.columnName().isEmpty()) {
                        dbName = column.columnName();
                    }
                    set(dbName, f.get(t));
                }
                if (column == null && f.get(t) != null) {
                    set(dbName, f.get(t));
                }
                if (column != null && column.primaryKey() && needPrimary) {
                    where(dbName + " = ?", f.get(t));
                }
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }
        return this;
    }

    public Update limit(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public Update where(String where, Object... conditions) {
        for (Object condition : conditions) {
            String uuidKey = "updateParam" + (this.parameters.getValues().size() + 1);
            where = where.replaceFirst("\\?", "(:" + uuidKey + ")");
            this.parameters.addValue(uuidKey, condition);
        }
        this.where.add(where);
        return this;
    }

    private String buildSQL() {
        StringBuilder stringBuilder = new StringBuilder();

        List<String> columns = new ArrayList<>();
        this.columns.forEach(column -> columns.add("`" + column + "` =:" + column));

        stringBuilder.append("UPDATE ")
                .append(tableName)
                .append(" SET ")
                .append(String.join(", ", columns));

        if (this.where.size() > 0) {
            stringBuilder.append(" WHERE ").append(String.join(" AND ", this.where));
        }

        if (this.pageSize != null) {
            stringBuilder.append(" LIMIT ").append(pageSize);
        }

        String sql = stringBuilder.toString();
        DB.writeLog(logger, sql, parameters.getValues());

        return sql;
    }

    @Override
    public String toString() {
        return buildSQL();
    }

    public Integer execute() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        return namedParameterJdbcTemplate.update(buildSQL(), parameters);
    }
}
