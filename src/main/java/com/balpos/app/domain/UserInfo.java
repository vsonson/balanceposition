package com.balpos.app.domain;

import com.balpos.app.domain.enumeration.UserStatus;
import com.balpos.app.domain.enumeration.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * User is a default entity in a Stack Stack monolithic app and API gateway service and therefore cannot be modified in order to add a new relationship, fields etc.  Define a UserData entity so that the name does not collide with the User entity
 */
@ApiModel(description = "User is a default entity in a Stack Stack monolithic app and API gateway service and therefore cannot be modified in order to add a new relationship, fields etc.  Define a UserData entity so that the name does not collide with the User entity")
@Entity
@Table(name = "user_info")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1644158041216286959L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "userstatus")
    private UserStatus userstatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private UserType userType;

    @Column(name = "phone")
    private String phone;

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

    @Column(name = "education_level")
    @Getter
    @Setter
    @Accessors(chain = true)
    private String educationLevel;

    @Column(name = "primary_sport")
    @Getter
    @Setter
    @Accessors(chain = true)
    private String primarySport;

    @Lob
    @Column(name = "profile_pic")
    private byte[] profilePic;

    @Column(name = "profile_pic_content_type")
    private String profilePicContentType;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "gender")
    private String gender;

    @Column(name = "year_in_college")
    private String yearInCollege;

    @Column(name = "college_division")
    private String collegeDivision;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "state_code")
    private String stateCode;

    @Column(name = "last_quote_date")
    private LocalDate lastQuoteDate;

    @Column(name = "last_quote_id")
    private Long lastQuoteId;

    @OneToOne
    @JoinColumn(unique = true, insertable = false, updatable = false)
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
    private Set<UserNotification> userNotifications = new HashSet<>();

    @OneToMany(mappedBy = "userInfo")
    @JsonIgnore
    private Set<WellnessHistory> wellnessHistories = new HashSet<>();

    @OneToMany(mappedBy = "userInfo")
    @JsonIgnore
    private Set<IncentiveHistory> incentiveHistories = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "user_info_quote_of_the_day",
        joinColumns = @JoinColumn(name = "user_infos_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "quote_of_the_days_id", referencedColumnName = "id"))
    private Set<QuoteOfTheDay> quoteOfTheDays = new HashSet<>();

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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public UserInfo dateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public UserInfo gender(String gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getYearInCollege() {
        return yearInCollege;
    }

    public UserInfo yearInCollege(String yearInCollege) {
        this.yearInCollege = yearInCollege;
        return this;
    }

    public void setYearInCollege(String yearInCollege) {
        this.yearInCollege = yearInCollege;
    }

    public String getCollegeDivision() {
        return collegeDivision;
    }

    public UserInfo collegeDivision(String collegeDivision) {
        this.collegeDivision = collegeDivision;
        return this;
    }

    public void setCollegeDivision(String collegeDivision) {
        this.collegeDivision = collegeDivision;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public UserInfo countryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getStateCode() {
        return stateCode;
    }

    public UserInfo stateCode(String stateCode) {
        this.stateCode = stateCode;
        return this;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public LocalDate getLastQuoteDate() {
        return lastQuoteDate;
    }

    public UserInfo lastQuoteDate(LocalDate lastQuoteDate) {
        this.lastQuoteDate = lastQuoteDate;
        return this;
    }

    public void setLastQuoteDate(LocalDate lastQuoteDate) {
        this.lastQuoteDate = lastQuoteDate;
    }

    public Long getLastQuoteId() {
        return lastQuoteId;
    }

    public UserInfo lastQuoteId(Long lastQuoteId) {
        this.lastQuoteId = lastQuoteId;
        return this;
    }

    public void setLastQuoteId(Long lastQuoteId) {
        this.lastQuoteId = lastQuoteId;
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

    public Set<QuoteOfTheDay> getQuoteOfTheDays() {
        return quoteOfTheDays;
    }

    public UserInfo quoteOfTheDays(Set<QuoteOfTheDay> quoteOfTheDays) {
        this.quoteOfTheDays = quoteOfTheDays;
        return this;
    }

    public UserInfo addQuoteOfTheDay(QuoteOfTheDay quoteOfTheDay) {
        this.quoteOfTheDays.add(quoteOfTheDay);
        quoteOfTheDay.getUserInfos().add(this);
        return this;
    }

    public UserInfo removeQuoteOfTheDay(QuoteOfTheDay quoteOfTheDay) {
        this.quoteOfTheDays.remove(quoteOfTheDay);
        quoteOfTheDay.getUserInfos().remove(this);
        return this;
    }

    public void setQuoteOfTheDays(Set<QuoteOfTheDay> quoteOfTheDays) {
        this.quoteOfTheDays = quoteOfTheDays;
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
            ", phone='" + getPhone() + "'" +
            ", address='" + getAddress() + "'" +
            ", address2='" + getAddress2() + "'" +
            ", city='" + getCity() + "'" +
            ", state='" + getState() + "'" +
            ", zip='" + getZip() + "'" +
            ", country='" + getCountry() + "'" +
            ", profilePic='" + getProfilePic() + "'" +
            ", profilePicContentType='" + profilePicContentType + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", gender='" + getGender() + "'" +
            ", yearInCollege='" + getYearInCollege() + "'" +
            ", collegeDivision='" + getCollegeDivision() + "'" +
            ", countryCode='" + getCountryCode() + "'" +
            ", stateCode='" + getStateCode() + "'" +
            ", lastQuoteDate='" + getLastQuoteDate() + "'" +
            ", lastQuoteId='" + getLastQuoteId() + "'" +
            "}";
    }
}
