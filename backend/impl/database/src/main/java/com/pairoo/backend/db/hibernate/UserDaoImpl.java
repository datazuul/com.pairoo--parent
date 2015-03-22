package com.pairoo.backend.db.hibernate;

import com.datazuul.framework.persistence.dao.hibernate.ExtendedGenericDaoImpl;
import com.pairoo.backend.dao.UserDao;
import com.pairoo.backend.dao.search.DaoUserSearchParams;
import com.pairoo.domain.ImageEntry;
import com.pairoo.domain.User;
import com.pairoo.domain.enums.ProfilePictureType;
import com.pairoo.domain.enums.Role;
import com.pairoo.domain.search.UserSearchResult;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.AliasToBeanResultTransformer;

/**
 * @author Ralf Eichinger
 */
public class UserDaoImpl extends ExtendedGenericDaoImpl<User, Long> implements UserDao {

    @Override
    public List<UserSearchResult> search(DaoUserSearchParams searchParams, long first, long count) {
	Criteria rootCriteria = createSearchCriteria(searchParams);

	// rootCriteria.setFetchMode("up.imageEntries", FetchMode.JOIN);

	// create projection list for result transformer
	ProjectionList pl = Projections.projectionList();
	pl.add(Projections.property("id").as("id"));
	pl.add(Projections.property("sp.partnershipType").as("searchesPartnershipType"));
	pl.add(Projections.property("ua.lastLogin").as("lastLogin"));
	pl.add(Projections.property("ua.online").as("online"));
	pl.add(Projections.property("ua.premiumEndDate").as("premiumEndDate"));
	pl.add(Projections.property("ua.username").as("username"));
	pl.add(Projections.property("up.birthdate").as("birthdate"));
	pl.add(Projections.property("up.familyStatus").as("familyStatusType"));
	pl.add(Projections.property("up.numberOfKidsType").as("numberOfKidsType"));
	pl.add(Projections.property("up.partnerType").as("partnerType"));
	pl.add(Projections.property("up.geoLocation").as("geoLocation"));
	pl.add(Projections.property("app.ethnicity").as("ethnicity"));
	pl.add(Projections.property("app.height").as("height"));
	pl.add(Projections.property("ls.smokeType").as("smokeType"));
	// pl.add(Projections.property("up.imageEntries").as("images"));

	rootCriteria.setProjection(pl);
	rootCriteria.setResultTransformer(new AliasToBeanResultTransformer(UserSearchResult.class));

	// ordering
	rootCriteria.addOrder(Order.desc("ua.lastLogin"));

	// paging
	rootCriteria.setFirstResult((int) first);
	rootCriteria.setMaxResults((int) count);

	// do search
	List<UserSearchResult> list = rootCriteria.list();

	for (UserSearchResult result : list) {
	    List<ImageEntry> listOfImageEntries = searchImageEntry(result.getId());
	    if (!listOfImageEntries.isEmpty()) {
		for (ImageEntry imageEntry : listOfImageEntries) {
		    if (imageEntry.isProfileImage() && imageEntry.isVisible()) {
			result.setProfileImageEntry(imageEntry);
			result.setImages(null);
			break;
		    }
		}
	    }
	}

	// for (UserSearchResult userSearchResult : list) {
	// List<ImageEntry> imageEntries = userSearchResult.getImages();
	// if (imageEntries != null && !imageEntries.isEmpty()) {
	// for (ImageEntry imageEntry : imageEntries) {
	// if (imageEntry.isProfileImage() && imageEntry.isVisible()) {
	// userSearchResult.setProfileImageEntry(imageEntry);
	// userSearchResult.setImages(null);
	// break;
	// }
	// }
	// }

	//
	// Criteria userCriteria = getSession().createCriteria(User.class);
	// userCriteria.createAlias("userProfile", "up");
	// userCriteria.createAlias("up.imageEntries", "im");
	//
	// userCriteria.add(Restrictions.eq("id", userSearchResult.getId()));

	// userCriteria.add(Restrictions.eq("im.profileImage", true));
	// userCriteria.add(Restrictions.eq("im.visible", true));

	// userCriteria.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	// ProjectionList plImageEntry = Projections.projectionList();

	// plImageEntry.add(Projections.property("up.imageEntries").as("imageEntries"));
	// plImageEntry.add(Projections.property("up.imageEntries").as("im"));
	// userCriteria.setProjection(plImageEntry);

	// userCriteria.setResultTransformer(new
	// AliasToBeanResultTransformer(UserProfile.class));
	// List<UserProfile> userProfiles = userCriteria.list();
	// for (UserProfile userProfile : userProfiles) {
	// List<ImageEntry> imageEntries = userProfile.getImageEntries();
	// if (imageEntries != null && !imageEntries.isEmpty()) {
	// profileImageEntry = imageEntries.get(0);
	// }
	// }

	// if (userProfile != null) {
	// List<ImageEntry> imageEntries = userProfile.getImageEntries();
	// if (imageEntries != null && !imageEntries.isEmpty()) {
	// profileImageEntry = imageEntries.get(0);
	// }
	// }
	// List<Object[]> result = userCriteria.list();
	// for (Object[] row : result) {
	// Long imageEntryId = (Long) row[0];
	//
	// // ...
	// }
	// Object imageEntryId = userCriteria.uniqueResult();
	// System.out.println(imageEntryId);

	// List<ImageEntry> imagEntries = userCriteria.list();
	// ImageEntry profileImageEntry = null;
	// if (!imagEntries.isEmpty()) {
	// profileImageEntry = imagEntries.get(0);
	// }

	// userCriteria.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	// List result = userCriteria.list();
	// for (Object aResult : result) {
	// Map map = (Map) aResult;
	// // profileImageEntry = (ImageEntry) map.get("up.imageEntries");
	// List<ImageEntry> imageEntries = (List<ImageEntry>) map.get("im");
	// if (imageEntries != null && !imageEntries.isEmpty()) {
	// profileImageEntry = imageEntries.get(0);
	// }
	// }

	// userSearchResult.setProfileImageEntry(profileImageEntry);
	// }

	return list;
    }

