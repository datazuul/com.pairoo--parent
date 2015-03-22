package com.pairoo.backend.db.hibernate;

import com.datazuul.framework.persistence.dao.hibernate.ExtendedGenericDaoImpl;
import com.pairoo.backend.dao.ProductDao;
import com.pairoo.domain.Product;

/**
 * @author Ralf Eichinger
 */
public class ProductDaoImpl extends ExtendedGenericDaoImpl<Product, Long> implements ProductDao {
}
