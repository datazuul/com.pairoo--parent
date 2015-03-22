package com.pairoo.business.services.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.measure.quantity.Length;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;
import javax.validation.ValidationException;

import org.jscience.physics.amount.Amount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.datazuul.framework.business.services.persistence.AbstractDomainObjectServiceImpl;
import com.datazuul.framework.domain.geo.Country;
import com.datazuul.framework.domain.geo.GeoLocation;
import com.datazuul.framework.security.HashResult;
import com.datazuul.framework.security.HashUtil;
import com.datazuul.framework.util.EnumUtils;
import com.datazuul.framework.util.RandomUtil;
import com.googlecode.genericdao.search.Search;
import com.pairoo.backend.dao.UserDao;
import com.pairoo.backend.dao.search.DaoUserSearchParams;
import com.pairoo.backend.sao.EmailSao;
import com.pairoo.business.LocalizedStrings;
import com.pairoo.business.api.CountryService;
import com.pairoo.business.api.GeoLocationService;
import com.pairoo.business.api.MembershipService;
import com.pairoo.business.api.MessageService;
import com.pairoo.business.api.NotificationService;
import com.pairoo.business.api.PersonProfileService;
import com.pairoo.business.api.ProductService;
import com.pairoo.business.api.UserAccountService;
import com.pairoo.business.api.UserService;
import com.pairoo.business.api.marketing.PromotionService;
import com.pairoo.business.api.payment.VoucherPaymentService;
import com.pairoo.business.exceptions.RegistrationException;
import com.pairoo.business.exceptions.ReportProfileException;
import com.pairoo.domain.Membership;
import com.pairoo.domain.Message;
import com.pairoo.domain.PersonProfile;
import com.pairoo.domain.Product;
import com.pairoo.domain.SearchProfile;
import com.pairoo.domain.User;
import com.pairoo.domain.UserAccount;
import com.pairoo.domain.UserProfile;
import com.pairoo.domain.enums.MaxDistance;
import com.pairoo.domain.enums.MembershipStatus;
import com.pairoo.domain.enums.Role;
import com.pairoo.domain.geo.GeoArea;
import com.pairoo.domain.marketing.Promotion;
import com.pairoo.domain.payment.VoucherPaymentDuplicateException;
import com.pairoo.domain.search.UserSearchResult;

public class UserServiceImpl extends AbstractDomainObjectServiceImpl<Long, User> implements UserService {

    static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private UserDao dao;
    @Autowired
    private EmailSao emailSao;
    private final CountryService countryService;
    private final GeoLocationService geoLocationService;
    private final MembershipService membershipService;
    private final MessageService messageService;
    private final NotificationService notificationService;
    private final PersonProfileService personProfileService;
    private final ProductService productService;
    private final PromotionService promotionService;
    private final UserAccountService userAccountService;
    private final VoucherPaymentService voucherPaymentService;

    /**
     * Default constructor needed for Spring...
     */
    // public UserServiceImpl() {
    // super();
    // }
    /**
     * Constructor needed for test handing over a mock dao.
     *
     * @param dao the data access interface
     */
    public UserServiceImpl(final UserDao dao, final UserAccountService userAccountService,
            final PersonProfileService personProfileService, final MessageService messageService,
            final ProductService productService, final MembershipService membershipService,
            final CountryService countryService, final PromotionService promotionService,
            final VoucherPaymentService voucherPaymentService, final GeoLocationService geoLocationService, final NotificationService notificationService) {
        super(dao);
        this.dao = dao;

        this.countryService = countryService;
        this.membershipService = membershipService;
        this.messageService = messageService;
        this.notificationService = notificationService;
        this.personProfileService = personProfileService;
        this.productService = productService;
        this.promotionService = promotionService;
        this.userAccountService = userAccountService;
        this.voucherPaymentService = voucherPaymentService;
        this.geoLocationService = geoLocationService;
    }

    @Override
    public List<User> getByEmail(final String email) {
        LOGGER.info("email = {}", email);
        final Search search = new Search(User.class);
        search.addFilterEqual("email", email);
        return ((UserDao) dao).search(search);
    }

