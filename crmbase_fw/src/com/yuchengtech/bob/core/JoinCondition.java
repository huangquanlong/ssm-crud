package com.yuchengtech.bob.core;

/** 
 * 多表查询连接条件类
 */
public class JoinCondition {

    /** 等号左边的列 */
    private String leftColumn;
    /** 等号右边的列 */
    private String rightColumn;

    public String getLeftColumn() {
        return leftColumn;
    }

    public void setLeftColumn(String leftColumn) {
        this.leftColumn = leftColumn;
    }

    public String getRightColumn() {
        return rightColumn;
    }

    public void setRightColumn(String ringtColumn) {
        this.rightColumn = ringtColumn;
    }

}
