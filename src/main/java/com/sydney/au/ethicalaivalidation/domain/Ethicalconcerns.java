package com.sydney.au.ethicalaivalidation.domain;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author: Xin Lin on 11/2/2020
 * @package: com.sydney.au.ethicalaivalidation.domain
 * @version: 1.0
 * <b>Description:</b>
 * <p></p>
 */
@Entity
public class Ethicalconcerns {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private int projectid;
    private int subquesid;
    private int questionid;
    private String answer;
    private Integer points;
    private int finished;

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
    @Column(name = "subquesid", nullable = false)
    public int getSubquesid() {
        return subquesid;
    }

    public void setSubquesid(int subquesid) {
        this.subquesid = subquesid;
    }

    @Basic
    @Column(name = "questionid", nullable = false)
    public int getQuestionid() {
        return questionid;
    }

    public void setQuestionid(int questionid) {
        this.questionid = questionid;
    }

    @Basic
    @Column(name = "answer", nullable = true, length = -1)
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Basic
    @Column(name = "points", nullable = true)
    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    @Basic
    @Column(name = "finished", nullable = false)
    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ethicalconcerns that = (Ethicalconcerns) o;
        return id == that.id &&
                projectid == that.projectid &&
                subquesid == that.subquesid &&
                questionid == that.questionid &&
                finished == that.finished &&
                Objects.equals(answer, that.answer) &&
                Objects.equals(points, that.points);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, projectid, subquesid, questionid, answer, points, finished);
    }
}
