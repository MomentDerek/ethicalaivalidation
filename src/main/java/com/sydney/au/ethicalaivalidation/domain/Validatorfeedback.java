package com.sydney.au.ethicalaivalidation.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author: Xin Lin on 11/2/2020
 * @package: com.sydney.au.ethicalaivalidation.domain
 * @version: 1.0
 * <b>Description:</b>
 * <p></p>
 */
@Entity
public class Validatorfeedback {
    private int id;
    private int validatorid;
    private int projectid;
    private Timestamp sendtime;
    private int checkindex;

    public Validatorfeedback(int validatorid, int projectid, Timestamp sendtime, int checkindex) {
        this.validatorid = validatorid;
        this.projectid = projectid;
        this.sendtime = sendtime;
        this.checkindex = checkindex;
    }

    public Validatorfeedback() {
    }

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "validatorid", nullable = false)
    public int getValidatorid() {
        return validatorid;
    }

    public void setValidatorid(int validatorid) {
        this.validatorid = validatorid;
    }

    @Basic
    @Column(name = "projectid", nullable = false)
    public int getProjectid() {
        return projectid;
    }

    public void setProjectid(int projectid) {
        this.projectid = projectid;
    }

    @Basic
    @Column(name = "sendtime", nullable = false)
    public Timestamp getSendtime() {
        return sendtime;
    }

    public void setSendtime(Timestamp sendtime) {
        this.sendtime = sendtime;
    }

    @Basic
    @Column(name = "checkindex", nullable = false)
    public int getCheckindex() {
        return checkindex;
    }

    public void setCheckindex(int checkindex) {
        this.checkindex = checkindex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Validatorfeedback that = (Validatorfeedback) o;
        return id == that.id &&
                validatorid == that.validatorid &&
                projectid == that.projectid &&
                checkindex == that.checkindex &&
                Objects.equals(sendtime, that.sendtime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, validatorid, projectid, sendtime, checkindex);
    }
}
