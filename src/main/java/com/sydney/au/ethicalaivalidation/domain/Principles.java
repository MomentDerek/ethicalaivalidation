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
public class Principles {
    private int id;
    private String principlename;

    public Principles() {
    }

    public Principles(String principlename) {
        this.principlename = principlename;
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
    @Column(name = "principlename", nullable = false, length = 50)
    public String getPrinciplename() {
        return principlename;
    }

    public void setPrinciplename(String principlename) {
        this.principlename = principlename;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Principles that = (Principles) o;
        return id == that.id &&
                Objects.equals(principlename, that.principlename);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, principlename);
    }

    @Override
    public String toString() {
        return "Principles{" +
                "id=" + id +
                ", principlename='" + principlename + '\'' +
                '}';
    }
}
