package com.example.JsonNeo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.Map;

@Node
public class GenericNode {
    @Id
    private String id;
    private Map<String, Object> properties;

    public GenericNode(String id, Map<String, Object> properties) {
        this.id = id;
        this.properties = properties;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
}
