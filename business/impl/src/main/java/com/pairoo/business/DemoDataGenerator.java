package com.pairoo.business;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.SmartLifecycle;

import com.datazuul.framework.domain.Language;
import com.datazuul.framework.domain.geo.GeoLocation;
import com.datazuul.framework.util.RandomUtil;
import com.pairoo.business.api.FavoriteService;
import com.pairoo.business.api.GeoLocationService;
import com.pairoo.business.api.ImageService;
import com.pairoo.business.api.MembershipService;
import com.pairoo.business.api.MessageService;
import com.pairoo.business.api.PersonProfileService;
import com.pairoo.business.api.ProductService;
import com.pairoo.business.api.UserProfileService;
import com.pairoo.business.api.UserService;
import com.pairoo.business.api.VisitService;
import com.pairoo.business.api.marketing.PromotionService;
import com.pairoo.business.api.payment.VoucherPaymentService;
import com.pairoo.business.exceptions.RegistrationException;
import com.pairoo.business.services.impl.PersonProfileServiceImpl;
import com.pairoo.domain.Appearance;
import com.pairoo.domain.Favorite;
import com.pairoo.domain.ImageEntry;
import com.pairoo.domain.LifeStyle;
import com.pairoo.domain.Message;
import com.pairoo.domain.Product;
import com.pairoo.domain.SearchProfile;
import com.pairoo.domain.User;
import com.pairoo.domain.UserAccount;
import com.pairoo.domain.UserProfile;
import com.pairoo.domain.Visit;
import com.pairoo.domain.enums.EducationType;
import com.pairoo.domain.enums.Ethnicity;
import com.pairoo.domain.enums.FamilyStatusType;
import com.pairoo.domain.enums.ImageEntryType;
import com.pairoo.domain.enums.MediaType;
import com.pairoo.domain.enums.NumberOfKidsType;
import com.pairoo.domain.enums.PartnerType;
import com.pairoo.domain.enums.PartnershipType;
import com.pairoo.domain.enums.Religion;
import com.pairoo.domain.enums.Role;
import com.pairoo.domain.enums.SmokeType;
import com.pairoo.domain.marketing.Promotion;
import com.pairoo.domain.marketing.enums.PromotionType;
import com.pairoo.domain.payment.VoucherPaymentDuplicateException;

/**
 * @author Ralf Eichinger
 */
public class DemoDataGenerator implements SmartLifecycle {
    // implements Phased did not work out (to start as last one)
    // implements ApplicationListener<ContextRefreshedEvent> did not work, too

    static final Logger LOGGER = LoggerFactory
            .getLogger(DemoDataGenerator.class);
    public static final String[] FIRSTNAMES_FEMALE = {"Emily", "Sarah",
        "Brianna", "Samantha", "Hailey", "Abby", "Sandra", "Kathleen",
        "Pamela", "Virginia", "Debra", "Maria", "Linda", "Veronika"};
    public static final String[] FIRSTNAMES_MALE = {"Jacob", "Michael",
        "Matthew", "Nicholas", "Christopher", "Abner", "Joshua", "Douglas",
        "Jack", "Keith", "Gerald", "Samuel", "Willie", "Larry", "Jose",
        "Timothy", "Ralf", "Willi"};
    public static final String[] LASTNAMES = {"Smith", "Johnson", "Williams",
        "Jones", "Brown", "Donahue", "Bailey", "Rose", "Allen", "Black",
        "Davis", "Clark", "Hall", "Lee", "Baker", "Gonzalez", "Nelson",
        "Moore", "Wilson", "Graham", "Fisher", "Cruz", "Ortiz", "Gomez",
        "Murray"};
    public static final String[] MOTTOS = {"Who cares?",
        "Why not you and me?", "Anything goes...", "Nothing else matters",
        "Diamonds are a girl's best friends."};
    public static final String[] PASSWORDS = {"start123"};
    private FavoriteService favoriteService;
    private GeoLocationService geoLocationService;
    private ImageService imageService;
    private MembershipService membershipService;
    private MessageService messageService;
    private final PersonProfileService personProfileService = new PersonProfileServiceImpl();
    private ProductService productService;
    private PromotionService promotionService;
    private UserProfileService userProfileService;
    private UserService userService;
    private VisitService visitService;
    private VoucherPaymentService voucherPaymentService;
    private Long countGeoLocations;
    private boolean addImages = true;
    private int count = 10;

