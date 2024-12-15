package com.batch.project.config;

import com.batch.project.dto.Clause;
import com.batch.project.dto.InputData;
import org.springframework.batch.item.ItemReader;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomReader implements ItemReader<Map<String,Object>> {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final List<Map<String, Object>> currentRecords;
    private AtomicInteger currentPoint = new AtomicInteger(0);

    public CustomReader(NamedParameterJdbcTemplate namedParameterJdbcTemplate, InputData inputData) {
        this.jdbcTemplate = namedParameterJdbcTemplate;
        MapSqlParameterSource params = new MapSqlParameterSource();
        for(Clause clause:inputData.getAdditionalWhereClauses()){
            params.addValue(clause.getCriteriaAttribute(), clause.getCriteriaValue());
        }
        this.currentRecords = namedParameterJdbcTemplate.query(inputData.getQuery(), params, (rs, rowNum) -> {
            Map<String,Object> row = new HashMap<>();
            int columnCount = rs.getMetaData().getColumnCount();
            for(int i=1; i<=columnCount; i++){
                row.put(rs.getMetaData().getColumnName(i).toLowerCase(Locale.ROOT), rs.getObject(i));
            }
            return row;
        });

    }

    @Override
    public Map<String, Object> read() {
        int index = currentPoint.getAndIncrement();
        if(currentRecords.size() ==0 || index >= currentRecords.size()){
            currentPoint.set(0);
            return null;
        }

        Map<String,Object> currentRecord = currentRecords.get(index);
        return currentRecord;
    }

}
