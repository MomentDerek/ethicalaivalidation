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
public class Segmentsummary {
    private int id;
    private int projectid;
    private int validatorid;
    private int segmentid;
    private String summary;
    private Timestamp createdtime;

    public Segmentsummary() {
    }

    public Segmentsummary(int projectid, int validatorid, int segmentid, String summary, Timestamp createdtime) {
        this.projectid = projectid;
        this.validatorid = validatorid;
        this.segmentid = segmentid;
        this.summary = summary;
        this.createdtime = createdtime;
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
    @Column(name = "segmentid", nullable = false)
    public int getSegmentid() {
        return segmentid;
    }

    public void setSegmentid(int segmentid) {
        this.segmentid = segmentid;
    }

    @Basic
    @Column(name = "summary", nullable = false, length = -1)
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Basic
    @Column(name = "createdtime", nullable = false)
    public Timestamp getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(Timestamp createdtime) {
        this.createdtime = createdtime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Segmentsummary that = (Segmentsummary) o;
        return id == that.id &&
                projectid == that.projectid &&
                validatorid == that.validatorid &&
                segmentid == that.segmentid &&
                Objects.equals(summary, that.summary) &&
                Objects.equals(createdtime, that.createdtime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, projectid, validatorid, segmentid, summary, createdtime);
    }
}
