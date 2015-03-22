package com.pairoo.business.services.impl;

import com.datazuul.framework.business.services.persistence.AbstractDomainObjectServiceImpl;
import com.googlecode.genericdao.search.Search;
import com.pairoo.backend.dao.MembershipDao;
import com.pairoo.business.api.MembershipService;
import com.pairoo.business.api.UserAccountService;
import com.pairoo.domain.Membership;
import com.pairoo.domain.Product;
import com.pairoo.domain.UserAccount;
import com.pairoo.domain.enums.MembershipStatus;
import com.pairoo.domain.enums.Role;
import com.pairoo.domain.payment.Transaction;
import com.pairoo.domain.payment.enums.StatusType;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MembershipServiceImpl extends AbstractDomainObjectServiceImpl<Long, Membership> implements
        MembershipService {

  static final Logger LOGGER = LoggerFactory.getLogger(MembershipServiceImpl.class);
  private final UserAccountService userAccountService;

  /**
   * Constructor needed for test handing over a mock dao.
   *
   * @param dao the data access interface
   */
  public MembershipServiceImpl(final MembershipDao dao, final UserAccountService userAccountService) {
    super(dao);
    this.userAccountService = userAccountService;
  }

  @Override
  public void activateMembership(final UserAccount userAccount) {
    final Membership membership = getFirstMembership(userAccount);
    membership.setStatus(MembershipStatus.ACTIVE);
    dao.save(membership);
    LOGGER.info("activated membership of useraccount = {}", userAccount.getUsername());
  }

  @Override
  public void addMembership(UserAccount userAccount, Membership membership, MembershipStatus membershipStatus) {
    // set accepted terms
    membership.setAcceptedTerms(true);

    membership.setUserAccount(userAccount);

    membership.setStatus(membershipStatus);

    // set start date immediately after that
    Calendar calendarStart = Calendar.getInstance();

    // get end date of the most future membership
    Membership mostFutureMembership = getLastMembership(userAccount);
    if (mostFutureMembership != null) {
      Date mostFutureEndDate = mostFutureMembership.getEndDate();
      calendarStart.setTime(mostFutureEndDate);
      calendarStart.add(Calendar.DAY_OF_MONTH, 1);
    }

    calendarStart.set(Calendar.HOUR_OF_DAY, 0);
    calendarStart.set(Calendar.MINUTE, 0);
    calendarStart.set(Calendar.SECOND, 0);
    calendarStart.set(Calendar.MILLISECOND, 0);

    Date startDate = calendarStart.getTime();
    membership.setStartDate(startDate);

    // set end date
    Product product = membership.getProduct();
    if (product != null && product.getDuration() != null) {
      int monthsCounter = product.getDuration().getMonthlyRateFactor();
      Calendar calendarEnd = Calendar.getInstance();
      calendarEnd.setTime(membership.getStartDate());
      calendarEnd.add(Calendar.MONTH, monthsCounter);
      calendarEnd.set(Calendar.HOUR_OF_DAY, 24);
      calendarEnd.set(Calendar.MINUTE, 0);
      calendarEnd.set(Calendar.SECOND, 0);
      calendarEnd.set(Calendar.MILLISECOND, 0);

      Date endDate = calendarEnd.getTime();
      membership.setEndDate(endDate);
    }
    dao.save(membership);

        // product stuff
    // 0. add roles
    if (product.getRole() != null) {
      String role = product.getRole().getCode();

            // TODO better solution? because of:
      // org.hibernate.NonUniqueObjectException: a different object with
      // the same identifier
      // value was already associated with the session:
      // [com.pairoo.domain.UserAccount#41]
      // I do a fetch to get object from session
      userAccount = userAccountService.get(userAccount.getId());
      userAccountService.addRole(userAccount, role);
      userAccountService.save(userAccount);
            // Roles roles = userAccount.getRoles();
      // if (roles == null) {
      // roles = new Roles(Roles.FREE);
      // LOGGER.info("adding role FREE to useraccount {}",
      // new Object[] { userAccount.getUsername() });
      // }
      // if (!roles.hasRole(role)) {
      // roles.add(role);
      // LOGGER.info("adding role {} to useraccount {}", new Object[] {
      // role, userAccount.getUsername() });
      // }
      // userAccount.setRoles(roles);

      LOGGER.info("added role {} to useraccount {}, now having roles {}",
              new Object[]{role, userAccount.getUsername(), userAccount.getRoles().toString()});
      // 1. extend end date
      if (Role.PREMIUM.equals(product.getRole()) && membership.getEndDate() != null) {
        // set premium end date
        userAccount.setPremiumEndDate(membership.getEndDate());
        userAccountService.save(userAccount);
        LOGGER.info("set premium end date to {}, having roles {}", new Object[]{
          membership.getEndDate().toString(), userAccount.getRoles().toString()});
      } else {
        LOGGER.info("not setting premium enddate");
      }
    } else {
      LOGGER.warn("product has no role! product id = {}. Not updating useraccount roles,", product.getId());
    }
  }

  @Override
  public Membership getCurrentMembership(UserAccount userAccount) {
    Search search = new Search(Membership.class);

    // all memberships of this useraccount
    search.addFilterEqual("userAccount", userAccount);

    // only when having end date (STANDARD does not have one ;-))
    search.addFilterNotEmpty("endDate");

    Date now = new Date();
    search.addFilterLessOrEqual("startDate", now);
    search.addFilterGreaterOrEqual("endDate", now);
    search.addSortDesc("endDate");

    Membership currentMembership;
    List<Membership> memberships = dao.search(search);
    if (memberships.isEmpty()) {
      search = new Search(Membership.class);
      search.addFilterEqual("userAccount", userAccount);
      search.addFilterEqual("product.role", Role.STANDARD);
      currentMembership = dao.searchUnique(search);
    } else {
      currentMembership = memberships.get(0);
    }
    if (currentMembership.getProduct() != null) {
      LOGGER.info("current membership has role " + currentMembership.getProduct().getRole());
    }
    return currentMembership;
  }

  @Override
  public Membership getLastMembership(UserAccount userAccount) {
    final Search search = new Search(Membership.class);

    // all memberships of this useraccount
    search.addFilterEqual("userAccount", userAccount);

    // only when having end date (STANDARD does not have one ;-))
    search.addFilterNotEmpty("endDate");

    // sort end date decreasing
    search.addSortDesc("endDate");

    List<Membership> memberships = dao.search(search);
    if (memberships.isEmpty()) {
      return null;
    }
    return memberships.get(0);
  }

  @Override
  public Membership getFirstMembership(UserAccount userAccount) {
    final Search search = new Search(Membership.class);

    // all memberships of this useraccount
    search.addFilterEqual("userAccount", userAccount);

    // sort end date decreasing
    search.addSortAsc("startDate");

        // FIXME TransientObjectException: object references an unsaved
    // transient instance - save the transient instance before flushing:
    // com.pairoo.domain.UserAccount
    List<Membership> memberships = dao.search(search);
    if (memberships.isEmpty()) {
      return null;
    }
    return memberships.get(0);
  }
}
