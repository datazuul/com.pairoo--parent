package com.pairoo.business.api;

import java.awt.image.BufferedImage;
import java.io.IOException;

import com.pairoo.domain.ImageEntry;
import com.pairoo.domain.enums.ImageEntryType;

/**
 * @author Ralf Eichinger
 */
public interface ImageService {
    public static final String BEAN_ID = "imageService";

    /**
     * Save an image.
     * 
     * @param imageEntry
     *            image meta data
     * @param imageBytes
     *            image raw data
     * @throws IOException
     *             thrown if image can not be saved
     */
    public void save(ImageEntry imageEntry, ImageEntryType imageEntryType, byte[] imageBytes) throws IOException;

    public void save(ImageEntry imageEntry, ImageEntryType imageEntryType, BufferedImage bufferedImage)
	    throws IOException;

    public BufferedImage load(ImageEntry imageEntry, ImageEntryType imageEntryType) throws IOException;

    public void delete(ImageEntry imageEntry);

    public void deleteTemporary(ImageEntry imageEntry);

    public void saveTemporary(ImageEntry imageEntry, ImageEntryType imageEntryType, byte[] imageBytes)
	    throws IOException;

    public void saveTemporary(ImageEntry imageEntry, ImageEntryType imageEntryType, BufferedImage bufferedImage)
	    throws IOException;

    public BufferedImage loadTemporary(ImageEntry imageEntry, ImageEntryType imageEntryType) throws IOException;

    public double getMaxFileSizeInMB();
}
