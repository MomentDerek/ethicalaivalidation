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
public class Questionfeedback {
    private int id;
    private int projectid;
    private int validatorid;
    private int subquesid;
    private int createdindex;
    private String content;
    private Timestamp feedbacktime;

    public Questionfeedback() {
    }

    public Questionfeedback(int projectid, int validatorid, int subquesid, int createdindex, String content, Timestamp feedbacktime) {
        this.projectid = projectid;
        this.validatorid = validatorid;
        this.subquesid = subquesid;
        this.createdindex = createdindex;
        this.content = content;
        this.feedbacktime = feedbacktime;
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
    @Column(name = "createdindex", nullable = false)
    public int getCreatedindex() {
        return createdindex;
    }

    public void setCreatedindex(int createdindex) {
        this.createdindex = createdindex;
    }

    @Basic
    @Column(name = "content", nullable = false, length = -1)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "feedbacktime", nullable = false)
    public Timestamp getFeedbacktime() {
        return feedbacktime;
    }

    public void setFeedbacktime(Timestamp feedbacktime) {
        this.feedbacktime = feedbacktime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Questionfeedback that = (Questionfeedback) o;
        return id == that.id &&
                projectid == that.projectid &&
                validatorid == that.validatorid &&
                subquesid == that.subquesid &&
                createdindex == that.createdindex &&
                Objects.equals(content, that.content) &&
                Objects.equals(feedbacktime, that.feedbacktime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, projectid, validatorid, subquesid, createdindex, content, feedbacktime);
    }
}
