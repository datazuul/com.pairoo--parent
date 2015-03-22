package com.pairoo.backend.db.hibernate;

import com.datazuul.framework.persistence.dao.hibernate.ExtendedGenericDaoImpl;
import com.pairoo.backend.dao.MembershipDao;
import com.pairoo.domain.Membership;

/**
 * @author Ralf Eichinger
 */
public class MembershipDaoImpl extends ExtendedGenericDaoImpl<Membership, Long> implements MembershipDao {
}
