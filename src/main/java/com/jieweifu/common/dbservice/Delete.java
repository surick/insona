package com.jieweifu.common.dbservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Delete {
    private final static Logger logger = LoggerFactory.getLogger(Delete.class);

    private JdbcTemplate jdbcTemplate;
    private List<String> where = new ArrayList<>();
    private Integer pageSize = null;
    private String tableName = "";
    private MapSqlParameterSource parameters = new MapSqlParameterSource();

    Delete(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Delete from(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public <T> Delete from(Class<T> clazz) {
        this.tableName = DB.getTableNameByClass(clazz);
        return this;
    }

    public Delete limit(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public Delete where(String where, Object... conditions) {
        for (Object condition : conditions) {
            String uuidKey = "deleteParam" + (this.parameters.getValues().size() + 1);
            where = where.replaceFirst("\\?", "(:" + uuidKey + ")");
            this.parameters.addValue(uuidKey, condition);
        }
        this.where.add(where);
        return this;
    }

    private String buildSQL() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DELETE FROM ").append(tableName);

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
