package com.sydney.au.ethicalaivalidation.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

/**
 * @author: Xin Lin on 11/2/2020
 * @package: com.sydney.au.ethicalaivalidation.domain
 * @version: 1.0
 * <b>Description:</b>
 * <p></p>
 */
@Entity
public class Questionstatus {
    private int id;
    private int projectid;
    private int validatorid;
    private int subquesid;
    private int status;

    public Questionstatus() {
    }

    public Questionstatus(int projectid, int validatorid, int subquesid, int status) {
        this.projectid = projectid;
        this.validatorid = validatorid;
        this.subquesid = subquesid;
        this.status = status;
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
    @Column(name = "projectid", nullable = false)
    public int getProjectid() {
        return projectid;
    }

    public void setProjectid(int projectid) {
        this.projectid = projectid;
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
    @Column(name = "subquesid", nullable = false)
    public int getSubquesid() {
        return subquesid;
    }

    public void setSubquesid(int subquesid) {
        this.subquesid = subquesid;
    }

    @Basic
    @Column(name = "status", nullable = false)
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Questionstatus that = (Questionstatus) o;
        return id == that.id &&
                projectid == that.projectid &&
                validatorid == that.validatorid &&
                subquesid == that.subquesid &&
                status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, projectid, validatorid, subquesid, status);
    }
}
