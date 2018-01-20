package com.yuchengtech.bob.core;

/**
 * 查询条件的操作符
 */
public enum CriteriaOperator {

    /** 大于 */
    GT,

    /** 大于等于 */
    GE,

    /** 小于 */
    LT,

    /** 小于等于 */
    LE,

    /** 等于 */
    EQ,

    /** 介于...和...之间 */
    BETWEEN,

    /** 模糊查询 */
    LIKE,

    /** IS NULL */
    NULL,

    /** IS NOT NULL */
    NOTNULL;

    @Override
    public String toString() {
        switch (this) {
        case GT:
            return ">";
        case GE:
            return ">=";
        case LT:
            return "<";
        case LE:
            return "<=";
        case EQ:
            return "=";
        case NULL:
            return "IS NULL";
        case NOTNULL:
            return "IS NOT NULL";
        default:
            return super.toString();
        }
    }

}
