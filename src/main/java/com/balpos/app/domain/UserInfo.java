package com.balpos.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.balpos.app.domain.enumeration.UserStatus;

import com.balpos.app.domain.enumeration.UserType;

/**
 * User is a default entity in a JHipster monolithic app and API gateway service and therefore cannot be modified in order to add a new relationship, fields etc.  Define a UserData entity so that the name does not collide with the User entity
 */
@ApiModel(description = "User is a default entity in a JHipster monolithic app and API gateway service and therefore cannot be modified in order to add a new relationship, fields etc.  Define a UserData entity so that the name does not collide with the User entity")
@Entity
@Table(name = "user_info")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "userstatus")
    private UserStatus userstatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private UserType userType;

    @NotNull
    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "jhi_password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "fname")
    private String fname;

    @Column(name = "mname")
    private String mname;

    @Column(name = "lname")
    private String lname;

    @Column(name = "address")
    private String address;

    @Column(name = "address_2")
    private String address2;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "zip")
    private Integer zip;

    @Column(name = "country")
    private String country;

    @Lob
    @Column(name = "profile_pic")
    private byte[] profilePic;

    @Column(name = "profile_pic_content_type")
    private String profilePicContentType;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "networkOwner")
    @JsonIgnore
    private Set<NetworkMember> networkMembers = new HashSet<>();

    @OneToMany(mappedBy = "userInfo")
    @JsonIgnore
    private Set<MetricHistory> metricHistories = new HashSet<>();

    @OneToMany(mappedBy = "userInfo")
    @JsonIgnore
    private Set<ProgramHistory> programHistories = new HashSet<>();

    @OneToMany(mappedBy = "userInfo")
    @JsonIgnore
    private Set<Note> notes = new HashSet<>();

    @OneToMany(mappedBy = "userInfo")
    @JsonIgnore
    private Set<UserNotification> userNotifications = new HashSet<>();

    @OneToMany(mappedBy = "userInfo")
    @JsonIgnore
    private Set<WellnessHistory> wellnessHistories = new HashSet<>();

    @OneToMany(mappedBy = "userInfo")
    @JsonIgnore
    private Set<IncentiveHistory> incentiveHistories = new HashSet<>();

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserStatus getUserstatus() {
        return userstatus;
    }

    public UserInfo userstatus(UserStatus userstatus) {
        this.userstatus = userstatus;
        return this;
    }

    public void setUserstatus(UserStatus userstatus) {
        this.userstatus = userstatus;
    }

    public UserType getUserType() {
        return userType;
    }

    public UserInfo userType(UserType userType) {
        this.userType = userType;
        return this;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getUserName() {
        return userName;
    }

    public UserInfo userName(String userName) {
        this.userName = userName;
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public UserInfo password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public UserInfo email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public UserInfo phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFname() {
        return fname;
    }

    public UserInfo fname(String fname) {
        this.fname = fname;
        return this;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMname() {
        return mname;
    }

    public UserInfo mname(String mname) {
        this.mname = mname;
        return this;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getLname() {
        return lname;
    }

    public UserInfo lname(String lname) {
        this.lname = lname;
        return this;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getAddress() {
        return address;
    }

    public UserInfo address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public UserInfo address2(String address2) {
        this.address2 = address2;
        return this;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public UserInfo city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public UserInfo state(String state) {
        this.state = state;
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getZip() {
        return zip;
    }

    public UserInfo zip(Integer zip) {
        this.zip = zip;
        return this;
    }

    public void setZip(Integer zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public UserInfo country(String country) {
        this.country = country;
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public byte[] getProfilePic() {
        return profilePic;
    }

    public UserInfo profilePic(byte[] profilePic) {
        this.profilePic = profilePic;
        return this;
    }

    public void setProfilePic(byte[] profilePic) {
        this.profilePic = profilePic;
    }

    public String getProfilePicContentType() {
        return profilePicContentType;
    }

    public UserInfo profilePicContentType(String profilePicContentType) {
        this.profilePicContentType = profilePicContentType;
        return this;
    }

    public void setProfilePicContentType(String profilePicContentType) {
        this.profilePicContentType = profilePicContentType;
    }

    public User getUser() {
        return user;
    }

    public UserInfo user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<NetworkMember> getNetworkMembers() {
        return networkMembers;
    }

    public UserInfo networkMembers(Set<NetworkMember> networkMembers) {
        this.networkMembers = networkMembers;
        return this;
    }

    public UserInfo addNetworkMembers(NetworkMember networkMember) {
        this.networkMembers.add(networkMember);
        networkMember.setNetworkOwner(this);
        return this;
    }

    public UserInfo removeNetworkMembers(NetworkMember networkMember) {
        this.networkMembers.remove(networkMember);
        networkMember.setNetworkOwner(null);
        return this;
    }

    public void setNetworkMembers(Set<NetworkMember> networkMembers) {
        this.networkMembers = networkMembers;
    }

    public Set<MetricHistory> getMetricHistories() {
        return metricHistories;
    }

    public UserInfo metricHistories(Set<MetricHistory> metricHistories) {
        this.metricHistories = metricHistories;
        return this;
    }

    public UserInfo addMetricHistory(MetricHistory metricHistory) {
        this.metricHistories.add(metricHistory);
        metricHistory.setUserInfo(this);
        return this;
    }

    public UserInfo removeMetricHistory(MetricHistory metricHistory) {
        this.metricHistories.remove(metricHistory);
        metricHistory.setUserInfo(null);
        return this;
    }

    public void setMetricHistories(Set<MetricHistory> metricHistories) {
        this.metricHistories = metricHistories;
    }

    public Set<ProgramHistory> getProgramHistories() {
        return programHistories;
    }

    public UserInfo programHistories(Set<ProgramHistory> programHistories) {
        this.programHistories = programHistories;
        return this;
    }

    public UserInfo addProgramHistory(ProgramHistory programHistory) {
        this.programHistories.add(programHistory);
        programHistory.setUserInfo(this);
        return this;
    }

    public UserInfo removeProgramHistory(ProgramHistory programHistory) {
        this.programHistories.remove(programHistory);
        programHistory.setUserInfo(null);
        return this;
    }

    public void setProgramHistories(Set<ProgramHistory> programHistories) {
        this.programHistories = programHistories;
    }

    public Set<Note> getNotes() {
        return notes;
    }

    public UserInfo notes(Set<Note> notes) {
        this.notes = notes;
        return this;
    }

    public UserInfo addNotes(Note note) {
        this.notes.add(note);
        note.setUserInfo(this);
        return this;
    }

    public UserInfo removeNotes(Note note) {
        this.notes.remove(note);
        note.setUserInfo(null);
        return this;
    }

    public void setNotes(Set<Note> notes) {
        this.notes = notes;
    }

    public Set<UserNotification> getUserNotifications() {
        return userNotifications;
    }

    public UserInfo userNotifications(Set<UserNotification> userNotifications) {
        this.userNotifications = userNotifications;
        return this;
    }

    public UserInfo addUserNotifications(UserNotification userNotification) {
        this.userNotifications.add(userNotification);
        userNotification.setUserInfo(this);
        return this;
    }

    public UserInfo removeUserNotifications(UserNotification userNotification) {
        this.userNotifications.remove(userNotification);
        userNotification.setUserInfo(null);
        return this;
    }

    public void setUserNotifications(Set<UserNotification> userNotifications) {
        this.userNotifications = userNotifications;
    }

    public Set<WellnessHistory> getWellnessHistories() {
        return wellnessHistories;
    }

    public UserInfo wellnessHistories(Set<WellnessHistory> wellnessHistories) {
        this.wellnessHistories = wellnessHistories;
        return this;
    }

    public UserInfo addWellnessHistory(WellnessHistory wellnessHistory) {
        this.wellnessHistories.add(wellnessHistory);
        wellnessHistory.setUserInfo(this);
        return this;
    }

    public UserInfo removeWellnessHistory(WellnessHistory wellnessHistory) {
        this.wellnessHistories.remove(wellnessHistory);
        wellnessHistory.setUserInfo(null);
        return this;
    }

    public void setWellnessHistories(Set<WellnessHistory> wellnessHistories) {
        this.wellnessHistories = wellnessHistories;
    }

    public Set<IncentiveHistory> getIncentiveHistories() {
        return incentiveHistories;
    }

    public UserInfo incentiveHistories(Set<IncentiveHistory> incentiveHistories) {
        this.incentiveHistories = incentiveHistories;
        return this;
    }

    public UserInfo addIncentiveHistory(IncentiveHistory incentiveHistory) {
        this.incentiveHistories.add(incentiveHistory);
        incentiveHistory.setUserInfo(this);
        return this;
    }

    public UserInfo removeIncentiveHistory(IncentiveHistory incentiveHistory) {
        this.incentiveHistories.remove(incentiveHistory);
        incentiveHistory.setUserInfo(null);
        return this;
    }

    public void setIncentiveHistories(Set<IncentiveHistory> incentiveHistories) {
        this.incentiveHistories = incentiveHistories;
    }
    // jhipster-needle-entity-add-getters-setters - Jhipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserInfo userInfo = (UserInfo) o;
        if (userInfo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userInfo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserInfo{" +
            "id=" + getId() +
            ", userstatus='" + getUserstatus() + "'" +
            ", userType='" + getUserType() + "'" +
            ", userName='" + getUserName() + "'" +
            ", password='" + getPassword() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", fname='" + getFname() + "'" +
            ", mname='" + getMname() + "'" +
            ", lname='" + getLname() + "'" +
            ", address='" + getAddress() + "'" +
            ", address2='" + getAddress2() + "'" +
            ", city='" + getCity() + "'" +
            ", state='" + getState() + "'" +
            ", zip='" + getZip() + "'" +
            ", country='" + getCountry() + "'" +
            ", profilePic='" + getProfilePic() + "'" +
            ", profilePicContentType='" + profilePicContentType + "'" +
            "}";
    }
}
