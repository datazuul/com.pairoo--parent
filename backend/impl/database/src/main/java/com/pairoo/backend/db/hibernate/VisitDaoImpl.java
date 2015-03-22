package com.pairoo.backend.db.hibernate;

import com.datazuul.framework.persistence.dao.hibernate.ExtendedGenericDaoImpl;
import com.pairoo.backend.dao.VisitDao;
import com.pairoo.domain.Favorite;
import com.pairoo.domain.ImageEntry;
import com.pairoo.domain.User;
import com.pairoo.domain.Visit;
import com.pairoo.domain.search.VisitSearchResult;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;

/**
 * @author Ralf Eichinger
 */
public class VisitDaoImpl extends ExtendedGenericDaoImpl<Visit, Long> implements VisitDao {

    @Override
    public List<VisitSearchResult> search(User user, long first, long count) {
        Criteria rootCriteria = createSearchCriteria(user);

        // create projection list for result transformer
        ProjectionList pl = Projections.projectionList();
        pl.add(Projections.property("id").as("id"));
        pl.add(Projections.property("v.id").as("visitorId"));
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

        rootCriteria.setProjection(pl);
        rootCriteria.setResultTransformer(new AliasToBeanResultTransformer(VisitSearchResult.class));

        // ordering
        rootCriteria.addOrder(Order.desc("timeStamp"));

        // paging
        rootCriteria.setFirstResult((int) first);
        rootCriteria.setMaxResults((int) count);

        // do search
        List<VisitSearchResult> list = rootCriteria.list();

        for (VisitSearchResult result : list) {
            List<ImageEntry> listOfImageEntries = searchImageEntry(result.getVisitorId());
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

        return list;
    }

    private Criteria createSearchCriteria(User user) throws HibernateException {
        Criteria rootCriteria = getSession().createCriteria(Visit.class);
        // alias (joins)
        // TODO: if an alias references a null object the User is
        // not in results (even if it would match)!!!
        rootCriteria.createAlias("visitedUser", "u");
        rootCriteria.createAlias("visitor", "v");
        rootCriteria.createAlias("v.searchProfile", "sp");
        rootCriteria.createAlias("v.userAccount", "ua");
        rootCriteria.createAlias("v.userProfile", "up");
        rootCriteria.createAlias("up.appearance", "app");
        rootCriteria.createAlias("up.geoLocation", "geo");
        rootCriteria.createAlias("up.lifeStyle", "ls");

        // adding restrictions (search params / where clauses) to criteria
        rootCriteria.add(Restrictions.eq("u.id", user.getId()));

        return rootCriteria;
    }

    @Override
    public long count(User user) {
        Criteria rootCriteria = createSearchCriteria(user);
        return (long) rootCriteria.setProjection(Projections.rowCount()).uniqueResult();
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
