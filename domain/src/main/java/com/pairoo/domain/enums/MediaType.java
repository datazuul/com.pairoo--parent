package com.pairoo.domain.enums;

/**
 * see also MediaType in springframework...
 * @author Ralf Eichinger
 */
public enum MediaType {
	IMAGE_GIF("image", "gif", "gif"), IMAGE_JPEG("image", "jpeg", "jpg"), IMAGE_PNG("image", "png", "png");

	private String type;
	private String subType;
	private String fileSuffix;

	private MediaType(String type, String subType, String fileSuffix) {
		this.type = type;
		this.subType = subType;
		this.fileSuffix = fileSuffix;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return the subType
	 */
	public String getSubType() {
		return subType;
	}

	/**
	 * @return the contenttype
	 */
	public String getContentType() {
		return type + "/" + subType;
	}

	/**
	 * @return the fileSuffix
	 */
	public String getFileSuffix() {
		return fileSuffix;
	}
}
