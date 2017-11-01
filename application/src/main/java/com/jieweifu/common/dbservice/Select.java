package com.jieweifu.common.dbservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.lang.reflect.Field;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class Select {

    private final static Logger logger = LoggerFactory.getLogger(Select.class);

    private List<String> columns = new ArrayList<>();
    private List<String> tables = new ArrayList<>();
    private List<String> join = new ArrayList<>();
    private List<String> innerJoin = new ArrayList<>();
    private List<String> outerJoin = new ArrayList<>();
    private List<String> leftOuterJoin = new ArrayList<>();
    private List<String> rightOuterJoin = new ArrayList<>();
    private List<String> where = new ArrayList<>();
    private MapSqlParameterSource parameters = new MapSqlParameterSource();
    private List<String> having = new ArrayList<>();
    private List<String> groupBy = new ArrayList<>();
    private List<String> orderBy = new ArrayList<>();
    private Integer pageIndex = null;
    private Integer pageSize = null;

    private JdbcTemplate jdbcTemplate;

    Select(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Select columns(String... columns) {
        this.columns.addAll(Arrays.asList(columns));
        return this;
    }

    public <T> Select from(Class<T> clazz) {
        this.tables.add(DB.getTableNameByClass(clazz));
        return this;
    }

    public <T> Select from(Class<T> clazz, String as) {
        this.tables.add(DB.getTableNameByClass(clazz) + " AS " + as);
        return this;
    }

    public Select from(String... tables) {
        this.tables.addAll(Arrays.asList(tables));
        return this;
    }

    public Select join(String... joins) {
        this.join.addAll(Arrays.asList(joins));
        return this;
    }

    public <T> Select join(Class<T> clazz, String as, String condition) {
        this.join.add(DB.getTableNameByClass(clazz) + " AS " + as + " ON " + condition);
        return this;
    }

    public Select innerJoin(String... joins) {
        this.innerJoin.addAll(Arrays.asList(joins));
        return this;
    }

    public <T> Select innerJoin(Class<T> clazz, String as, String condition) {
        this.innerJoin.add(DB.getTableNameByClass(clazz) + " AS " + as + " ON " + condition);
        return this;
    }

    public Select outerJoin(String... joins) {
        this.outerJoin.addAll(Arrays.asList(joins));
        return this;
    }

    public <T> Select outerJoin(Class<T> clazz, String as, String condition) {
        this.outerJoin.add(DB.getTableNameByClass(clazz) + " AS " + as + " ON " + condition);
        return this;
    }

    public Select leftOuterJoin(String... joins) {
        this.leftOuterJoin.addAll(Arrays.asList(joins));
        return this;
    }

    public <T> Select leftOuterJoin(Class<T> clazz, String as, String condition) {
        this.leftOuterJoin.add(DB.getTableNameByClass(clazz) + " AS " + as + " ON " + condition);
        return this;
    }

    public Select leftOuterJoin(Select select, String onCondition) {
        this.leftOuterJoin.add("(" + select.buildSQL(false) + ") " + onCondition);
        return this;
    }

    public Select rightOuterJoin(String... joins) {
        this.rightOuterJoin.addAll(Arrays.asList(joins));
        return this;
    }

    public <T> Select rightOuterJoin(Class<T> clazz, String as, String condition) {
        this.rightOuterJoin.add(DB.getTableNameByClass(clazz) + " AS " + as + " ON " + condition);
        return this;
    }

    public Select orderBy(String... orderBy) {
        this.orderBy.addAll(Arrays.asList(orderBy));
        return this;
    }

    public Select groupBy(String... groupBy) {
        this.groupBy.addAll(Arrays.asList(groupBy));
        return this;
    }

    public Select having(String... having) {
        this.having.addAll(Arrays.asList(having));
        return this;
    }

    public Select limit(Integer pageIndex, Integer pageSize) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        return this;
    }

    public Select where(String where, Object... conditions) {
        for (Object condition : conditions) {
            String uuidKey = "selectParam" + (this.parameters.getValues().size() + 1);
            where = where.replaceFirst("\\?", "(:" + uuidKey + ")");
            this.parameters.addValue(uuidKey, condition);
        }
        this.where.add(where);
        return this;
    }

    @Override
    public String toString() {
        return buildSQL(false);
    }

    private String buildSQL(boolean isTotal) {
        StringBuilder stringBuilder = new StringBuilder();
        String columns = " *";
        if (this.columns.size() > 0) {
            columns = " " + String.join(", ", this.columns);
        }
        if (isTotal) {
            columns = " COUNT(0)";
        }
        stringBuilder.append("SELECT")
                .append(columns)
                .append(" FROM ")
                .append(String.join(", ", this.tables));

        if (this.join.size() > 0) {
            stringBuilder.append(" JOIN ").append(String.join(" JOIN ", this.join));
        }

        if (this.innerJoin.size() > 0) {
            stringBuilder.append(" INNER JOIN ").append(String.join(" INNER JOIN ", this.innerJoin));
        }

        if (this.outerJoin.size() > 0) {
            stringBuilder.append(" OUTER JOIN ").append(String.join(" OUTER JOIN ", this.outerJoin));
        }

        if (this.leftOuterJoin.size() > 0) {
            stringBuilder.append(" LEFT OUTER JOIN ").append(String.join(" LEFT OUTER JOIN ", this.leftOuterJoin));
        }

        if (this.rightOuterJoin.size() > 0) {
            stringBuilder.append(" RIGHT OUTER JOIN ").append(String.join(" RIGHT OUTER JOIN ", this.rightOuterJoin));
        }

        if (this.where.size() > 0) {
            stringBuilder.append(" WHERE ").append(String.join(" AND ", this.where));
        }

        if (this.groupBy.size() > 0) {
            stringBuilder.append(" GROUP BY ").append(String.join(", ", this.groupBy));
        }
        if (this.having.size() > 0) {
            stringBuilder.append(" HAVING ").append(String.join(", ", this.having));
        }

        if (this.orderBy.size() > 0) {
            stringBuilder.append(" ORDER BY ").append(String.join(", ", this.orderBy));
        }

        if (this.pageIndex != null && !isTotal) {
            stringBuilder.append(" LIMIT ").append(pageIndex * pageSize).append(", ").append(pageSize);
        }

        String sql = stringBuilder.toString();
        DB.writeLog(logger, sql, parameters.getValues());

        return sql;
    }

    public Integer total() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        return namedParameterJdbcTemplate.queryForObject(buildSQL(true), parameters, Integer.class);
    }

    public <T> T queryForObject(Class<T> requiredType) {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        return namedParameterJdbcTemplate.queryForObject(buildSQL(false), parameters, requiredType);
    }

    public List<Map<String, Object>> queryForList() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        return namedParameterJdbcTemplate.queryForList(buildSQL(false), parameters);
    }

    public void queryForList(RowCallbackHandler rowCallbackHandler) {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        namedParameterJdbcTemplate.query(buildSQL(false), parameters, rowCallbackHandler);
    }

    public <T> T queryForList(ResultSetExtractor<T> resultSetExtractor) {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        return namedParameterJdbcTemplate.query(buildSQL(false), parameters, resultSetExtractor);
    }

    public <T> T queryForEntity(Class<T> clazz) {
        List<T> lists = this.queryForList(clazz);
        if (lists.size() > 0) {
            return lists.get(0);
        }
        return null;
    }

    public <T> List<T> queryForList(Class<T> clazz) {
        return this.queryForList(resultSet -> {
            List<T> lists = new ArrayList<>();
            while (resultSet.next()) {
                try {
                    T t = clazz.newInstance();
                    for (Field f : clazz.getDeclaredFields()) {
                        f.setAccessible(true);
                        Column column = f.getAnnotation(Column.class);
                        String dbName = f.getName();
                        if (column != null && !column.columnName().isEmpty()) {
                            dbName = column.columnName();
                        }
                        if(column == null || column.select()){
                            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                            int columns = resultSetMetaData.getColumnCount();
                            for (int x = 1; x <= columns; x++) {
                                if (dbName.equalsIgnoreCase(resultSetMetaData.getColumnName(x))) {
                                    f.set(t, resultSet.getObject(dbName));
                                }
                            }
                        }
                    }
                    lists.add(t);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return lists;
        });
    }
}
