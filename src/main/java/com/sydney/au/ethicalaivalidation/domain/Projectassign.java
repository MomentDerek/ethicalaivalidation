package com.sydney.au.ethicalaivalidation.domain;

import javax.persistence.*;
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
public class Projectassign {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private int projectid;
    private int supplierid;
    private Timestamp assigntime;
    private int locked;
    private Integer lockedquestion;
    private Timestamp lockedtime;
    private Timestamp updatetime;


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
    @Column(name = "supplierid", nullable = false)
    public int getSupplierid() {
        return supplierid;
    }

    public void setSupplierid(int supplierid) {
        this.supplierid = supplierid;
    }

    @Basic
    @Column(name = "assigntime", nullable = false)
    public Timestamp getAssigntime() {
        return assigntime;
    }

    public void setAssigntime(Timestamp assigntime) {
        this.assigntime = assigntime;
    }

    @Basic
    @Column(name = "locked", nullable = false)
    public int getLocked() {
        return locked;
    }

    public void setLocked(int locked) {
        this.locked = locked;
    }

    @Basic
    @Column(name = "lockedquestion", nullable = true)
    public Integer getLockedquestion() {
        return lockedquestion;
    }

    public void setLockedquestion(Integer lockedquestion) {
        this.lockedquestion = lockedquestion;
    }

    @Basic
    @Column(name = "lockedtime", nullable = true)
    public Timestamp getLockedtime() {
        return lockedtime;
    }

    public void setLockedtime(Timestamp lockedtime) {
        this.lockedtime = lockedtime;
    }

    @Basic
    @Column(name = "updatetime", nullable = true)
    public Timestamp getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Timestamp updatetime) {
        this.updatetime = updatetime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Projectassign that = (Projectassign) o;
        return id == that.id &&
                projectid == that.projectid &&
                supplierid == that.supplierid &&
                locked == that.locked &&
                Objects.equals(assigntime, that.assigntime) &&
                Objects.equals(lockedquestion, that.lockedquestion) &&
                Objects.equals(lockedtime, that.lockedtime) &&
                Objects.equals(updatetime, that.updatetime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, projectid, supplierid, assigntime, locked, lockedquestion, lockedtime, updatetime);
    }

    @Override
    public String toString() {
        return "Projectassign{" +
                "id=" + id +
                ", projectid=" + projectid +
                ", supplierid=" + supplierid +
                ", assigntime=" + assigntime +
                ", locked=" + locked +
                ", lockedquestion=" + lockedquestion +
                ", lockedtime=" + lockedtime +
                ", updatetime=" + updatetime +
                '}';
    }
}
