package com.sydney.au.ethicalaivalidation.domain;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author: Xin Lin on 11/2/2020
 * @package: com.sydney.au.ethicalaivalidation.domain
 * @version: 1.0
 * <b>Description:</b>
 * <p>This class map to table "Users".</p>
 */
@Entity
public class Users {
    private int id;
    private String username;
    private String password;
    private String email;
    private int usertype;
    private String firstname;
    private String lastname;
    private int companyid;
    private String address1;
    private String address2;
    private String phone;
    private Timestamp createdtime;
    private int verifiedemail;
    private String passwordtoken;
    private String emailtoken;
    private String image;

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
    @Column(name = "username", nullable = false, length = 50)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "password", nullable = false, length = 100)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "email", nullable = false, length = 50)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "usertype", nullable = false)
    public int getUsertype() {
        return usertype;
    }

    public void setUsertype(int usertype) {
        this.usertype = usertype;
    }

    @Basic
    @Column(name = "firstname", nullable = true, length = 50)
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    @Basic
    @Column(name = "lastname", nullable = true, length = 50)
    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Basic
    @Column(name = "companyid", nullable = false)
    public int getCompanyid() {
        return companyid;
    }

    public void setCompanyid(int companyid) {
        this.companyid = companyid;
    }

    @Basic
    @Column(name = "address1", nullable = true, length = 50)
    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    @Basic
    @Column(name = "address2", nullable = true, length = 50)
    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    @Basic
    @Column(name = "phone", nullable = true, length = 50)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
    @Column(name = "verifiedemail", nullable = false)
    public int getVerifiedemail() {
        return verifiedemail;
    }

    public void setVerifiedemail(int verifiedemail) {
        this.verifiedemail = verifiedemail;
    }

    @Basic
    @Column(name = "passwordtoken", nullable = true, length = 200)
    public String getPasswordtoken() {
        return passwordtoken;
    }

    public void setPasswordtoken(String passwordtoken) {
        this.passwordtoken = passwordtoken;
    }

    @Basic
    @Column(name = "emailtoken", nullable = true, length = 200)
    public String getEmailtoken() {
        return emailtoken;
    }

    public void setEmailtoken(String emailtoken) {
        this.emailtoken = emailtoken;
    }

    @Basic
    @Column(name = "image", nullable = false, length = 50)
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Users users = (Users) o;
        return usertype == users.usertype &&
                companyid == users.companyid &&
                verifiedemail == users.verifiedemail &&
                Objects.equals(id, users.id) &&
                Objects.equals(username, users.username) &&
                Objects.equals(password, users.password) &&
                Objects.equals(email, users.email) &&
                Objects.equals(firstname, users.firstname) &&
                Objects.equals(lastname, users.lastname) &&
                Objects.equals(address1, users.address1) &&
                Objects.equals(address2, users.address2) &&
                Objects.equals(phone, users.phone) &&
                Objects.equals(createdtime, users.createdtime) &&
                Objects.equals(passwordtoken, users.passwordtoken) &&
                Objects.equals(emailtoken, users.emailtoken) &&
                Objects.equals(image, users.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, email, usertype, firstname, lastname, companyid, address1, address2, phone, createdtime, verifiedemail, passwordtoken, emailtoken, image);
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", usertype=" + usertype +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", companyid=" + companyid +
                ", address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", phone='" + phone + '\'' +
                ", createdtime=" + createdtime +
                ", verifiedemail=" + verifiedemail +
                ", passwordtoken='" + passwordtoken + '\'' +
                ", emailtoken='" + emailtoken + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
