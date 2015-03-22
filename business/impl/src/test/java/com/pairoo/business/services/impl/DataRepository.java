package com.pairoo.business.services.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.datazuul.framework.domain.Language;
import com.datazuul.framework.security.HashResult;
import com.datazuul.framework.security.HashUtil;
import com.datazuul.framework.util.RandomUtil;
import com.pairoo.business.DemoDataGenerator;
import com.pairoo.business.api.PersonProfileService;
import com.pairoo.business.api.UserProfileService;
import com.pairoo.domain.Appearance;
import com.pairoo.domain.LifeStyle;
import com.pairoo.domain.Membership;
import com.pairoo.domain.Message;
import com.pairoo.domain.SearchProfile;
import com.pairoo.domain.User;
import com.pairoo.domain.UserAccount;
import com.pairoo.domain.UserProfile;
import com.pairoo.domain.enums.EducationType;
import com.pairoo.domain.enums.Ethnicity;
import com.pairoo.domain.enums.FamilyStatusType;
import com.pairoo.domain.enums.MembershipStatus;
import com.pairoo.domain.enums.NumberOfKidsType;
import com.pairoo.domain.enums.PartnerType;
import com.pairoo.domain.enums.PartnershipType;
import com.pairoo.domain.enums.Religion;
import com.pairoo.domain.enums.SmokeType;

/**
 * Repository with generated demo data. Only data is set that doesn't need database access.
 *
 * @author Ralf Eichinger
 */
public class DataRepository implements InitializingBean {
    static final Logger LOGGER = LoggerFactory.getLogger(DataRepository.class);

    private User adam;
    private int count = 40;
    private User eva;
    private final List<Message> messagesList = new ArrayList<Message>();
    private final List<UserAccount> userAccountList = new ArrayList<UserAccount>();
    private final List<User> userList = new ArrayList<User>();
    private final PersonProfileService personProfileService = new PersonProfileServiceImpl();
    private final UserProfileService userProfileService = new UserProfileServiceImpl(null);

