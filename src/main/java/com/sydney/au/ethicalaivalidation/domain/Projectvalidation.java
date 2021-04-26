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
public class Projectvalidation {
    private int id;
    private int projectid;
    private int validatorid;
    private Timestamp assignedtime;
    private int status;
    private int checknumber;

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
    @Column(name = "assignedtime", nullable = false)
    public Timestamp getAssignedtime() {
        return assignedtime;
    }

    public void setAssignedtime(Timestamp assignedtime) {
        this.assignedtime = assignedtime;
    }

    @Basic
    @Column(name = "status", nullable = false)
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Basic
    @Column(name = "checknumber", nullable = false)
    public int getChecknumber() {
        return checknumber;
    }

    public void setChecknumber(int checknumber) {
        this.checknumber = checknumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Projectvalidation that = (Projectvalidation) o;
        return id == that.id &&
                projectid == that.projectid &&
                validatorid == that.validatorid &&
                status == that.status &&
                checknumber == that.checknumber &&
                Objects.equals(assignedtime, that.assignedtime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, projectid, validatorid, assignedtime, status, checknumber);
    }
}
