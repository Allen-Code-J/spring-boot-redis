package com.model;

import lombok.*;
@Data
public class Plan {
    private Object planCostShares;
    private Object[] linkedPlanServices;
    private String _org;
    private String objectId;
    private String objectType;
    private String planType;
    private String creationDate;
}