package com.yuchengtech.bob.core;

import java.util.List;

/**
 * 多表查询连接表类
 */
public class JoinTable {
    
    /** 子查询 */
    private GenericQuery subSelect;
    /** 多表之间的连接条件 */
    private List<JoinCondition> joinConditions;
    /** 多表之间的连接关系 */
    private JoinType joinType = JoinType.LEFT_JOIN;
    /** 关联表名 */
    String tableName;
    
    @Override
    public String toString() { 
        StringBuilder builder = new StringBuilder();
        builder.append(joinType.toString());
        builder.append(" ");
        if (subSelect != null) {
            builder.append("(");
            builder.append(subSelect.toString());
            builder.append(") ");
        }
        if (tableName != null) {
            builder.append(tableName);
            builder.append(" ");            
        }
        if (joinConditions != null) {
            for (int i = 0; i < joinConditions.size(); i++) {
                if (i > 0) {
                    builder.append(" AND ");
                } else {
                    builder.append("ON ");
                }
                builder.append(joinConditions.get(i).getLeftColumn());
                builder.append(" = ");
                builder.append(joinConditions.get(i).getRightColumn());
            }            
        }
        return builder.toString();
    }

    public GenericQuery getSubSelect() {
        return subSelect;
    }

    public void setSubSelect(GenericQuery subSelect) {
        this.subSelect = subSelect;
    }

    public List<JoinCondition> getJoinConditions() {
        return joinConditions;
    }

    public void setJoinConditions(List<JoinCondition> joinConditions) {
        this.joinConditions = joinConditions;
    }

    public JoinType getJoinType() {
        return joinType;
    }

    public void setJoinType(JoinType joinType) {
        this.joinType = joinType;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    
}