    @Override
    public User register(final User user, final Locale locale, final String landingPageLink)
            throws RegistrationException {
	// TODO add business side validation (unique email, username) if not
        // only wicket accesses this...

        // 0. save both user and user account (cascade)
        // FIXME Batch entry 0 insert into SEARCHPROFILE (PARTNERTYPE,
        // PROFILE_PICTURE_TYPE, MINAGE, MAXAGE, GEOAREA_ID,
        // NUMBER_OF_KIDS_TYPE, WANT_MORE_CHILDREN_TYPE, PARTNERSHIPTYPE,
        // MOTHERLANGUAGE, HOUSEHOLDTYPE, MINHEIGHTCM, MAXHEIGHTCM, SMOKETYPE,
        // INCOMETYPE, OCCUPATIONTYPE, ID) values ('FEMALE', 'ANY', '33', '40',
        // '25', 'ANY', 'ANY', 'ANY', 'ANY', 'ANY', NULL, NULL, 'ANY', 'ANY',
        // 'ANY', '10') was aborted. Call getNextException to see the cause.
        //
        // [2012-08-28 11:20:56,336] ERROR [ajp-bio-28009-exec-7][]
        // org.hibernate.util.JDBCExceptionReporter.logExceptions - ERROR:
        // duplicate key value violates unique constraint "searchprofile_pkey"
        // Detail: Key (id)=(10) already exists.
        // insert into NOTIFICATIONSETTINGS (ONNEWMESSAGE, ONNEWSUGGESTIONS,
        // ONVISIT, NEWSLETTER, WEEKENDSUGGESTIONS, WEEKLYSTATISTIC, ID) values
        // ('1', '1', '1', '1', '1', '1', '12') was aborted.
        // duplicate key value violates unique constraint
        // "notificationsettings_pkey"
        // Detail: Key (id)=(12) already exists.
        // insert into GEOAREA (CONTINENT, COUNTRY, GEOLOCATION_ID, MAXDISTANCE,
        // SUBDIVISION_ID, ZIPCODE_START, ID) values (NULL, 'GERMANY', NULL,
        // 'ANY', NULL, NULL, '22')
        // duplicate key value violates unique constraint "geoarea_pkey"
        // Detail: Key (id)=(22) already exists.
        dao.save(user);

        // 1. update user account data
        final UserAccount userAccount = user.getUserAccount();
        // hash password
        userAccountService.hashPassword(userAccount);
        // add role
        userAccountService.addRole(userAccount, Role.STANDARD.getCode());
        userAccount.setUser(user);
        userAccountService.save(userAccount);

        // 2. add membership with free product / standard product
        final Product product = productService.getStandardProduct();
        final Membership membership = new Membership();
        membership.setProduct(product);
        membershipService.addMembership(userAccount, membership, MembershipStatus.REGISTERED);

        // 3. send confirmation email with activation link
        if (landingPageLink != null) {
            // create message
            // try {
            sendConfirmationEmail(user, locale, landingPageLink, LocalizedStrings.REGISTRATION_CONFIRMATION_SUBJECT,
                    "com/pairoo/business/templates/registration-activation-txt", null);
            // } catch (RegistrationException registrationException) {
            // // rollback register
            // // delete persisted membership (it is the only one because the
            // user just registered)
            // membershipService.delete(membership);
            // // delete persisted user/useraccount/userprofile
            // delete(user);
            //
            // // forward exception
            // throw registrationException;
            // }
        }

        // 4. inform Pairoo about new member
        notificationService.notifyAboutNewMember(user);
        return user;
    }

    private void sendConfirmationEmail(final User user, final Locale locale, final String landingPageLink,
            final String subjectKey, final String templatePath, Map<String, Object> model) throws RegistrationException {
        final User sender = new User();
        sender.setEmail(LocalizedStrings.get(LocalizedStrings.EMAIL_ADDRESS_PAIROO, locale));
        if (model == null) {
            model = new HashMap<>();
        }
        model.put("user", user);
        model.put("activationLink", landingPageLink);
        final Message msg = messageService.createMessage(user, sender, locale, LocalizedStrings.get(subjectKey, locale), templatePath, model);

        try {
            emailSao.sendMail(msg);
        } catch (final Exception e) {
            final RegistrationException registrationException = new RegistrationException(e);
            registrationException.setCode(RegistrationException.Code.SEND_CONFIRMATION_EMAIL);
            throw registrationException;
        }

        LOGGER.info("Registration activation link sent to {}", user.getEmail());
    }

