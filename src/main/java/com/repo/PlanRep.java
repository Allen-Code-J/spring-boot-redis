package com.repo;

import com.model.Plan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PlanRep {
    public static final String HASH_KEY = "Key";
    @Autowired
    private RedisTemplate redisTemplate;

    public void createPlan(Plan plan) {

        redisTemplate.opsForHash().put(HASH_KEY, plan.getObjectId(), plan);
    }

    public Plan getPlanById(String objectId) {

        return (Plan) redisTemplate.opsForHash().get(HASH_KEY, objectId);
    }

    public void deletePlan(String objectId) {
        redisTemplate.opsForHash().delete(HASH_KEY, objectId);
    }


}