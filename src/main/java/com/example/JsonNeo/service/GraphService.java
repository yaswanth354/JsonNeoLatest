package com.example.JsonNeo.service;

import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GraphService {
    private final Neo4jClient neo4jClient;

    public GraphService(Neo4jClient neo4jClient) {
        this.neo4jClient = neo4jClient;
    }

    @Transactional
    public void saveDynamicGraph(Map<String, Object> jsonInput) {
        // Process nodes
        for (String key : jsonInput.keySet()) {
            if (!key.equals("relationships")) {
                List<Map<String, Object>> nodes = (List<Map<String, Object>>) jsonInput.get(key);
                for (Map<String, Object> nodeData : nodes) {
                    String label = key.substring(0, 1).toUpperCase() + key.substring(1); // Capitalize label
                    String cypherQuery = String.format(
                            "MERGE (n:%s {id: $id}) " +
                                    "SET n += $properties",
                            label
                    );
                    nodeData.putIfAbsent("id", nodeData.getOrDefault("name", nodeData.hashCode())); // Ensure unique ID
                    neo4jClient.query(cypherQuery)
                            .bind(nodeData.get("id")).to("id")
                            .bind(nodeData).to("properties")
                            .run();
                }
            }
        }

        // Process relationships
        if (jsonInput.containsKey("relationships")) {
            List<Map<String, Object>> relationships = (List<Map<String, Object>>) jsonInput.get("relationships");
            for (Map<String, Object> relData : relationships) {
                String source = (String) relData.get("source");
                String target = (String) relData.get("target");
                String type = (String) relData.get("type");
                relData.remove("source");
                relData.remove("target");
                relData.remove("type");

                String cypherQuery = String.format(
                        "MATCH (a {id: $source}) OPTIONAL MATCH (b {id: $target}) "
                        + "WHERE b IS NOT NULL MERGE (a)-[r:%s]->(b) SET r += $properties",
                        type
                );

                neo4jClient.query(cypherQuery)
                        .bind(source).to("source")
                        .bind(target).to("target")
                        .bind(relData).to("properties")
                        .run();
            }
        }
    }
    
    public List<Map<String, Object>> getAllNodes() {
        String cypherQuery = "MATCH (n) RETURN n";
        return neo4jClient.query(cypherQuery)
            .fetchAs(Map.class)
            .mappedBy((typeSystem, record) -> {
                Map<String, Object> node = record.get("n").asNode().asMap();
                return node;
            })
            .all()
            .stream()
            .collect(Collectors.toList());
    }

}