    @Override
    public void resetPassword(final User user, final Locale locale) {
        final String password = RandomUtil.randomPassword(8);
        final HashResult hashResult = HashUtil.hash(password);

        final UserAccount userAccount = user.getUserAccount();
        userAccount.setPasswordSalt(hashResult.getSalt());
        userAccount.setPassword(hashResult.getDigest());

        // 1. save useraccount
        userAccountService.save(userAccount);

        // 2. send information email
        // create message
        final User sender = new User();
        sender.setEmail(LocalizedStrings.get(LocalizedStrings.EMAIL_ADDRESS_PAIROO, locale));
        final Map<String, Object> model = new HashMap<>();
        model.put("password", password); // in cleartext, so that user can use it ;-)
        final Message msg = messageService.createMessage(user, sender, locale, LocalizedStrings.get(LocalizedStrings.RESET_PASSWORD_SUBJECT, locale),
                "com/pairoo/business/templates/new-password-txt", model);

        try {
            emailSao.sendMail(msg);
        } catch (final Exception e) {
            LOGGER.error("exception during sending email to " + user.getEmail(), e);
            throw new RuntimeException(e);
        }

        LOGGER.info("Reset password for user {}, sent it to {}", user.getUserAccount().getUsername(), user.getEmail());
    }

    @Override
    public void changePassword(final User user, final Locale locale, final String oldPassword,
            final String newPassword, final String confirmNewPassword) {
        // 1. proof old password
        final String digest = user.getUserAccount().getPassword();
        final String salt = user.getUserAccount().getPasswordSalt();
        byte[] bDigest;
        byte[] bSalt;
        try {
            bDigest = HashUtil.base64ToByte(digest);
            bSalt = HashUtil.base64ToByte(salt);
            // Compute the new DIGEST
            final byte[] proposedDigest = HashUtil.hash(oldPassword, bSalt);
            if (!Arrays.equals(proposedDigest, bDigest)) {
                throw new ValidationException(
                        "Changing of password failed. Given old password does not match stored password.");
            }
        } catch (final IOException ex) {
            LOGGER.error("base64 to byte method failed!", ex);
        }

        // 2. validate new password
        if (!newPassword.equals(confirmNewPassword)) {
            throw new ValidationException(
                    "Changing of password failed. Given new password does not match repeated new password.");
        }

        // 3. validate strength of new password
        if (newPassword.length() < 6 || newPassword.length() > 12) {
            throw new ValidationException("Changing of password failed. Given new password is not of valid length.");
        }

        // 4. set new password
        final HashResult hashResult = HashUtil.hash(newPassword);
        final UserAccount userAccount = user.getUserAccount();
        user.getUserAccount().setPasswordSalt(hashResult.getSalt());
        user.getUserAccount().setPassword(hashResult.getDigest());

        // 1. save useraccount (saving unchanged user does no cascade save...)
        userAccountService.save(userAccount);

        // removed on product manager's request
        // // 2. send information email
        // final Message msg = new Message();
        // // recipient
        // msg.setReceiver(user);
        //
        // // sender
        // final User sender = new User();
        // sender.setEmail("noreply@pairoo.com"); // TODO externalize
        // configuration
        // msg.setSender(sender);
        //
        // // subject
        // msg.setSubject("TODO subject: Your new password");
        //
        // // text
        // final Map model = new HashMap();
        // model.put("password", newPassword);
        //
        // String text = null;
        // try {
        // text = renderText("com/pairoo/business/templates/new-password-txt",
        // locale, model);
        // } catch (final VelocityException e) {
        // e.printStackTrace();
        // }
        // msg.setText(text);
        //
        // try {
        // emailSao.sendMail(msg);
        // } catch (final Exception e) {
        // LOGGER.error("exception during sending email to " + user.getEmail(),
        // e);
        // throw new RuntimeException(e);
        // }
        //
        // LOGGER.info("Changed password for user {}, sent it to {}",
        // user.getUserAccount().getUsername(), user.getEmail());
        LOGGER.info("Changed password for user {}", user.getUserAccount().getUsername());
    }

    @Override
    public void reportProfile(final User reporter, final User profileUser, final String text, final Locale locale)
            throws ReportProfileException {
        // send report email
        // create message
        final User receiver = new User();
        final String receiverEmail = LocalizedStrings.get(LocalizedStrings.EMAIL_ADDRESS_SUPPORT, locale);
        receiver.setEmail(receiverEmail);

        final Map<String, Object> model = new HashMap<>();
        model.put("receiverEmail", receiverEmail);
        model.put("reporterUsername", reporter.getUserAccount().getUsername());
        model.put("profileUsername", profileUser.getUserAccount().getUsername());
        model.put("text", text);

        final Message msg = messageService.createMessage(receiver, reporter, locale,
                LocalizedStrings.get(LocalizedStrings.SUBJECT_REPORT_PROFILE, locale), "com/pairoo/business/templates/email-report-profile-txt",
                model);

        try {
            emailSao.sendMail(msg);
        } catch (final Exception e) {
            throw new ReportProfileException(e);
        }

        LOGGER.info("User {} reported misuse by user {}", reporter.getUserAccount().getUsername(), profileUser
                .getUserAccount().getUsername());
    }

