package com.example.JsonNeo.service;

import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import java.util.Map;


import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;
import org.neo4j.driver.Result;
import java.util.Arrays;

@Service
public class CypherQueryService {

    private final Driver neo4jDriver;
    private final Neo4jClient neo4jClient;

    public CypherQueryService(Neo4jClient neo4jClient, Driver neo4jDriver) {
        this.neo4jDriver = neo4jDriver;
        this.neo4jClient = neo4jClient;
    }
    
   

    public Object executeQuery(String cypherQuery, Map<String, Object> parameters) {
        try {
            return neo4jClient.query(cypherQuery)
                    .fetch()
                    .all();
        } catch (Exception e) {
            // Handle errors
            throw new RuntimeException("Failed to execute Cypher query: " + e.getMessage());
        }}

    public String executeQuery(String queries) {
        // Split the input string by semicolon, trimming extra whitespace
        String[] queryArray = queries.split("(?<=;)");
        
        // Use StringBuilder to log results
        StringBuilder results = new StringBuilder();
        
        try (Session session = neo4jDriver.session()) {
            session.executeWrite(tx -> {
                for (String query : queryArray) {
                    query = query.trim();
                    if (!query.isEmpty()) {
                        try {
                            Result result = tx.run(query);
                            results.append("Query executed successfully: ").append(query).append("\n");
                        } catch (Exception e) {
                            results.append("Error executing query: ").append(query).append(" - ").append(e.getMessage()).append("\n");
                            // Optional: throw exception here if you want to rollback all
                        }
                    }
                }
                return null;
            });
        }
        return results.toString();
    }
    
    public String getRelationshipBetweenNodes(String nodeA, String nodeB) {
        String query = "MATCH (a {name: $nodeA})-[r]-(b {name: $nodeB}) " +
                       "RETURN type(r) AS relationship, properties(r) AS details";
        try (Session session = neo4jDriver.session()) {
            return session.executeRead(tx -> {
                Result result = tx.run(query, 
                    Map.of("nodeA", nodeA, "nodeB", nodeB));
                StringBuilder response = new StringBuilder();
                while (result.hasNext()) {
                    org.neo4j.driver.Record record = result.next();
                    response.append(nodeA +" ").append(record.get("relationship").asString())
                            .append(", Details: ").append(record.get("details").asString())
                            		.append(" "+ nodeB +" ").append("\n");
                }
                return response.toString().isEmpty() ? "No relationship found" : response.toString();
            });
        }
    }

}


