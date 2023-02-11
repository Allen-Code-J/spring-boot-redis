package com.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.model.Plan;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import com.repo.PlanRep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisPool;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@Slf4j
public class itemController {
    @Autowired
    PlanRep planRep;
    Map<String, Plan> map = new HashMap<>();
    private static JedisPool jedisPool = new JedisPool("localhost", 6379);

    @PostMapping("/plan")
    public String validateJson(@RequestBody String jsonData) throws JsonProcessingException {
        InputStream resourceAsStream = itemController.class.getClassLoader().getResourceAsStream("src/main/resources/schema.json");
        JsonSchema schema = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4).getSchema(resourceAsStream);

        ObjectMapper om = new ObjectMapper();
        om.setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE);
        JsonNode jsonNode = om.readTree(jsonData);

        Set<ValidationMessage> error = schema.validate(jsonNode);
        String err = "";
        for (ValidationMessage e : error) {
            log.error("Validation error:{}", error);
            err += e.toString();
        }
        if (err.length() == 0) {
            Plan re = om.readValue(jsonData, Plan.class);
            return re.getObjectId();
        }
        return "Error!";
    }

    @GetMapping("/{objectId}")
    public Object getItem(@PathVariable String objectId) {

        return map.get(objectId);
    }

    @DeleteMapping("/{objectId}")
    public String deleteItem(@PathVariable String objectId) {
        if (map.containsKey(objectId)) {
            planRep.deletePlan(objectId);
            map.remove(objectId);
            return "Delete Successful!";
        } else {
            return "Failed to delete!";
        }
    }
}

