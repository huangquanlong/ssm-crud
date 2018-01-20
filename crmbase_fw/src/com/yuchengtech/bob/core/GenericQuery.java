package com.yuchengtech.bob.core;

import java.util.List;

/**
 * 通用查询类
 */
public class GenericQuery {
    
    /** 查询条件 */
    private Criteria criteria;
    /** 子查询 */
    private GenericQuery subSelect;
    /** 多表关联查询 */
    private List<JoinTable> joinTables;
    /** 要查询的返回字段 */
    private String[] selectList;
    /** 查询主表名 */
    private String tableName;
    /** 查询分组条件 */
    private String[] groupBy;
    /** 查询排序条件 */
    private String[] orderBy;
    /** 主键字段 */
    private String primaryKeyColumn = "ID";
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT ");
        if (selectList == null) {
            builder.append("*");
        } else {
            for (int i = 0; i < selectList.length; i++) {
                if (i > 0) {
                    builder.append(", ");
                }
                builder.append(selectList[i]);
            }
        }
        builder.append(" FROM ");
        if (subSelect != null) {
            builder.append("(");
            builder.append(subSelect.toString());
            builder.append(") ");
        }
        if (tableName != null) {
            builder.append(tableName);
        }
        if (joinTables != null) {
            for (JoinTable joinTable : joinTables) {
                builder.append(" ");
                builder.append(joinTable.toString());
            }        
        }
        if (criteria != null) {
            builder.append(" WHERE ");
            builder.append(criteria.toString());            
        }
        if (groupBy != null) {
            for (int i = 0; i < groupBy.length; i++) {
                if (i > 0) {
                    builder.append(", ");
                } else {
                    builder.append(" GROUP BY ");
                }
                builder.append(groupBy[i].toString());
            }            
        }
        if (orderBy != null) {
            for (int i = 0; i < orderBy.length; i++) {
                if (i > 0) {
                    builder.append(", ");
                } else {
                    builder.append(" ORDER BY ");
                }
                builder.append(orderBy[i].toString());
            }            
        }
        return builder.toString();
    }

    public Criteria getCriteria() {
        return criteria;
    }

    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
    }

    public GenericQuery getSubSelect() {
        return subSelect;
    }

    public void setSubSelect(GenericQuery subSelect) {
        this.subSelect = subSelect;
    }

    public List<JoinTable> getJoinTables() {
        return joinTables;
    }

    public void setJoinTables(List<JoinTable> joinTables) {
        this.joinTables = joinTables;
    }

    public String[] getSelectList() {
        return selectList;
    }

    public void setSelectList(String[] selectList) {
        this.selectList = selectList;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String[] getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String[] groupBy) {
        this.groupBy = groupBy;
    }

    public String[] getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String[] orderBy) {
        this.orderBy = orderBy;
    }

    public String getPrimaryKeyColumn() {
        return primaryKeyColumn;
    }

    public void setPrimaryKeyColumn(String primaryKeyColumn) {
        this.primaryKeyColumn = primaryKeyColumn;
    }

}
