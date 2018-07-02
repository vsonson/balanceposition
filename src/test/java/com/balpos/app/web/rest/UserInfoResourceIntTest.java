package com.balpos.app.web.rest;

import com.balpos.app.BalancepositionApp;
import com.balpos.app.domain.UserInfo;
import com.balpos.app.domain.enumeration.UserStatus;
import com.balpos.app.domain.enumeration.UserType;
import com.balpos.app.repository.UserInfoRepository;
import com.balpos.app.service.UserInfoService;
import com.balpos.app.web.rest.errors.ExceptionTranslator;
import com.balpos.app.web.rest.mapper.UserInfoUserMapper;
import com.balpos.app.web.rest.vm.UserInfoUserVM;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.balpos.app.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the UserInfoResource REST controller.
 *
 * @see UserInfoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BalancepositionApp.class)
public class UserInfoResourceIntTest {

    private static final UserStatus DEFAULT_USERSTATUS = UserStatus.ACTIVE;
    private static final UserStatus UPDATED_USERSTATUS = UserStatus.INVITED;

    private static final UserType DEFAULT_USER_TYPE = UserType.STUDENTATHLETE;
    private static final UserType UPDATED_USER_TYPE = UserType.ADMIN;

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_2 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_2 = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ZIP = 1;
    private static final Integer UPDATED_ZIP = 2;

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_EDUCATIONLEVEL = "AAAAAAAAAA";
    private static final String UPDATED_EDUCATIONLEVEL = "BBBBBBBBBB";

    private static final String DEFAULT_PRIMARYSPORT = "AAAAAAAAAA";
    private static final String UPDATED_PRIMARYSPORT = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PROFILE_PIC = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PROFILE_PIC = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_PROFILE_PIC_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PROFILE_PIC_CONTENT_TYPE = "image/png";

    private static final LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BIRTH = LocalDate.now();

    private static final String DEFAULT_GENDER = "AAAAAAAAAA";
    private static final String UPDATED_GENDER = "BBBBBBBBBB";

    private static final String DEFAULT_YEAR_IN_COLLEGE = "AAAAAAAAAA";
    private static final String UPDATED_YEAR_IN_COLLEGE = "BBBBBBBBBB";

    private static final String DEFAULT_COLLEGE_DIVISION = "AAAAAAAAAA";
    private static final String UPDATED_COLLEGE_DIVISION = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_STATE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_STATE_CODE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_LAST_QUOTE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_QUOTE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_LAST_QUOTE_ID = 1L;
    private static final Long UPDATED_LAST_QUOTE_ID = 2L;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserInfoUserMapper userInfoUserMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUserInfoMockMvc;

