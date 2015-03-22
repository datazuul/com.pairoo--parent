package com.pairoo.backend.dao;

import java.awt.image.BufferedImage;
import java.io.IOException;

import com.pairoo.domain.ImageEntry;
import com.pairoo.domain.enums.ImageEntryType;

/**
 * @author Ralf Eichinger
 */
public interface ImageDao {

    /**
     * Saves (not existing id) or updates (existing id) an image.
     * 
     * @param imageEntry
     *            image metadata
     * @param imageEntryType
     * @param imageBytes
     *            image data
     * @param quality
     *            a quality value between 0.0 and 1.0
     */
    public void save(ImageEntry imageEntry, final ImageEntryType imageEntryType, final byte[] imageBytes,
	    final float quality);

    /**
     * @param imageEntry
     *            image metadata
     * @param imageEntryType
     * @return image data as buffered image
     * @throws IOException
     */
    public BufferedImage load(ImageEntry imageEntry, ImageEntryType imageEntryType) throws IOException;

    public BufferedImage loadTemporary(ImageEntry imageEntry, ImageEntryType imageEntryType) throws IOException;

    public void delete(ImageEntry imageEntry);

    public void deleteTemporary(ImageEntry imageEntry);

    /**
     * Saves (not existing id) or updates (existing id) an image.
     * 
     * @param imageEntry
     *            image metadata
     * @param imageEntryType
     * @param bufferedImage
     *            image data as buffered image
     * @param quality
     *            a quality value between 0.0 and 1.0
     */
    public void save(ImageEntry imageEntry, ImageEntryType imageEntryType, BufferedImage bufferedImage,
	    final float quality);

    public void saveTemporary(ImageEntry imageEntry, ImageEntryType imageEntryType, BufferedImage bufferedImage,
	    float quality);
}
