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
 * The persistent class for the OCRM_F_WP_USER_MODULE database table.
 * 
 */
@Entity
@Table(name = "OCRM_F_WP_USER_MODULE")
public class WorkspaceUserModule implements Serializable {

    private static final long serialVersionUID = 7421356456675863350L;

    @Id
    @GeneratedValue(generator = "CommonSequnce", strategy = GenerationType.TABLE)
    private long id;

    @Column(name = "COLUMN_SEQ")
    private BigDecimal COLUMN_SEQ;

    @Column(name = "LAYOUT_ID")
    private String LAYOUT_ID;

    @Column(name = "MODULE_ID")
    private String MODULE_ID;

    @Column(name = "MODULE_SEQ")
    private BigDecimal MODULE_SEQ;

    @Column(name = "USER_ID")
    private String USER_ID;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getCOLUMN_SEQ() {
        return COLUMN_SEQ;
    }

    public void setCOLUMN_SEQ(BigDecimal cOLUMN_SEQ) {
        COLUMN_SEQ = cOLUMN_SEQ;
    }

    public String getLAYOUT_ID() {
        return LAYOUT_ID;
    }

    public void setLAYOUT_ID(String lAYOUT_ID) {
        LAYOUT_ID = lAYOUT_ID;
    }

    public String getMODULE_ID() {
        return MODULE_ID;
    }

    public void setMODULE_ID(String mODULE_ID) {
        MODULE_ID = mODULE_ID;
    }

    public BigDecimal getMODULE_SEQ() {
        return MODULE_SEQ;
    }

    public void setMODULE_SEQ(BigDecimal mODULE_SEQ) {
        MODULE_SEQ = mODULE_SEQ;
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