    @Override
    public long count(DaoUserSearchParams searchParams) {
	Criteria rootCriteria = createSearchCriteria(searchParams);
	return (long) rootCriteria.setProjection(Projections.rowCount()).uniqueResult();
    }

    private Criteria createSearchCriteria(DaoUserSearchParams searchParams) throws HibernateException {
	Criteria rootCriteria = getSession().createCriteria(User.class);
	// alias (joins)
	// TODO: if an alias references a null object the User is
	// not in results (even if it would match)!!!
	rootCriteria.createAlias("searchProfile", "sp");
	rootCriteria.createAlias("userAccount", "ua");
	rootCriteria.createAlias("userProfile", "up");
	rootCriteria.createAlias("up.appearance", "app");
	rootCriteria.createAlias("up.geoLocation", "geo");
	// rootCriteria.createAlias("up.imageEntries", "img");
	rootCriteria.createAlias("up.lifeStyle", "ls");

	// adding restrictions (search params / where clauses) to criteria
	rootCriteria.add(Restrictions.ne("id", searchParams.getIdOfSearchingUser()));

	// not an ADMIN
	rootCriteria.add(Restrictions.not(Restrictions.ilike("ua.Roles.roles", Role.ADMIN.getCode(), MatchMode.ANYWHERE)));

	Criterion noBirthdate = Restrictions.isNull("up.birthdate");
	if (searchParams.getBirthdateOldest() != null) {
	    Criterion oldestBirthdate = Restrictions.ge("up.birthdate", searchParams.getBirthdateOldest());
	    rootCriteria.add(Restrictions.or(oldestBirthdate, noBirthdate));
	}
	if (searchParams.getBirthdateYoungest() != null) {
	    Criterion youngestBirthdate = Restrictions.le("up.birthdate", searchParams.getBirthdateYoungest());
	    rootCriteria.add(Restrictions.or(youngestBirthdate, noBirthdate));
	}
	// empty birthdate
	// rootCriteria.add(Restrictions.or(Restrictions.isNull("up.birthdate")));
	// Criteria criteria = getSession().createCriteria(clazz);
	// Criterion rest1 = Restrictions.and(Restrictions.eq("A", "X"),
	// Restrictions.in("B", Arrays.asList("X", "Y")));
	// Criterion rest2 = Restrictions.and(Restrictions.eq("A", "Y"),
	// Restrictions.eq("B", "Z"));
	// criteria.add(Restrictions.or(rest1, rest2));

	if (searchParams.getCountry() != null) {
	    rootCriteria.add(Restrictions.eq("geo.country", searchParams.getCountry()));
	}
	if (searchParams.getEthnicities() != null) {
	    rootCriteria.add(Restrictions.in("app.ethnicity", searchParams.getEthnicities()));
	}
	if (searchParams.getMinLatitude() != null && searchParams.getMinLongitude() != null
		&& searchParams.getMaxLatitude() != null && searchParams.getMaxLongitude() != null) {
	    rootCriteria.add(Restrictions.between("geo.latitude", searchParams.getMinLatitude(),
		    searchParams.getMaxLatitude()));
	    rootCriteria.add(Restrictions.between("geo.longitude", searchParams.getMinLongitude(),
		    searchParams.getMaxLongitude()));
	}
	if (searchParams.getPartnerType() != null) {
	    rootCriteria.add(Restrictions.eq("up.partnerType", searchParams.getPartnerType()));
	}
	if (searchParams.getProfilePictureType() != null) {
	    ProfilePictureType profilePictureType = searchParams.getProfilePictureType();
	    Criteria subCriteria = rootCriteria.createCriteria("up.imageEntries", "ie", JoinType.FULL_JOIN);
	    if (ProfilePictureType.WITH_PICTURES.equals(profilePictureType)) {
		subCriteria.add(Restrictions.eq("profileImage", Boolean.TRUE));
		subCriteria.add(Restrictions.eq("visible", Boolean.TRUE));
	    } else {
		subCriteria.add(Restrictions.or(Restrictions.eq("profileImage", Boolean.FALSE),
			Restrictions.eq("visible", Boolean.FALSE)));
	    }
	}
	if (searchParams.getSmokeType() != null) {
	    rootCriteria.add(Restrictions.eq("ls.smokeType", searchParams.getSmokeType()));
	}

	if (searchParams.getZipcodeStartsWith() != null) {
	    rootCriteria.add(Restrictions.ilike("geo.zipcode", searchParams.getZipcodeStartsWith(), MatchMode.START));
	}

	return rootCriteria;
    }

    public List<ImageEntry> searchImageEntry(Long userId) {
	Criteria rootCriteria = getSession().createCriteria(User.class);
	rootCriteria.createAlias("userProfile", "up");
	rootCriteria.add(Restrictions.idEq(userId));
	Criteria subCriteria = rootCriteria.createCriteria("up.imageEntries", "ie");

	ProjectionList pl = Projections.projectionList();
	pl.add(Projections.property("ie.id").as("id"));
	pl.add(Projections.property("ie.uuid").as("uuid"));
	pl.add(Projections.property("ie.height").as("height"));
	pl.add(Projections.property("ie.mediaType").as("mediaType"));
	pl.add(Projections.property("ie.profileImage").as("profileImage"));
	pl.add(Projections.property("ie.repositoryId").as("repositoryId"));
	pl.add(Projections.property("ie.visible").as("visible"));
	pl.add(Projections.property("ie.width").as("width"));
	rootCriteria.setProjection(pl);
	rootCriteria.setResultTransformer(new AliasToBeanResultTransformer(ImageEntry.class));

	// do search
	return rootCriteria.list();
    }
}
