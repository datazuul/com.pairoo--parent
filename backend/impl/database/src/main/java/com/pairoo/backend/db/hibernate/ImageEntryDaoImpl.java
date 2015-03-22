package com.pairoo.backend.db.hibernate;

import com.datazuul.framework.persistence.dao.hibernate.ExtendedGenericDaoImpl;
import com.pairoo.backend.dao.ImageEntryDao;
import com.pairoo.domain.ImageEntry;

/**
 * @author Ralf Eichinger
 */
public class ImageEntryDaoImpl extends ExtendedGenericDaoImpl<ImageEntry, Long> implements ImageEntryDao {
}
