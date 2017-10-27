package com.jieweifu.common.dbservice;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@SuppressWarnings("unused")
@Component
public class DB {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DB(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Select select() {
        return new Select(jdbcTemplate);
    }

    public Update update() {
        return new Update(jdbcTemplate);
    }

    public Delete delete() {
        return new Delete(jdbcTemplate);
    }

    public Insert insert() {
        return new Insert(jdbcTemplate);
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    static <T> String getTableNameByClass(Class<T> clazz) {
        String tableName = clazz.getSimpleName();
        Entity entity = clazz.getAnnotation(Entity.class);
        if (entity != null && !entity.tableName().isEmpty()) {
            tableName = entity.tableName();
        }
        return tableName;
    }

    static void writeLog(Logger logger, String sql, Map<String, Object> map) {
        if (logger.isDebugEnabled()) {
            logger.debug("==>  Preparing: " + sql);
            logger.debug("==>  Parameters: " + map.size());
            map.forEach((key, value) ->
                    logger.debug("     key: " + key + ", value: " + value)
            );
            logger.debug("==>  Parameters");
        }
    }
}