    @Override
    public User getRegistrationDefaultUser(Locale locale) {
        final User user = new User();

        // ---------------- default values
        final UserProfile userProfile = new UserProfile();
	// userProfile.setPartnerType(PartnerType.FEMALE);
        // userProfile.setHouseholdType(HouseholdType.DONT_SAY);

        // final LifeStyle lifeStyle = new LifeStyle();
        // lifeStyle.setSmokeType(SmokeType.DONT_SAY);
        // userProfile.setLifeStyle(lifeStyle);
        final Locale sessionLocale = locale;
        final String countryCode = sessionLocale.getCountry();
        if (countryCode != null) {
            final Country country = countryService.getCountryByCode(countryCode);
            if (country != null) {
                final GeoLocation geoLocation = new GeoLocation();
                geoLocation.setCountry(country);
                userProfile.setGeoLocation(geoLocation);
            }
        }
        user.setUserProfile(userProfile);

        final SearchProfile searchProfile = new SearchProfile();
        // searchProfile.setPartnerType(PartnerType.MALE);
        // searchProfile.getEthnicities().add(Ethnicity.ANY);
        searchProfile.setMinAge(personProfileService.getMinAge());
        searchProfile.setMaxAge(personProfileService.getMaxAge());
        user.setSearchProfile(searchProfile);

        return user;
    }

    @Override
    public void registerAndEncashVoucher(User user, Locale preferredLocale, String landingPageLink, Promotion promotion)
            throws RegistrationException, VoucherPaymentDuplicateException {
	// TODO add business side validation (unique email, username) if not
        // only wicket accesses this...

        // 0. save both user and user account (cascade)
        dao.save(user);

        // 1. update user account data
        final UserAccount userAccount = user.getUserAccount();
        // hash password
        userAccountService.hashPassword(userAccount);
        // add role
        userAccountService.addRole(userAccount, Role.STANDARD.getCode());
        userAccount.setUser(user);
        userAccountService.save(userAccount);

        // 2. add membership with free product / standard product
        voucherPaymentService.encashVoucher(userAccount, promotion);

        // 3. send confirmation email with activation link
        // create message
        Membership lastMembership = membershipService.getLastMembership(userAccount);
        Product product = lastMembership.getProduct();
        String productRole = LocalizedStrings.get(EnumUtils.getEnumKey(product.getRole()), preferredLocale);

        final Map<String, Object> model = new HashMap<>();
        model.put("productRole", productRole);

        model.put("endDate",
                DateFormat.getDateInstance(DateFormat.SHORT, preferredLocale).format(userAccount.getPremiumEndDate()));

        String subscriptionFunctionality = LocalizedStrings.get(LocalizedStrings.SUBSCRIPTION_FUNCTIONALITY_INACTIVE,
                preferredLocale);
        if (product.isAbo()) {
            subscriptionFunctionality = LocalizedStrings.get(LocalizedStrings.SUBSCRIPTION_FUNCTIONALITY_ACTIVE,
                    preferredLocale);
        }
        model.put("subscriptionFunctionality", subscriptionFunctionality);

        try {
            sendConfirmationEmail(user, preferredLocale, landingPageLink,
                    LocalizedStrings.REGISTRATION_CONFIRMATION_SUBJECT,
                    "com/pairoo/business/templates/email-registration-activation-voucher-txt", model);
        } catch (RegistrationException registrationException) {
	    // rollback register

            // delete persisted membership (it is the only one because the user
            // just registered)
            membershipService.delete(lastMembership);
            // delete persisted user/useraccount/userprofile
            delete(user);

            // forward exception
            throw registrationException;
        }

        // 4. inform Pairoo about new member
        notificationService.notifyAboutNewMember(user);
    }

