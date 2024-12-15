package com.batch.project.config;

import com.batch.project.dto.Attribute;
import com.batch.project.dto.InputData;
import org.springframework.batch.item.ItemProcessor;

import java.util.LinkedHashMap;
import java.util.Map;

public class OutputProcessor implements ItemProcessor<Map<String,Object>, Map<String,Object>> {

    private InputData inputData;

    public OutputProcessor(InputData inputData){
        this.inputData = inputData;
    }

    @Override
    public Map<String,Object> process(Map<String,Object> outputDTO) {
        Map<String,Object> processedOutput = new LinkedHashMap<>();
        for(Attribute attribute : inputData.getAttributes()){
            if(attribute != null && attribute.getDbColumn() != null){
                processedOutput.put(attribute.getDbColumn(), outputDTO.get(attribute.getDbColumn()));
            }
        }
        return processedOutput;
    }
}
