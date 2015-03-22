package com.pairoo.frontend.webapp.wicket.components;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pairoo.business.api.ImageEntryService;
import com.pairoo.business.api.ImageService;
import com.pairoo.domain.ImageEntry;
import com.pairoo.domain.enums.ImageEntryType;
import com.pairoo.domain.enums.MediaType;

/**
 * The important thing when creating the resource reference is to give it unique
 * ResourceReference.Key and provide implementation of #getResource() method:
 * ImageResourceReference.java
 */
public class ImageResourceReference extends ResourceReference {
    private static final long serialVersionUID = 1L;
    static final Logger LOGGER = LoggerFactory.getLogger(ImageResourceReference.class);

    @SpringBean(name = ImageEntryService.BEAN_ID)
    private ImageEntryService imageEntryService;
    // FIXME java.io.NotSerializableException:
    // com.pairoo.business.services.impl.ImageEntryServiceImpl
    @SpringBean(name = ImageService.BEAN_ID)
    private ImageService imageService;

    public ImageResourceReference() {
	// this creates a Key with scope 'ImageResourceReference.class'
	// and a name 'image'
	super(ImageResourceReference.class, "image");
	Injector.get().inject(this);
    }

    @Override
    public IResource getResource() {
	return new ImageResource();
    }

    private class ImageResource extends DynamicImageResource {
	private static final long serialVersionUID = 1L;

	@Override
	protected byte[] getImageData(final Attributes attributes) {

	    final PageParameters parameters = attributes.getParameters();
	    final org.apache.wicket.util.string.StringValue idStr = parameters.get("id");
	    final org.apache.wicket.util.string.StringValue sizeStr = parameters.get("size");

	    LOGGER.debug("getting image repository id [{}] size [{}]", new Object[] { idStr.toString(), sizeStr.toString() });

	    byte[] imageBytes = null;

	    if (idStr.isEmpty() == false && sizeStr.isEmpty() == false) {
		ImageEntry imageEntry = null;
		try {
		    final String repositoryId = idStr.toString();
		    imageEntry = imageEntryService.getByRepositoryId(repositoryId);
		} catch (final Exception e) {
		    e.printStackTrace();
		    return null;
		}
		imageBytes = getImageAsBytes(imageEntry, sizeStr);
	    }
	    return imageBytes;
	}

	private byte[] getImageAsBytes(final ImageEntry imageEntry, final StringValue sizeStr) {
	    ImageEntryType imageEntryType = ImageEntryType.NORMAL;
	    if (imageEntry != null) {
		try {
		    final String size = sizeStr.toString();
		    if (ImageEntryType.MIDDLE.name().equals(size)) {
			imageEntryType = ImageEntryType.MIDDLE;
		    } else if (ImageEntryType.NORMAL.name().equals(size)) {
			imageEntryType = ImageEntryType.NORMAL;
		    } else if (ImageEntryType.RAW_ORIGINAL.name().equals(size)) {
			imageEntryType = ImageEntryType.RAW_ORIGINAL;
		    } else if (ImageEntryType.THUMBNAIL.name().equals(size)) {
			imageEntryType = ImageEntryType.THUMBNAIL;
		    }
		    imageEntry.setMediaType(MediaType.IMAGE_JPEG);
		    final BufferedImage bufferedImage = imageService.load(imageEntry, imageEntryType);
		    if (bufferedImage != null) {
			final ByteArrayOutputStream baos = new ByteArrayOutputStream(1000);
			try {
			    ImageIO.write(bufferedImage, MediaType.IMAGE_JPEG.getSubType(), baos);
			} catch (final IOException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
			return baos.toByteArray();
		    }
		} catch (final Exception e) {
		    LOGGER.error("ERROR loading image {} of type {}", imageEntry.getRepositoryId(),
			    imageEntryType.name());
		    e.printStackTrace();
		    return null;
		}
	    }
	    return null;
	}

	@Override
	public boolean equals(final Object that) {
	    return that instanceof ImageResource;
	}
    }
}