package org.ametiste.utility.xmas.infrastructure.persistance.jdbc;

import org.ametiste.utility.xmas.domain.model.RawDataBox;
import org.ametiste.utility.xmas.domain.model.RelayResult;
import org.ametiste.utility.xmas.infrastructure.persistance.LoggingRepository;
import com.google.gson.Gson;
import org.ametiste.utils.common.jdbc.TableName;
import org.postgresql.util.PGobject;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Created by Daria on 24.04.2015.
 */
public class PgLoggingRepository implements LoggingRepository{

    private final TableName tableName;
    private TableName outboundTableName;
    private JdbcTemplate template;

    public PgLoggingRepository(DataSource source, TableName tableName, TableName outboundTableName) {
        this.tableName = tableName;
        this.outboundTableName = outboundTableName;
        this.template = new JdbcTemplate(source);

    }

    @Override
    public void saveOriginalMessage(RawDataBox data){

        PGobject jsonObject = new PGobject();

        jsonObject.setType("json");
        try {
            jsonObject.setValue(new Gson().toJson(data.getAsMap()));
        } catch (SQLException e) {
            throw new IllegalArgumentException("Incoming data cant be converted to json");
        }
        template.update("INSERT INTO " + tableName.getFullTableName()
                    + " values (?,?,?) ", data.getId(), System.currentTimeMillis(), jsonObject);


    }

    @Override
    public void saveTransferResult(RelayResult result) {

        PGobject jsonObject = new PGobject();

        jsonObject.setType("json");
        try {
            jsonObject.setValue(new Gson().toJson(result.getData()));
        } catch (SQLException e) {
            throw new IllegalArgumentException("Incoming data cant be converted to json");
        }

        template.update("INSERT INTO " + outboundTableName.getFullTableName()
                +"  values (?,?,?,?,?)", new Object[]{result.getId(), System.currentTimeMillis(), result.getRelayName(),
                result.getStatus().toString(), jsonObject});

    }
}
