package com.yammer.dropwizard.logging;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * Space Ape Games
 */
public class LogstashParam {

    @NotNull
    @JsonProperty
    private String name;

    @NotNull
    @JsonProperty
    private String value;

    public LogstashParam(){}

    public LogstashParam(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
