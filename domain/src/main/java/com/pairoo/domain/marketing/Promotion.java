package com.pairoo.domain.marketing;

import java.util.Date;

import com.datazuul.framework.domain.AbstractPersistentDomainObject;
import com.pairoo.domain.Product;
import com.pairoo.domain.UserAccount;
import com.pairoo.domain.marketing.enums.PromotionType;

/**
 * @author Ralf Eichinger
 */
public class Promotion extends AbstractPersistentDomainObject {
    private static final long serialVersionUID = 1L;

    private PromotionType promotionType;
    private String code;
    private UserAccount userAccount;
    private Product product;
    private Date validFrom;
    private Date validTo;
    private Date timeStamp;
    private Boolean used;

    /**
     * @param code
     *            the code to set
     */
    public void setCode(String code) {
	this.code = code;
    }

    /**
     * @return the code
     */
    public String getCode() {
	return code;
    }

    /**
     * @param timeStamp
     *            the timeStamp to set
     */
    public void setTimeStamp(Date timeStamp) {
	this.timeStamp = timeStamp;
    }

    /**
     * @return the timeStamp
     */
    public Date getTimeStamp() {
	return timeStamp;
    }

    public Product getProduct() {
	return product;
    }

    public void setProduct(Product product) {
	this.product = product;
    }

    public UserAccount getUserAccount() {
	return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
	this.userAccount = userAccount;
    }

    public Date getValidFrom() {
	return validFrom;
    }

    public void setValidFrom(Date validFrom) {
	this.validFrom = validFrom;
    }

    public Date getValidTo() {
	return validTo;
    }

    public void setValidTo(Date validTo) {
	this.validTo = validTo;
    }

    public PromotionType getPromotionType() {
	return promotionType;
    }

    public void setPromotionType(PromotionType promotionType) {
	this.promotionType = promotionType;
    }

    /**
     * @return the used
     */
    public Boolean isUsed() {
	return used;
    }

    /**
     * @param used
     *            the used to set
     */
    public void setUsed(Boolean used) {
	this.used = used;
    }
}
