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
public class Company {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private String companyname;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "companyname", nullable = false, length = 100)
    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return id == company.id &&
                Objects.equals(companyname, company.companyname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, companyname);
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", companyname='" + companyname + '\'' +
                '}';
    }
}
