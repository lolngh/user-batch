package com.batch.project.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class InputData {

    private String type;

    private String query;

    private String delimeter;

    @JsonProperty("file_name")
    private String fileName;

    @JsonProperty("file_path")
    private String filePath;

    @JsonProperty("file_type")
    private String fileType;

    private List<Attribute> attributes;

    @JsonProperty("batch_size")
    private int batchSize;

    @JsonProperty("additional_where_clause")
    private List<Clause> additionalWhereClauses;
}