    public void init() throws Exception {
        LOGGER.info("Initialising " + count + " demo users.");

        // set amount of all geolocations
        countGeoLocations = geoLocationService.countAll();

        final List<User> userList = new ArrayList<User>();
        int i;
        for (i = 0; i < count; i++) {
            User user = null;
            try {
                user = generateRandomUser(i);
                final boolean addImages = (i % 2 == 0); // only for every second one
                final Role role = RandomUtil.randomEnum(Role.class);
                fillAndSaveUser(i, user, addImages, role);
                userList.add(user);
            } catch (RuntimeException e) {
        	LOGGER.error("can not save user " + user.getUserAccount().getUsername(), e);
            }
        }

        // add adam and eva ;-)
        // adam
        final User adam = generatePremiumUser((i + 1), "Adam", "Mensch",
                PartnerType.MALE, 45, 50, "adam@pairoo.com", "adam",
                PASSWORDS[0], Language.GERMAN, Ethnicity.CAUCASIAN_WHITE,
                FamilyStatusType.SEPARATED, NumberOfKidsType.NONE, 4,
                PartnerType.FEMALE, 28, 40, Ethnicity.ANY);

        // eva
        final User eva = generatePremiumUser((i + 2), "Eva", "Mensch",
                PartnerType.FEMALE, 35, 40, "eva@pairoo.com", "eva",
                PASSWORDS[0], Language.ENGLISH, Ethnicity.CAUCASIAN_WHITE,
                FamilyStatusType.SINGLE, NumberOfKidsType.SOME, 4,
                PartnerType.MALE, 35, 50, Ethnicity.ANY);

        // Interaction
        // ===========
        // messages
        final List<Message> messagesList = getMessagesList(eva, adam);
        for (final Iterator<Message> iterator = messagesList.iterator(); iterator
                .hasNext();) {
            final Message message = iterator.next();
            message.setId(null);
            messageService.send(message);
        }

        // visits
        // visit adam
        if (!userList.isEmpty()) {
            for (int j = 0; j < 6; j++) {
                final User user = userList.get(j);
                if (user != null && user.getUserAccount().getUsername() != "adam") {
                    final Visit visit = new Visit();
                    visit.setVisitedUser(adam);
                    visit.setVisitor(user);
                    visit.setTimeStamp(new Date());
                    visitService.save(visit);
                }
            }
        }
        // favorites
        final Favorite fav1 = new Favorite();
        fav1.setOwner(adam);
        fav1.setTarget(eva);
        fav1.setTimeStamp(new Date());
        favoriteService.save(fav1);

        System.out.println("for login use: eva / " + PASSWORDS[0]);
        System.out.println("for login use: adam / " + PASSWORDS[0]);
    }

    private List<Message> getMessagesList(final User sender, final User receiver) {
        final List<Message> result = new ArrayList<Message>();

        final Message message1 = new Message();
        message1.setId(new Long(0)); // use id as index in list for simple
        // service ;-)
        message1.setSender(sender);
        message1.setReceiver(receiver);
        message1.setSubject("Hello Adam, how are you?");
        message1.setText("Hi Adam,\n\nI am sitting under an apple tree. Come join me...");
        message1.setTimeStamp(RandomUtil.randomDate(4, 5));
        result.add(message1);

        final Message message2 = new Message();
        message2.setId(new Long(1));
        message2.setSender(sender);
        message2.setReceiver(receiver);
        message2.setSubject("Adam! Where are you?");
        message2.setText("Adam,\n\nI am waiting under the tree. I have an apple for you...");
        message2.setTimeStamp(RandomUtil.randomDate(2, 3));
        result.add(message2);

        final Message message3 = new Message();
        message3.setId(new Long(2));
        message3.setSender(sender);
        message3.setReceiver(receiver);
        message3.setSubject("Adam???");
        message3.setText("Adam, don't be afraid of the snake, it is so cute...");
        message3.setTimeStamp(RandomUtil.randomDate(0, 1));
        result.add(message3);

        return result;
    }

