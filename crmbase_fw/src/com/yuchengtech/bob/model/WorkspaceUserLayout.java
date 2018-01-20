package com.yuchengtech.bob.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The persistent class for the OCRM_WP_USER_LAYOUT database table.
 * 
 */
@Entity
@Table(name = "OCRM_F_WP_USER_LAYOUT")
public class WorkspaceUserLayout implements Serializable {

    private static final long serialVersionUID = 4257241125267355556L;

    @Id
    @GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
    private long id;

    @Column(name = "COLUMN_COUNT")
    private BigDecimal COLUMN_COUNT;

    @Column(name = "LAYOUT_ID")
    private String LAYOUT_ID;

    @Column(name = "USER_ID")
    private String USER_ID;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getCOLUMN_COUNT() {
        return COLUMN_COUNT;
    }

    public void setCOLUMN_COUNT(BigDecimal cOLUMN_COUNT) {
        COLUMN_COUNT = cOLUMN_COUNT;
    }

    public String getLAYOUT_ID() {
        return LAYOUT_ID;
    }

    public void setLAYOUT_ID(String lAYOUT_ID) {
        LAYOUT_ID = lAYOUT_ID;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String uSER_ID) {
        USER_ID = uSER_ID;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}