package com.pairoo.domain;

import java.util.Date;

import com.datazuul.framework.domain.AbstractPersistentDomainObject;
import com.pairoo.domain.enums.MediaType;

/**
 * @author Ralf Eichinger
 */
public class ImageEntry extends AbstractPersistentDomainObject {
    private static final long serialVersionUID = 1L;

    private boolean profileImage;
    private boolean visible;
    private String clientFileName;
    private Date lastModified;
    private MediaType mediaType;
    private int height;
    private int width;
    private String repositoryId;

    /**
     * Constructs a new instance.
     */
    public ImageEntry() {
	super();
	setLastModified(new Date());
	setId(null);
    }

    public Date getLastModified() {
	return lastModified;
    }

    public void setLastModified(final Date lastModified) {
	this.lastModified = lastModified;
    }

    /**
     * @param clientFileName
     *            the clientFileName to set
     */
    public void setClientFileName(final String clientFileName) {
	this.clientFileName = clientFileName;
    }

    /**
     * @return the clientFileName
     */
    public String getClientFileName() {
	return clientFileName;
    }

    /**
     * @param height
     *            the height to set
     */
    public void setHeight(final int height) {
	this.height = height;
    }

    /**
     * @return the height
     */
    public int getHeight() {
	return height;
    }

    /**
     * @param width
     *            the width to set
     */
    public void setWidth(final int width) {
	this.width = width;
    }

    /**
     * @return the width
     */
    public int getWidth() {
	return width;
    }

    /**
     * @param mediaType
     *            the mediaType to set
     */
    public void setMediaType(final MediaType mediaType) {
	this.mediaType = mediaType;
    }

    /**
     * @return the mediaType
     */
    public MediaType getMediaType() {
	return mediaType;
    }

    /**
     * @param profileImage
     *            the profileImage to set
     */
    public void setProfileImage(final boolean profileImage) {
	this.profileImage = profileImage;
    }

    /**
     * @return the profileImage
     */
    public boolean isProfileImage() {
	return profileImage;
    }

    /**
     * @param repositoryId
     *            the repositoryId to set
     */
    public void setRepositoryId(final String repositoryId) {
	this.repositoryId = repositoryId;
    }

    /**
     * @return the repositoryId
     */
    public String getRepositoryId() {
	return repositoryId;
    }

    public void setVisible(final boolean visible) {
	this.visible = visible;
    }

    public boolean isVisible() {
	return visible;
    }
}
