package com.balpos.app.web.rest.vm;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class UserInfoUserVM {
    public Long id;
    public String city;
    public Integer zip;
    public String country;
    public String countryCode;
    public String educationLevel;
    public String gender;
    public Long lastQuoteId;
    public LocalDate lastQuoteDate;
    public String primarySport;
    public String state;
    public String stateCode;
    public String userstatus;
    public String userType;
    public String phone;
    public String address;
    public String address2;
    public LocalDate dateOfBirth;
    public String collegeDivision;
    public String yearInCollege;
    public String profilePicContentType;
    public byte[] profilePic;

    @NotNull
    public String user; // represents the users email
}
