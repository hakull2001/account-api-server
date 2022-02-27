package com.example.demo.specification;

import javax.persistence.criteria.JoinType;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class JoinCriteria {
    private SearchOperation searchOperation;
    private String joinColumnName;
    private String key;
    private Object value;
    private JoinType joinType;
}