    private User generatePremiumUser(final int i, final String firstname,
            final String lastname, final PartnerType partnerType,
            final int minAge, final int maxAge, final String email,
            final String username, final String password,
            final Language preferredLanguage, final Ethnicity ethnicity,
            final FamilyStatusType familyStatusType,
            final NumberOfKidsType numberOfKidsType, final int amountOfImages,
            final PartnerType searchProfilePartnerType,
            final int searchProfileMinAge, final int searchProfileMaxAge,
            final Ethnicity searchProfileEthnicity) throws IOException,
            SecurityException, IllegalArgumentException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException,
            RegistrationException {
        final User user = new User();
        final SearchProfile searchProfile = user.getSearchProfile();
        final UserAccount userAccount = user.getUserAccount();
        final UserProfile userProfile = user.getUserProfile();

        // I am looking for
        searchProfile.setPartnerType(searchProfilePartnerType);
        searchProfile.setMaxAge(searchProfileMaxAge);
        searchProfile.setMinAge(searchProfileMinAge);

        // I am
        // gender
        userProfile.setPartnerType(partnerType);

        // living in
        addGeoLocation(userProfile);

        // pseudonym
        userAccount.setUsername(username);

        // password
        userAccount.setPassword(password);

        // preferred language
        userAccount.setPreferredLanguage(preferredLanguage);

        // email
        user.setEmail(email);

        register(user);

        // additional data
        user.setFirstname(firstname);
        user.setLastname(lastname);
        userProfile.getAppearance().setEthnicity(ethnicity);
        userProfile.setBirthdate(userProfileService.getRandomBirthdate(minAge,
                maxAge));
        userProfile.setFamilyStatus(familyStatusType);
        userProfile.setMotherLanguage(RandomUtil.randomEnum(Language.class));
        userProfile.setMotto(RandomUtil.randomString(MOTTOS));
        userProfile.setNumberOfKidsType(numberOfKidsType);
        userProfile.setReligion(RandomUtil.randomEnum(Religion.class));
        final List<Ethnicity> ethnicities = new ArrayList<Ethnicity>();
        ethnicities.add(searchProfileEthnicity);
        searchProfile.setEthnicities(ethnicities);
        searchProfile.setPartnershipType(PartnershipType.LOVE);

        userService.save(user);

        addImages(userProfile, amountOfImages);

        upgradeMembership(i, user, Role.PREMIUM);

        return user;
    }

    private void fillAndSaveUser(final int i, final User user,
            final boolean addImages, final Role role)
            throws RegistrationException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, IOException {
        // REGISTRATION
        // ============
        register(user);

        // ADD ADDITIONAL DATA
        // ===================
        addAdditionalData(user);

        // SAVE
        // ====
        userService.save(user);

        // add images
        if (addImages) {
            addImages(user.getUserProfile());
        }

        upgradeMembership(i, user, role);
    }

    private void upgradeMembership(final int i, final User user, final Role role) {
        // MEMBERSHIP
        // ==========
        membershipService.activateMembership(user.getUserAccount());

        // PREMIUM MEMBERSHIP
        // ==================
        if (Role.PREMIUM.equals(role)) {
            addPremiumMembership(i, user);
        }
    }

    private void addAdditionalData(final User user)
            throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException {
        // user: lastname
        user.setLastname(RandomUtil.randomString(LASTNAMES));

        final UserAccount userAccount = user.getUserAccount();

        // userAccount: last login
        userAccount.setLastLogin(RandomUtil.randomDate(0, 1));

        // preferred language
        userAccount.setPreferredLanguage(Language.ENGLISH);

        final UserProfile userProfile = user.getUserProfile();
        // userProfile: birthdate
        userProfile.setBirthdate(userProfileService.getRandomBirthdate(
                personProfileService.getMinAge(),
                personProfileService.getMaxAge()));

        // userProfile: family status
        userProfile.setFamilyStatus(RandomUtil.randomEnum(
                FamilyStatusType.class, FamilyStatusType.ANY));

        // userProfile: mother tongue
        userProfile.setMotherLanguage(RandomUtil.randomEnum(Language.class,
                Language.ANY));

        // userProfile: motto
        userProfile.setMotto(RandomUtil.randomString(MOTTOS));

        // userProfile: number of kids
        userProfile.setNumberOfKidsType(RandomUtil.randomEnum(
                NumberOfKidsType.class, NumberOfKidsType.ANY));

        // userProfile: religion
        userProfile.setReligion(RandomUtil.randomEnum(Religion.class,
                Religion.ANY));

        final Appearance appearance = userProfile.getAppearance();
        // appearance: weight, height
        appearance.setHeight(RandomUtil.randomInt(150, 210));
        appearance.setWeight(RandomUtil.randomInt(60, 130));

        // appearance: ethnicity
        appearance.setEthnicity(RandomUtil.randomEnum(Ethnicity.class,
                Ethnicity.ANY));

        final LifeStyle lifeStyle = userProfile.getLifeStyle();
        // lifestyle: smoke type
        lifeStyle.setSmokeType(RandomUtil.randomEnum(SmokeType.class,
                SmokeType.ANY));

        final SearchProfile searchProfile = user.getSearchProfile();
        // searchProfile: age
        searchProfile.setMinAge(RandomUtil.randomInt(20, 25));
        searchProfile.setMaxAge(RandomUtil.randomInt(35, 70));

        // searchProfile: education
        final List<EducationType> educationTypes = new ArrayList<EducationType>();
        educationTypes.add(EducationType.HIGH_SCHOOL);
        educationTypes.add(EducationType.PHD_OR_DOCTORATE);
        educationTypes.add(EducationType.UNIVERSITY);
        searchProfile.setEducationTypes(educationTypes);

        // searchProfile: partnership type
        searchProfile.setPartnershipType(RandomUtil
                .randomEnum(PartnershipType.class));
    }

