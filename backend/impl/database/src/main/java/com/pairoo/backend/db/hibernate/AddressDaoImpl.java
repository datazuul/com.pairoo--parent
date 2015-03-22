package com.pairoo.backend.db.hibernate;

import com.datazuul.framework.persistence.dao.hibernate.ExtendedGenericDaoImpl;
import com.pairoo.backend.dao.AddressDao;
import com.pairoo.domain.Address;

/**
 * @author ralf
 */
public class AddressDaoImpl extends ExtendedGenericDaoImpl<Address, Long> implements AddressDao {

}
