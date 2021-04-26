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
public class Projects {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id = 0;

    private String projectname;
    private String description;
    private Timestamp createdtime;
    private int status;
    private Timestamp finishedtime;
    private Integer creatorid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "projectname", nullable = false, length = 100)
    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    @Basic
    @Column(name = "description", nullable = true, length = -1)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "createdtime", nullable = false)
    public Timestamp getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(Timestamp createdtime) {
        this.createdtime = createdtime;
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
    @Column(name = "finishedtime", nullable = true)
    public Timestamp getFinishedtime() {
        return finishedtime;
    }

    public void setFinishedtime(Timestamp finishedtime) {
        this.finishedtime = finishedtime;
    }

    @Basic
    @Column(name = "creatorid", nullable = true)
    public Integer getCreatorid() {
        return creatorid;
    }

    public void setCreatorid(Integer creatorid) {
        this.creatorid = creatorid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Projects projects = (Projects) o;
        return id == projects.id &&
                status == projects.status &&
                Objects.equals(projectname, projects.projectname) &&
                Objects.equals(description, projects.description) &&
                Objects.equals(createdtime, projects.createdtime) &&
                Objects.equals(finishedtime, projects.finishedtime) &&
                Objects.equals(creatorid, projects.creatorid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, projectname, description, createdtime, status, finishedtime, creatorid);
    }
}
