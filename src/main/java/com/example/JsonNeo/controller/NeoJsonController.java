package com.example.JsonNeo.controller;
import com.example.JsonNeo.service.GraphService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/graph")
public class NeoJsonController {
        private final GraphService graphService;

        public NeoJsonController(GraphService graphService) {
            this.graphService = graphService;
        }

        @PostMapping("/create")
        public List<Map<String,Object>> createGraph(@RequestBody Map<String, Object> jsonInput) {
            graphService.saveDynamicGraph(jsonInput);
            
            return graphService.getAllNodes();
        }
    }