    private void addPremiumMembership(final int i, final User user) {
        final UserAccount userAccount = user.getUserAccount();

        final List<Product> allProducts = productService
                .getAllProducts(Role.PREMIUM);
        final int index = RandomUtil.randomInt(0, allProducts.size() - 1);
        final Product product = allProducts.get(index);

        // generate an unique voucher to be used for "paying"
        final Promotion voucherPromotion = new Promotion();
        voucherPromotion.setPromotionType(PromotionType.VOUCHER);
        voucherPromotion.setUserAccount(userAccount);
        voucherPromotion.setCode("1234567890" + i);
        voucherPromotion.setTimeStamp(new Date());
        voucherPromotion.setValidFrom(new Date());
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        voucherPromotion.setValidTo(calendar.getTime());
        voucherPromotion.setProduct(product);
        promotionService.save(voucherPromotion);

        // "pay" with generated voucher
        try {
            voucherPaymentService.encashVoucher(userAccount, voucherPromotion);
        } catch (VoucherPaymentDuplicateException e) {
            e.printStackTrace();
        }
    }

    private void register(final User user) throws RegistrationException {
        final Locale locale = user.getUserAccount().getPreferredLanguage()
                .getLocale();
        // do not send activationLink when you want to avoid mail sending
        // "http://www.pairoo.com/activationlink"
        userService.register(user, locale, null);
    }

    private User generateRandomUser(final int i) throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException {
        final User user = new User();
        final SearchProfile searchProfile = user.getSearchProfile();
        final UserAccount userAccount = user.getUserAccount();
        final UserProfile userProfile = user.getUserProfile();

        // I am looking for
        searchProfile.setPartnerType(RandomUtil.randomEnum(PartnerType.class));
        searchProfile.setMaxAge(personProfileService.getMaxAge());
        searchProfile.setMinAge(personProfileService.getMinAge());

        // I am
        // gender
        final PartnerType partnerType = RandomUtil.randomEnum(
                PartnerType.class, PartnerType.ANY);
        userProfile.setPartnerType(partnerType);

        // living in
        addGeoLocation(userProfile);

        // pseudonym / firstname
        String firstname = null;
        if (partnerType == PartnerType.MALE) {
            firstname = RandomUtil.randomString(FIRSTNAMES_MALE);
        } else {
            firstname = RandomUtil.randomString(FIRSTNAMES_FEMALE);
        }
        user.setFirstname(firstname);
        userAccount.setUsername(firstname + i);

        // password
        userAccount.setPassword(PASSWORDS[0]);

        // language
        userAccount.setPreferredLanguage(Language.ENGLISH);
        
        // email
        String email = user.getFirstname() + "." + user.getLastname() + i + "@"
                + "localhost";
        email = email.toLowerCase();
        user.setEmail(email);

        return user;
    }

    private void addGeoLocation(final UserProfile userProfile) {
        final long randomId = RandomUtil.randomLong(1, countGeoLocations);
        final GeoLocation loc = geoLocationService.get(randomId);
        userProfile.setGeoLocation(loc);
    }

