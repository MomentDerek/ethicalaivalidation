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
public class Questions {
    private int id;
    private int segmentid;
    private String questioncontent;

    public Questions() {
    }

    public Questions(int segmentid, String questioncontent) {
        this.segmentid = segmentid;
        this.questioncontent = questioncontent;
    }

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    @Column(name = "questioncontent", nullable = false, length = -1)
    public String getQuestioncontent() {
        return questioncontent;
    }

    public void setQuestioncontent(String questioncontent) {
        this.questioncontent = questioncontent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Questions questions = (Questions) o;
        return id == questions.id &&
                segmentid == questions.segmentid &&
                Objects.equals(questioncontent, questions.questioncontent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, segmentid, questioncontent);
    }

    @Override
    public String toString() {
        return "Questions{" +
                "id=" + id +
                ", segmentid=" + segmentid +
                ", questioncontent='" + questioncontent + '\'' +
                '}';
    }
}
