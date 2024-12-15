package com.batch.project.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Clause {

    @JsonProperty("criteria_value")
    private Object criteriaValue;

    @JsonProperty("criteria_attribute")
    private String criteriaAttribute;

}
