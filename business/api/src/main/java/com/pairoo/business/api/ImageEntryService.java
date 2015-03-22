package com.pairoo.business.api;

import java.util.List;

import com.datazuul.framework.business.services.DomainObjectService;
import com.pairoo.domain.ImageEntry;

/**
 * @author Ralf Eichinger
 */
public interface ImageEntryService extends DomainObjectService<Long, ImageEntry> {
    public static final String BEAN_ID = "imageEntryService";

    public int getMaxCount();

    /**
     * @param imageEntries
     *            list of all image entries
     * @return number of visible image entries
     */
    public int getVisibleCount(List<ImageEntry> imageEntries);

    /**
     * Load image entry specified by its unique repository id.
     * 
     * @param repositoryId unique repository id
     * @return image entry
     */
    public ImageEntry getByRepositoryId(String repositoryId);
}
