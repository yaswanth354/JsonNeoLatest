package com.example.JsonNeo.Repository;

import com.example.JsonNeo.entity.GenericNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface GenericNodeRepository extends Neo4jRepository<GenericNode, String> {
}