    private String randomFilePath(final PartnerType partnerType) {
        if (PartnerType.FEMALE.equals(partnerType)) {
            return "demo/woman-0" + RandomUtil.randomInt(1, 5) + ".png";
        } else if (PartnerType.MALE.equals(partnerType)) {
            return "demo/man-0" + RandomUtil.randomInt(1, 3) + ".png";
        }
        return null;
    }

    /**
     * @param favoriteService the favoriteService to set
     */
    public void setFavoriteService(final FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    /**
     * @param imageService the imageService to set
     */
    public void setImageService(final ImageService imageService) {
        this.imageService = imageService;
    }

    public void setMessageService(final MessageService service) {
        this.messageService = service;
    }

    public void setUserService(final UserService service) {
        this.userService = service;
    }

    /**
     * @param visitService the visitService to set
     */
    public void setVisitService(final VisitService visitService) {
        this.visitService = visitService;
    }

    /**
     * @param geoLocationService the geoLocationService to set
     */
    public void setGeoLocationService(
            final GeoLocationService geoLocationService) {
        this.geoLocationService = geoLocationService;
    }

    public boolean isAddImages() {
        return addImages;
    }

    public void setAddImages(final boolean addImages) {
        this.addImages = addImages;
    }

    public void setProductService(final ProductService productService) {
        this.productService = productService;
    }

    /**
     * @param userProfile throws IOException
     */
    private void addImages(final UserProfile userProfile) throws IOException {
        addImages(userProfile, 2);
    }

    private void addImages(final UserProfile userProfile, final int i)
            throws IOException {
        if (isAddImages()) {
            // add images (but not if we are in "TEST" mode)
            // final List<ImageEntry> imageEntries =
            // userProfile.getImageEntries();
            for (int j = 0; j <= i; j++) {
                final ImageEntry imageEntry = new ImageEntry();
                imageEntry.setMediaType(MediaType.IMAGE_PNG);
                if (j == 0) {
                    imageEntry.setProfileImage(true);
                }
                if (j != 0 && j % 2 == 0) {
                    imageEntry.setVisible(false);
                } else {
                    imageEntry.setVisible(true);
                }
                final String filePath = randomFilePath(userProfile
                        .getPartnerType());
                final InputStream is = this.getClass().getResourceAsStream(
                        filePath);
                final ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int c = 0;
                while ((c = is.read()) != -1) {
                    bos.write((char) c);
                }
                final byte[] imageBytes = bos.toByteArray();
                imageEntry.setClientFileName("image.png");
                try {
                    imageService.save(imageEntry, ImageEntryType.NORMAL,
                            imageBytes);
                    userProfile.getImageEntries().add(imageEntry);
                    userProfileService.save(userProfile);
                    // imageEntry.setMediaType(MediaType.IMAGE_JPEG);
                    // imageService.save(imageEntry, ImageEntryType.NORMAL,
                    // imageBytes);
                    // imageEntryService.save(imageEntry);
                } catch (final IOException ioe) {
                    LOGGER.error(
                            "Sorry, there was an error uploading the image.",
                            ioe);
                }
            }
        }
    }

    /**
     * @param membershipService the membershipService to set
     */
    public void setMembershipService(final MembershipService membershipService) {
        this.membershipService = membershipService;
    }

    /**
     * @param count the count to set
     */
    public void setCount(final int count) {
        this.count = count;
    }

    // @Override
    // public int getPhase() {
    // // start as last bean to be sure all other beans are up and running
    // (especially emailSao...)
    // return Integer.MAX_VALUE;
    // }
    // @Override
    // public void onApplicationEvent(ContextRefreshedEvent event) {
    // try {
    // init();
    // } catch (Exception e) {
    // throw new RuntimeException("can not start up!");
    // }
    // }
    @Override
    public void start() {
        try {
            init();
        } catch (final Exception e) {
            e.printStackTrace();
            throw new RuntimeException("can not start up!");
        }
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop() {
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public void stop(final Runnable callback) {
    }

    /**
     * @param userProfileService the userProfileService to set
     */
    public void setUserProfileService(
            final UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    /**
     * @param voucherService the voucherService to set
     */
    public void setVoucherPaymentService(final VoucherPaymentService voucherPaymentService) {
        this.voucherPaymentService = voucherPaymentService;
    }

    /**
     * @param promotionService the promotionService to set
     */
    public void setPromotionService(PromotionService promotionService) {
        this.promotionService = promotionService;
    }
}
