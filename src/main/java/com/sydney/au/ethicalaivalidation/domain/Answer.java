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
public class Answer {
    private int id;
    private int subquesid;
    private int answer;
    private int point;

    public Answer() {
    }

    public Answer(int subquesid, int answer, int point) {
        this.subquesid = subquesid;
        this.answer = answer;
        this.point = point;
    }

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    @Column(name = "answer", nullable = false)
    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    @Basic
    @Column(name = "point", nullable = false)
    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer1 = (Answer) o;
        return id == answer1.id &&
                subquesid == answer1.subquesid &&
                answer == answer1.answer &&
                point == answer1.point;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subquesid, answer, point);
    }
}
