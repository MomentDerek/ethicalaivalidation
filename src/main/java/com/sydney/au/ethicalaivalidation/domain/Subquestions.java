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
public class Subquestions {
    private int id;
    private int questionid;
    private String content;
    private int questiontype;
    private Timestamp createdtime;
    private int level;

    public Subquestions() {
    }

    public Subquestions(int questionid, String content, int questiontype, Timestamp createdtime, int level) {
        this.questionid = questionid;
        this.content = content;
        this.questiontype = questiontype;
        this.createdtime = createdtime;
        this.level = level;
    }

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    @Column(name = "content", nullable = false, length = -1)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "questiontype", nullable = false)
    public int getQuestiontype() {
        return questiontype;
    }

    public void setQuestiontype(int questiontype) {
        this.questiontype = questiontype;
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
    @Column(name = "level", nullable = false)
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subquestions that = (Subquestions) o;
        return id == that.id &&
                questionid == that.questionid &&
                questiontype == that.questiontype &&
                level == that.level &&
                Objects.equals(content, that.content) &&
                Objects.equals(createdtime, that.createdtime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, questionid, content, questiontype, createdtime, level);
    }

    @Override
    public String toString() {
        return "Subquestions{" +
                "id=" + id +
                ", questionid=" + questionid +
                ", content='" + content + '\'' +
                ", questiontype=" + questiontype +
                ", createdtime=" + createdtime +
                ", level=" + level +
                '}';
    }
}