    @Override
    public List<UserSearchResult> search(final User user, final SearchProfile searchProfile, final long first,
            final long count) {
        final GeoLocation geoLocationUser = user.getUserProfile().getGeoLocation();
        Country usersCountry;
        try {
            usersCountry = geoLocationUser.getCountry();
        } catch (Exception e) {
            usersCountry = Country.GERMANY;
        }
        DaoUserSearchParams daoUserSearchParams = fillDaoSearchParams(user, searchProfile, usersCountry);

        // do search
        List<UserSearchResult> results = dao.search(daoUserSearchParams, first, count);

        // calculate missing data
        for (UserSearchResult userSearchResult : results) {
            // age
            Date birthdate = userSearchResult.getBirthdate();
            if (birthdate != null) {
                userSearchResult.setAge(PersonProfile.getAge(birthdate));
            }
            // distance
            final GeoLocation geoLocation2 = userSearchResult.getGeoLocation();
            Unit<Length> preferredUnit = usersCountry.getUnitDistance();
            String distance = geoLocationService.distanceInPreferredUnit(geoLocationUser, geoLocation2, preferredUnit);
            userSearchResult.setDistance(distance);

            // premium
            Date premiumEndDate = userSearchResult.getPremiumEndDate();
            userSearchResult.setPremium(!userAccountService.isPremiumEndDateExceeded(premiumEndDate));
        }

        return results;
    }

    @Override
    public long count(User user, SearchProfile searchProfile) {
        final GeoLocation geoLocationUser = user.getUserProfile().getGeoLocation();
        Country usersCountry;
        try {
            usersCountry = geoLocationUser.getCountry();
        } catch (Exception e) {
            usersCountry = Country.GERMANY;
        }
        DaoUserSearchParams daoUserSearchParams = fillDaoSearchParams(user, searchProfile, usersCountry);
        return dao.count(daoUserSearchParams);
    }

    public DaoUserSearchParams fillDaoSearchParams(final User user, final SearchProfile searchProfile,
            Country usersCountry) {
        DaoUserSearchParams daoUserSearchParams = new DaoUserSearchParams(user, searchProfile);

        // convert to zipcode with dedicated geolocation if found
        final String zipcode = searchProfile.getGeoArea().getZipcodeStartsWith();
        if (zipcode != null) {
            Country country = daoUserSearchParams.getCountry();

            // lookup geolocation for the edited zipcode and country
            final GeoLocation geoLocation = geoLocationService.getByCountryAndZipcode(country, zipcode);
            if (geoLocation != null) {
                final GeoArea geoArea = searchProfile.getGeoArea();
                // unique geolocation found, shift data in GeoArea object
                geoArea.setGeoLocation(geoLocation);
                geoArea.setZipcodeStartsWith(zipcode); // because model
                // of field
                geoArea.setSubdivision(null);
                geoArea.setCountry(country); // because model of field
                geoArea.setContinent(null);

                final MaxDistance maxDistanceEnumValue = searchProfile.getGeoArea().getMaxDistance();
                if (maxDistanceEnumValue != null) {
                    if (!MaxDistance.ANY.equals(maxDistanceEnumValue)) {
                        // max distance given, so search in an area
                        // instead of in an exact location
                        final Integer maxDistanceUnits = maxDistanceEnumValue.getDistanceUnits();

                        // convert to kilometers
                        final Unit<Length> unitDistance = usersCountry.getUnitDistance();
                        final Amount<Length> maxDistance = Amount.valueOf(maxDistanceUnits, unitDistance);
                        final double maxDistanceInKilometers = maxDistance.doubleValue(SI.KILOMETER);

                        // latitude between min / max
                        // longitude between min / max
                        final GeoLocation[] extremePoints = geoLocationService.getExtremePointsFrom(geoLocation,
                                maxDistanceInKilometers);
                        GeoLocation minPoint = extremePoints[0];
                        GeoLocation maxPoint = extremePoints[1];

                        daoUserSearchParams.setMinLatitude(minPoint.getLatitude());
                        daoUserSearchParams.setMinLongitude(minPoint.getLongitude());
                        daoUserSearchParams.setMaxLatitude(maxPoint.getLatitude());
                        daoUserSearchParams.setMaxLongitude(maxPoint.getLongitude());
                    }
                } else {
                    // no max distance given, so search in exact location
                    daoUserSearchParams.setZipcode(zipcode);
                }
            } else {
                // leave zipcode fragment in
                // searchProfile.getGeoArea().getZipcodeStartsWith()...
                daoUserSearchParams.setZipcodeStartsWith(zipcode);
            }
        }

        return daoUserSearchParams;
    }
}
