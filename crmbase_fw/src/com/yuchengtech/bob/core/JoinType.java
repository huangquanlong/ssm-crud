package com.yuchengtech.bob.core;

/**
 * 多表查询连接类型
 */
public enum JoinType {

    /** 左链接 */
    LEFT_JOIN,

    /** 右链接 */
    RIGHT_JOIN,

    /** 内链接 */
    INNER_JOIN,

    /** 外链接 */
    OUTER_JOIN,

    /** 交叉链接（求笛卡儿积） */
    CROSS_JOIN;

    /** 将多表查询连接类型转换为字符串 */
    @Override
    public String toString() {
        switch (this) {
        case LEFT_JOIN:
            return "LEFT JOIN";
        case RIGHT_JOIN:
            return "RIGHT JOIN";
        case INNER_JOIN:
            return "INNER JOIN";
        case OUTER_JOIN:
            return "OUTER JOIN";
        case CROSS_JOIN:
            return "CROSS JOIN";
        }
        return null;
    }
    
}
