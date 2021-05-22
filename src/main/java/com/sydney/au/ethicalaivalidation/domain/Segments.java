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
public class Segments {
    private int id;
    private int principleid;
    private String segmentname;

    public Segments() {
    }

    public Segments(int principleid, String segmentname) {
        this.principleid = principleid;
        this.segmentname = segmentname;
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
    @Column(name = "principleid", nullable = false)
    public int getPrincipleid() {
        return principleid;
    }

    public void setPrincipleid(int principleid) {
        this.principleid = principleid;
    }

    @Basic
    @Column(name = "segmentname", nullable = false, length = 50)
    public String getSegmentname() {
        return segmentname;
    }

    public void setSegmentname(String segmentname) {
        this.segmentname = segmentname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Segments segments = (Segments) o;
        return id == segments.id &&
                principleid == segments.principleid &&
                Objects.equals(segmentname, segments.segmentname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, principleid, segmentname);
    }

    @Override
    public String toString() {
        return "Segments{" +
                "id=" + id +
                ", principleid=" + principleid +
                ", segmentname='" + segmentname + '\'' +
                '}';
    }
}