    @SuppressWarnings("unchecked")
    @Override
    public void afterPropertiesSet() throws Exception {
	LOGGER.info("Initialising " + count + " demo users.");

	for (int i = 0; i < count; i++) {

	    // ======================= User
	    final User user = new User();

	    // lastname
	    user.setLastname(RandomUtil.randomString(DemoDataGenerator.LASTNAMES));

	    // email
	    String email = user.getFirstname() + "." + user.getLastname() + i + "@" + "localhost";
	    email = email.toLowerCase();
	    user.setEmail(email);

	    final UserAccount ua = new UserAccount();
	    ua.setUser(user);

	    // password
	    HashResult hashResult = HashUtil.hash(DemoDataGenerator.PASSWORDS[0]);
	    ua.setPasswordSalt(hashResult.getSalt());
	    ua.setPassword(hashResult.getDigest());

	    // membership
	    final Membership membership = new Membership();
	    membership.setUserAccount(ua);
	    membership.setStartDate(new Date());
	    membership.setStatus(MembershipStatus.REGISTERED);

	    // last login
	    ua.setLastLogin(RandomUtil.randomDate(0, 1));

	    user.setUserAccount(ua);

	    userAccountList.add(ua);

	    // ======================= UserProfile
	    final UserProfile up = new UserProfile();

	    // birthdate
	    up.setBirthdate(userProfileService.getRandomBirthdate(personProfileService.getMinAge(),
		    personProfileService.getMaxAge()));

	    // appearance
	    final Appearance appearance = new Appearance();
	    appearance.setHeight(RandomUtil.randomInt(150, 210));
	    appearance.setWeight(RandomUtil.randomInt(60, 130));

	    // lifestyle
	    final LifeStyle lifeStyle = new LifeStyle();
	    up.setLifeStyle(lifeStyle);
	    lifeStyle.setSmokeType(RandomUtil.randomEnum(SmokeType.class, SmokeType.ANY));

	    // motto
	    up.setMotto(RandomUtil.randomString(DemoDataGenerator.MOTTOS));

	    // ethnicity
	    final Ethnicity[] ethnicities = Ethnicity.values();
	    Ethnicity ethnicity = null;
	    do {
		ethnicity = ethnicities[RandomUtil.randomInt(0, ethnicities.length - 1)];
	    } while (ethnicity == Ethnicity.ANY);
	    appearance.setEthnicity(ethnicity);
	    up.setAppearance(appearance);

	    // gender
	    final PartnerType partnerType = RandomUtil.randomEnum(PartnerType.class, PartnerType.ANY);
	    up.setPartnerType(partnerType);

	    // firstname
	    String firstname = null;
	    if (partnerType == PartnerType.MALE) {
		firstname = RandomUtil.randomString(DemoDataGenerator.FIRSTNAMES_MALE);
	    } else {
		firstname = RandomUtil.randomString(DemoDataGenerator.FIRSTNAMES_FEMALE);
	    }
	    user.setFirstname(firstname);

	    // pseudonym
	    ua.setUsername(firstname + i);

	    // family status
	    try {
		up.setFamilyStatus(RandomUtil.randomEnum(FamilyStatusType.class, FamilyStatusType.ANY));
	    } catch (final Exception e) {
		e.printStackTrace();
	    }

	    // kids
	    try {
		up.setNumberOfKidsType(RandomUtil.randomEnum(NumberOfKidsType.class, NumberOfKidsType.ANY));
	    } catch (final Exception e) {
		e.printStackTrace();
	    }

	    // mother language
	    try {
		up.setMotherLanguage(RandomUtil.randomEnum(Language.class, Language.ANY));
	    } catch (final Exception e) {
		e.printStackTrace();
	    }
	    ua.setPreferredLanguage(Language.ENGLISH);

	    // religion
	    try {
		up.setReligion(RandomUtil.randomEnum(Religion.class, Religion.ANY));
	    } catch (final Exception e) {
		e.printStackTrace();
	    }

	    user.setUserProfile(up);

	    // ======================= SearchProfile
	    final SearchProfile sp = new SearchProfile();

	    // education
	    final List<EducationType> educationTypes = new ArrayList<EducationType>();
	    educationTypes.add(EducationType.HIGH_SCHOOL);
	    educationTypes.add(EducationType.PHD_OR_DOCTORATE);
	    educationTypes.add(EducationType.UNIVERSITY);
	    sp.setEducationTypes(educationTypes);

	    // partnership type
	    try {
		sp.setPartnershipType(RandomUtil.randomEnum(PartnershipType.class));
	    } catch (final Exception e) {
		e.printStackTrace();
	    }

	    // age
	    sp.setMinAge(RandomUtil.randomInt(20, 25));
	    sp.setMaxAge(RandomUtil.randomInt(35, 70));

	    user.setSearchProfile(sp);

	    userList.add(user);
	}

	// adam
	// ======================= User
	adam = new User();
	// firstname
	adam.setFirstname("Adam");
	// lastname
	adam.setLastname("Mensch");
	// email
	String email = "ralf.eichinger@pixotec.de";
	email = email.toLowerCase();
	adam.setEmail(email);

	// ======================= UserAccount
	UserAccount ua = new UserAccount();
	ua.setUser(adam);

	// password
	HashResult hashResult = HashUtil.hash(DemoDataGenerator.PASSWORDS[0]);
	ua.setPasswordSalt(hashResult.getSalt());
	ua.setPassword(hashResult.getDigest());
	// pseudonym
	ua.setUsername("adam");
	// membership
	Membership membership = new Membership();
	membership.setUserAccount(ua);
	membership.setStatus(MembershipStatus.REGISTERED);
	final Calendar start = Calendar.getInstance();
	start.add(Calendar.YEAR, -1);
	membership.setStartDate(start.getTime());
	adam.setUserAccount(ua);
	// last login
	ua.setLastLogin(RandomUtil.randomDate(0, 1));

	// ======================= UserProfile
	UserProfile up = new UserProfile();
	// birthdate
	up.setBirthdate(userProfileService.getRandomBirthdate(45, 50));
	// appearance
	Appearance appearance = new Appearance();
	appearance.setHeight(RandomUtil.randomInt(150, 210));
	appearance.setWeight(RandomUtil.randomInt(60, 130));

	// motto
	up.setMotto(RandomUtil.randomString(DemoDataGenerator.MOTTOS));
	// ethnicity
	Ethnicity ethnicity = Ethnicity.CAUCASIAN_WHITE;
	appearance.setEthnicity(ethnicity);
	up.setAppearance(appearance);
	// gender
	up.setPartnerType(PartnerType.MALE);

	// family status
	up.setFamilyStatus(FamilyStatusType.SEPARATED);

	// kids
	up.setNumberOfKidsType(NumberOfKidsType.NONE);

	// mother language
	try {
	    up.setMotherLanguage(RandomUtil.randomEnum(Language.class));
	} catch (final Exception e) {
	    e.printStackTrace();
	}
	ua.setPreferredLanguage(Language.GERMAN);

	// religion
	try {
	    up.setReligion(RandomUtil.randomEnum(Religion.class));
	} catch (final Exception e) {
	    e.printStackTrace();
	}
	adam.setUserProfile(up);

	// ======================= SearchProfile
	SearchProfile sp = new SearchProfile();
	sp.setPartnerType(PartnerType.FEMALE);
	sp.setMinAge(28);
	sp.setMaxAge(40);
	// partnership type
	sp.setPartnershipType(PartnershipType.LOVE);

	final List<Ethnicity> ethnicities = new ArrayList();
	ethnicities.add(Ethnicity.ANY);
	sp.setEthnicities(ethnicities);

	adam.setSearchProfile(sp);

	// eva
	// ======================= User
	eva = new User();
	// firstname
	eva.setFirstname("Eva");
	// lastname
	eva.setLastname("Mensch");
	// email
	email = "wilhelm.buntscheck@pairoo.com";
	email = email.toLowerCase();
	eva.setEmail(email);

	// ======================= UserAccount
	ua = new UserAccount();
	ua.setUser(eva);

	// password
	ua.setPasswordSalt(hashResult.getSalt());
	ua.setPassword(hashResult.getDigest());
	// pseudonym
	ua.setUsername("eva");
	// membership
	membership = new Membership();
	membership.setUserAccount(ua);
	membership.setStatus(MembershipStatus.REGISTERED);
	membership.setStartDate(start.getTime());
	eva.setUserAccount(ua);
	// last login
	ua.setLastLogin(RandomUtil.randomDate(0, 1));

	// ======================= UserProfile
	up = new UserProfile();
	// birthdate
	up.setBirthdate(userProfileService.getRandomBirthdate(35, 40));
	// appearance
	appearance = new Appearance();
	appearance.setHeight(RandomUtil.randomInt(150, 210));
	appearance.setWeight(RandomUtil.randomInt(60, 130));

	// motto
	up.setMotto(RandomUtil.randomString(DemoDataGenerator.MOTTOS));
	// ethnicity
	ethnicity = Ethnicity.CAUCASIAN_WHITE;
	appearance.setEthnicity(ethnicity);
	up.setAppearance(appearance);
	// gender
	up.setPartnerType(PartnerType.FEMALE);

	// family status
	up.setFamilyStatus(FamilyStatusType.SINGLE);

	// kids
	up.setNumberOfKidsType(NumberOfKidsType.SOME);

	// mother language
	try {
	    up.setMotherLanguage(RandomUtil.randomEnum(Language.class));
	} catch (final Exception e) {
	    e.printStackTrace();
	}
	ua.setPreferredLanguage(Language.ENGLISH);

	// religion
	try {
	    up.setReligion(RandomUtil.randomEnum(Religion.class));
	} catch (final Exception e) {
	    e.printStackTrace();
	}
	eva.setUserProfile(up);

	// ======================= SearchProfile
	sp = new SearchProfile();
	sp.setPartnerType(PartnerType.MALE);
	sp.setMinAge(35);
	sp.setMaxAge(50);
	// partnership type
	sp.setPartnershipType(PartnershipType.LOVE);
	eva.setSearchProfile(sp);

	// Interaction
	final Message message1 = new Message();
	message1.setId(new Long(0)); // use id as index in list for simple
	// service ;-)
	message1.setSender(eva);
	message1.setReceiver(adam);
	message1.setSubject("Hello Adam, how are you?");
	message1.setText("Hi Adam,\n\nI am sitting under an apple tree. Come join me...");
	message1.setTimeStamp(RandomUtil.randomDate(4, 5));
	getMessagesList().add(message1);

	final Message message2 = new Message();
	message2.setId(new Long(1));
	message2.setSender(eva);
	message2.setReceiver(adam);
	message2.setSubject("Adam! Where are you?");
	message2.setText("Adam,\n\nI am waiting under the tree. I have an apple for you...");
	message2.setTimeStamp(RandomUtil.randomDate(2, 3));
	getMessagesList().add(message2);

	final Message message3 = new Message();
	message3.setId(new Long(2));
	message3.setSender(eva);
	message3.setReceiver(adam);
	message3.setSubject("Adam???");
	message3.setText("Adam, don't be afraid of the snake, it is so cute...");
	message3.setTimeStamp(RandomUtil.randomDate(0, 1));
	getMessagesList().add(message3);
    }

    public User getAdam() {
	return adam;
    }

    public User getEva() {
	return eva;
    }

    public List<Message> getMessagesList() {
	return messagesList;
    }

    public List<UserAccount> getUserAccountList() {
	return userAccountList;
    }

    public List<User> getUserList() {
	return userList;
    }

    public void setCount(final int count) {
	this.count = count;
    }
}
