package com.pairoo.backend.db.hibernate.querybuilder;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.type.AbstractStandardBasicType;
import org.hibernate.type.StandardBasicTypes;

import com.datazuul.framework.domain.QueryParam;
import com.pairoo.domain.User;

/**
 * @author Ralf Eichinger
 */
public class UserDaoQueryBuilder {
    private List<String> parameters;
    private List<AbstractStandardBasicType> types;
    private boolean count;
    private User filter = new User();
    private QueryParam queryParam;

    public String buildHql() {
	parameters = new ArrayList<String>();
	types = new ArrayList<AbstractStandardBasicType>();
	final StringBuilder hql = new StringBuilder();
	addCountClause(hql);
	hql.append("from users target where 1=1 ");
	addMatchingCondition(hql, filter.getFirstname(), "firstname");
	addMatchingCondition(hql, filter.getLastname(), "lastname");
	addMatchingCondition(hql, filter.getEmail(), "email");
	addOrderByClause(hql);
	return hql.toString();
    }

    private void addCountClause(final StringBuilder hql) {
	if (count) {
	    hql.append("select count(*) ");
	}
    }

    private void addMatchingCondition(final StringBuilder hql, final String value, final String name) {
	if (value != null) {
	    hql.append("and upper(target.");
	    hql.append(name);
	    hql.append(") like (?)");
	    parameters.add("%" + value.toUpperCase() + "%");
	    types.add(StandardBasicTypes.STRING);
	}
    }

    private void addOrderByClause(final StringBuilder hql) {
	if (!count && queryParam != null && queryParam.hasSort()) {
	    hql.append("order by upper(target.");
	    hql.append(queryParam.getSort());
	    hql.append(") ");
	    hql.append(queryParam.isSortAsc() ? "asc" : "desc");
	}
    }

    public void setQueryParam(final QueryParam queryParam) {
	this.queryParam = queryParam;
    }

    public void setFilter(final User filter) {
	if (filter == null) {
	    throw new IllegalArgumentException("Null value not allowed.");
	}
	this.filter = filter;
    }

    public void setCount(final boolean count) {
	this.count = count;
    }

    public String[] getParameters() {
	return parameters.toArray(new String[0]);
    }

    public AbstractStandardBasicType[] getTypes() {
	return types.toArray(new AbstractStandardBasicType[0]);
    }
}
