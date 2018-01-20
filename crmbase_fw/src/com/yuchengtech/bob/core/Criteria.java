package com.yuchengtech.bob.core;

import java.io.Serializable;

/**
 * 查询条件类
 */
public class Criteria implements Serializable {

    private static final long serialVersionUID = 7791270116455286938L;
    
    /** 列名 */
    private String column;
    /** 操作符 */
    private CriteriaOperator operator = CriteriaOperator.NULL;
    /** 操作数1 */
    private String comparison1 = "";
    /** 操作数2 */
    private String comparison2 = "";
    /** 组合方式 ，只能是AND或者OR */
    private CriteriaComposite composite;
    /** 组合查询 */
    private Criteria another;
    /** 是否在该查询条件前加NOT */
    private boolean isNotLogical;
    /** 该查询条件是否需要括号 */
    private boolean isInnerBracketsRequired;
    /** 该查询组合条件是否需要括号 */
    private boolean isOuterBracketsRequired;
    

    /** 将查询条件转换为字符串 */
    @Override
    public String toString() {       
        StringBuilder builder = new StringBuilder();
        if (column != null) {
            builder.append(column);
            builder.append(" ");
            builder.append(operator.toString());
            switch (operator) {
            case NULL:
                break;
            case NOTNULL:
                break;
            case LIKE:
                builder.append(" %");
                builder.append(comparison1);
                builder.append("%");
                break;
            case BETWEEN:
                builder.append(" ");
                builder.append(comparison1);
                builder.append(" AND ");
                builder.append(comparison2);
                break;
            default:
                builder.append(" ");
                builder.append(comparison1);
            }            
        }
        if(isInnerBracketsRequired) {
            builder.insert(0, "(");
            builder.append(")");
        }
        if(another != null) {
            builder.append(" ");
            builder.append(composite.toString());
            builder.append(" ");
            builder.append(another.toString());
        }
        if(isOuterBracketsRequired) {
            builder.insert(0, "(");
            builder.append(")");
        }
        if(isNotLogical) {
            builder.insert(0, "NOT ");
        }
        return builder.toString();
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public CriteriaOperator getOperator() {
        return operator;
    }

    public void setOperator(CriteriaOperator operator) {
        this.operator = operator;
    }

    public String getComparison1() {
        return comparison1;
    }

    public void setComparison1(String comparison1) {
        this.comparison1 = comparison1;
    }

    public String getComparison2() {
        return comparison2;
    }

    public void setComparison2(String comparison2) {
        this.comparison2 = comparison2;
    }

    public CriteriaComposite getComposite() {
        return composite;
    }

    public void setComposite(CriteriaComposite composite) {
        this.composite = composite;
    }

    public Criteria getAnother() {
        return another;
    }

    public void setAnother(Criteria another) {
        this.another = another;
    }

    public boolean isNotLogical() {
        return isNotLogical;
    }

    public void setNotLogical(boolean isNotLogical) {
        this.isNotLogical = isNotLogical;
    }

    public boolean isInnerBracketsRequired() {
        return isInnerBracketsRequired;
    }

    public void setInnerBracketsRequired(boolean isInnerBracketsRequired) {
        this.isInnerBracketsRequired = isInnerBracketsRequired;
    }

    public boolean isOuterBracketsRequired() {
        return isOuterBracketsRequired;
    }

    public void setOuterBracketsRequired(boolean isOuterBracketsRequired) {
        this.isOuterBracketsRequired = isOuterBracketsRequired;
    }

}