    private UserInfo userInfo;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserInfo createEntity(EntityManager em) {
        UserInfo userInfo = new UserInfo()
            .userstatus(DEFAULT_USERSTATUS)
            .userType(DEFAULT_USER_TYPE)
            .phone(DEFAULT_PHONE)
            .address(DEFAULT_ADDRESS)
            .address2(DEFAULT_ADDRESS_2)
            .city(DEFAULT_CITY)
            .state(DEFAULT_STATE)
            .zip(DEFAULT_ZIP)
            .country(DEFAULT_COUNTRY)
            .setEducationLevel(DEFAULT_EDUCATIONLEVEL)
            .setPrimarySport(DEFAULT_PRIMARYSPORT)
            .profilePic(DEFAULT_PROFILE_PIC)
            .profilePicContentType(DEFAULT_PROFILE_PIC_CONTENT_TYPE)
            .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
            .gender(DEFAULT_GENDER)
            .yearInCollege(DEFAULT_YEAR_IN_COLLEGE)
            .collegeDivision(DEFAULT_COLLEGE_DIVISION)
            .countryCode(DEFAULT_COUNTRY_CODE)
            .stateCode(DEFAULT_STATE_CODE)
            .lastQuoteDate(DEFAULT_LAST_QUOTE_DATE)
            .lastQuoteId(DEFAULT_LAST_QUOTE_ID);
        return userInfo;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserInfoResource userInfoResource = new UserInfoResource(userInfoService, userInfoUserMapper);
        this.restUserInfoMockMvc = MockMvcBuilders.standaloneSetup(userInfoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        userInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserInfo() throws Exception {
        int databaseSizeBeforeCreate = userInfoRepository.findAll().size();

        UserInfoUserVM userInfoVM = userInfoUserMapper.toVm(userInfo);

        // Create the UserInfo
        restUserInfoMockMvc.perform(post("/api/user-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userInfoVM)))
            .andExpect(status().isCreated());

        // Validate the UserInfo in the database
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeCreate + 1);
        UserInfo testUserInfo = userInfoList.get(userInfoList.size() - 1);
        assertThat(testUserInfo.getUserstatus()).isEqualTo(DEFAULT_USERSTATUS);
        assertThat(testUserInfo.getUserType()).isEqualTo(DEFAULT_USER_TYPE);
        assertThat(testUserInfo.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testUserInfo.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testUserInfo.getAddress2()).isEqualTo(DEFAULT_ADDRESS_2);
        assertThat(testUserInfo.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testUserInfo.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testUserInfo.getZip()).isEqualTo(DEFAULT_ZIP);
        assertThat(testUserInfo.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testUserInfo.getPrimarySport()).isEqualTo(DEFAULT_PRIMARYSPORT);
        assertThat(testUserInfo.getEducationLevel()).isEqualTo(DEFAULT_EDUCATIONLEVEL);
        assertThat(testUserInfo.getProfilePic()).isEqualTo(DEFAULT_PROFILE_PIC);
        assertThat(testUserInfo.getProfilePicContentType()).isEqualTo(DEFAULT_PROFILE_PIC_CONTENT_TYPE);
        assertThat(testUserInfo.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testUserInfo.getYearInCollege()).isEqualTo(DEFAULT_YEAR_IN_COLLEGE);
        assertThat(testUserInfo.getCollegeDivision()).isEqualTo(DEFAULT_COLLEGE_DIVISION);
        assertThat(testUserInfo.getCountryCode()).isEqualTo(DEFAULT_COUNTRY_CODE);
        assertThat(testUserInfo.getStateCode()).isEqualTo(DEFAULT_STATE_CODE);
        assertThat(testUserInfo.getLastQuoteDate()).isEqualTo(DEFAULT_LAST_QUOTE_DATE);
        assertThat(testUserInfo.getLastQuoteId()).isEqualTo(DEFAULT_LAST_QUOTE_ID);
    }

    @Test
    @Transactional
    public void createUserInfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userInfoRepository.findAll().size();

        // Create the UserInfo with an existing ID
        userInfo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserInfoMockMvc.perform(post("/api/user-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userInfo)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllUserInfos() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList
        restUserInfoMockMvc.perform(get("/api/user-infos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].userstatus").value(hasItem(DEFAULT_USERSTATUS.toString())))
            .andExpect(jsonPath("$.[*].userType").value(hasItem(DEFAULT_USER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].address2").value(hasItem(DEFAULT_ADDRESS_2.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].zip").value(hasItem(DEFAULT_ZIP)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].primarySport").value(hasItem(DEFAULT_PRIMARYSPORT.toString())))
            .andExpect(jsonPath("$.[*].educationLevel").value(hasItem(DEFAULT_EDUCATIONLEVEL.toString())))
            .andExpect(jsonPath("$.[*].profilePicContentType").value(hasItem(DEFAULT_PROFILE_PIC_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].profilePic").value(hasItem(Base64Utils.encodeToString(DEFAULT_PROFILE_PIC))))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].yearInCollege").value(hasItem(DEFAULT_YEAR_IN_COLLEGE.toString())))
            .andExpect(jsonPath("$.[*].collegeDivision").value(hasItem(DEFAULT_COLLEGE_DIVISION.toString())))
            .andExpect(jsonPath("$.[*].countryCode").value(hasItem(DEFAULT_COUNTRY_CODE.toString())))
            .andExpect(jsonPath("$.[*].stateCode").value(hasItem(DEFAULT_STATE_CODE.toString())))
            .andExpect(jsonPath("$.[*].lastQuoteDate").value(hasItem(DEFAULT_LAST_QUOTE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastQuoteId").value(hasItem(DEFAULT_LAST_QUOTE_ID.intValue())));
    }

    @Test
    @Transactional
    public void getUserInfo() throws Exception {
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get the userInfo
        restUserInfoMockMvc.perform(get("/api/user-infos/{id}", userInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userInfo.getId().intValue()))
            .andExpect(jsonPath("$.userstatus").value(DEFAULT_USERSTATUS.toString()))
            .andExpect(jsonPath("$.userType").value(DEFAULT_USER_TYPE.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.address2").value(DEFAULT_ADDRESS_2.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.zip").value(DEFAULT_ZIP))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()))
            .andExpect(jsonPath("$.primarySport").value(DEFAULT_PRIMARYSPORT.toString()))
            .andExpect(jsonPath("$.educationLevel").value(DEFAULT_EDUCATIONLEVEL.toString()))
            .andExpect(jsonPath("$.profilePicContentType").value(DEFAULT_PROFILE_PIC_CONTENT_TYPE))
            .andExpect(jsonPath("$.profilePic").value(Base64Utils.encodeToString(DEFAULT_PROFILE_PIC)))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.yearInCollege").value(DEFAULT_YEAR_IN_COLLEGE.toString()))
            .andExpect(jsonPath("$.collegeDivision").value(DEFAULT_COLLEGE_DIVISION.toString()))
            .andExpect(jsonPath("$.countryCode").value(DEFAULT_COUNTRY_CODE.toString()))
            .andExpect(jsonPath("$.stateCode").value(DEFAULT_STATE_CODE.toString()))
            .andExpect(jsonPath("$.lastQuoteDate").value(DEFAULT_LAST_QUOTE_DATE.toString()))
            .andExpect(jsonPath("$.lastQuoteId").value(DEFAULT_LAST_QUOTE_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingUserInfo() throws Exception {
        // Get the userInfo
        restUserInfoMockMvc.perform(get("/api/user-infos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserInfo() throws Exception {
        // Initialize the database
        userInfoService.save(userInfo);

        int databaseSizeBeforeUpdate = userInfoRepository.findAll().size();

        // Update the userInfo
        UserInfo updatedUserInfo = userInfoRepository.findOne(userInfo.getId());
        updatedUserInfo
            .userstatus(UPDATED_USERSTATUS)
            .userType(UPDATED_USER_TYPE)
            .phone(UPDATED_PHONE)
            .address(UPDATED_ADDRESS)
            .address2(UPDATED_ADDRESS_2)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zip(UPDATED_ZIP)
            .country(UPDATED_COUNTRY)
            .setPrimarySport(UPDATED_PRIMARYSPORT)
            .setEducationLevel(UPDATED_EDUCATIONLEVEL)
            .profilePic(UPDATED_PROFILE_PIC)
            .profilePicContentType(UPDATED_PROFILE_PIC_CONTENT_TYPE)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .gender(UPDATED_GENDER)
            .yearInCollege(UPDATED_YEAR_IN_COLLEGE)
            .collegeDivision(UPDATED_COLLEGE_DIVISION)
            .countryCode(UPDATED_COUNTRY_CODE)
            .stateCode(UPDATED_STATE_CODE)
            .lastQuoteDate(UPDATED_LAST_QUOTE_DATE)
            .lastQuoteId(UPDATED_LAST_QUOTE_ID);

        UserInfoUserVM updatedUserInfoVM = userInfoUserMapper.toVm(updatedUserInfo);

        restUserInfoMockMvc.perform(put("/api/user-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserInfoVM)))
            .andExpect(status().isOk());

        // Validate the UserInfo in the database
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeUpdate);
        UserInfo testUserInfo = userInfoList.get(userInfoList.size() - 1);
        assertThat(testUserInfo.getUserstatus()).isEqualTo(UPDATED_USERSTATUS);
        assertThat(testUserInfo.getUserType()).isEqualTo(UPDATED_USER_TYPE);
        assertThat(testUserInfo.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testUserInfo.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testUserInfo.getAddress2()).isEqualTo(UPDATED_ADDRESS_2);
        assertThat(testUserInfo.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testUserInfo.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testUserInfo.getZip()).isEqualTo(UPDATED_ZIP);
        assertThat(testUserInfo.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testUserInfo.getEducationLevel()).isEqualTo(UPDATED_EDUCATIONLEVEL);
        assertThat(testUserInfo.getPrimarySport()).isEqualTo(UPDATED_PRIMARYSPORT);
        assertThat(testUserInfo.getProfilePic()).isEqualTo(UPDATED_PROFILE_PIC);
        assertThat(testUserInfo.getProfilePicContentType()).isEqualTo(UPDATED_PROFILE_PIC_CONTENT_TYPE);
        assertThat(testUserInfo.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testUserInfo.getYearInCollege()).isEqualTo(UPDATED_YEAR_IN_COLLEGE);
        assertThat(testUserInfo.getCollegeDivision()).isEqualTo(UPDATED_COLLEGE_DIVISION);
        assertThat(testUserInfo.getCountryCode()).isEqualTo(UPDATED_COUNTRY_CODE);
        assertThat(testUserInfo.getStateCode()).isEqualTo(UPDATED_STATE_CODE);
        assertThat(testUserInfo.getLastQuoteDate()).isEqualTo(UPDATED_LAST_QUOTE_DATE);
        assertThat(testUserInfo.getLastQuoteId()).isEqualTo(UPDATED_LAST_QUOTE_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingUserInfo() throws Exception {
        int databaseSizeBeforeUpdate = userInfoRepository.findAll().size();

        // Create the UserInfo

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUserInfoMockMvc.perform(put("/api/user-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userInfo)))
            .andExpect(status().isCreated());

        // Validate the UserInfo in the database
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserInfo.class);
        UserInfo userInfo1 = new UserInfo();
        userInfo1.setId(1L);
        UserInfo userInfo2 = new UserInfo();
        userInfo2.setId(userInfo1.getId());
        assertThat(userInfo1).isEqualTo(userInfo2);
        userInfo2.setId(2L);
        assertThat(userInfo1).isNotEqualTo(userInfo2);
        userInfo1.setId(null);
        assertThat(userInfo1).isNotEqualTo(userInfo2);
    }
}
