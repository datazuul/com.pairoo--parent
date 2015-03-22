package com.pairoo.business.api;

import java.util.List;
import java.util.Locale;

import com.datazuul.framework.business.services.DomainObjectService;
import com.pairoo.business.exceptions.RegistrationException;
import com.pairoo.business.exceptions.ReportProfileException;
import com.pairoo.domain.SearchProfile;
import com.pairoo.domain.User;
import com.pairoo.domain.marketing.Promotion;
import com.pairoo.domain.payment.VoucherPaymentDuplicateException;
import com.pairoo.domain.search.UserSearchResult;

/**
 * @author Ralf Eichinger
 */
public interface UserService extends DomainObjectService<Long, User> {

    public static final String BEAN_ID = "userService";

    /**
     * Find Users having the email.
     *
     * @param email search param
     * @return matching Users
     */
    public List<User> getByEmail(String email);

    /**
     * Registering a newly created user. For complete registration we need the
     * preferred language (locale) for choosing the right email template and the
     * activation link to be included into email.
     *
     * @param user to be registered
     * @param locale user's preferred language
     * @param activationLink link to activate account when being clicked in
     * email
     * @return the registered user
     * @throws RegistrationException thrown if a technical problem appears
     * during registration
     */
    public User register(User user, Locale locale, String activationLink) throws RegistrationException;

    /**
     * Creates a new password and sends it to the user. Used e.g. if password
     * forgotten.
     *
     * @param user user to reset password for
     * @param locale user's preferred language
     */
    public void resetPassword(User user, Locale locale);

    /**
     * Change password of an user.
     *
     * @param user user whose password should be changed
     * @param locale locale/language for notifying user
     * @param oldPassword old password to verify it is the user himself
     * @param newPassword new password
     * @param confirmNewPassword new password repeated to avoid typo errors
     */
    public void changePassword(User user, Locale locale, String oldPassword, String newPassword,
            String confirmNewPassword);

    /**
     * Report a profile of an user that did some misuse.
     *
     * @param reporter user reporting misuse
     * @param profileUser user done misuse
     * @param text reason, description of misuse
     * @param locale locale/language for report template
     */
    public void reportProfile(User reporter, User profileUser, String text, final Locale locale)
            throws ReportProfileException;

    /**
     * @param locale locale of default user
     * @return default user object for starting registration forms
     */
    public User getRegistrationDefaultUser(Locale locale);

    /**
     * @param user user object containing registration data
     * @param preferredLocale user's preferred language
     * @param activationLink link to be sent in email
     * @param promotion promotion to be encashed after registration
     * @throws RegistrationException thrown if a technical problem appears
     * during registration
     * @throws VoucherPaymentDuplicateException
     */
    public void registerAndEncashVoucher(User user, Locale preferredLocale, String activationLink, Promotion promotion)
            throws RegistrationException, VoucherPaymentDuplicateException;

    /**
     * Searching for matching user(profiles)
     *
     * @param user searching user
     * @param searchProfile search params
     * @param first paging start
     * @param count paging size
     * @return list of results
     */
    public List<UserSearchResult> search(final User user, final SearchProfile searchProfile, final long first, final long count);

    /**
     * Count matching user(profiles)
     *
     * @param user searching user
     * @param searchProfile search params
     * @return number of results
     */
    public long count(User user, SearchProfile searchProfile);
}
