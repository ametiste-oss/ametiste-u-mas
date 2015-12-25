package org.ametiste.utility.xmas.infrastructure.persistance.jdbc;

import org.ametiste.utils.common.jdbc.TableName;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Created by Daria on 27.04.2015.
 */
public class PgLoggingBootstrap {

    private final TableName tableName;
    private TableName outboundTableName;
    private final JdbcTemplate template;

    public PgLoggingBootstrap(DataSource dataSource, TableName tableName, TableName outboundTableName) {
        this.tableName = tableName;
        this.outboundTableName = outboundTableName;
        this.template = new JdbcTemplate(dataSource);
    }

    public void init() {

        this.template.execute("CREATE SCHEMA IF NOT EXISTS " + this.tableName.getDatabaseName());
        this.template
                .execute("create table IF NOT EXISTS "
                        + this.tableName.getFullTableName()
                        + " (log_id varchar(100) not null, log_time bigint not null, log_message JSON)");
        this.template
                .execute("create table IF NOT EXISTS "
                        + this.outboundTableName.getFullTableName()
                        + " (log_id varchar(100) not null, log_time bigint not null, log_relay_name varchar(50), log_status varchar(50), log_message JSON)");

    }
}
