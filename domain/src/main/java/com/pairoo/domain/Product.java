package com.pairoo.domain;

import java.util.Date;

import com.datazuul.framework.domain.AbstractPersistentDomainObject;
import com.pairoo.domain.enums.ProductDurationType;
import com.pairoo.domain.enums.Role;

public class Product extends AbstractPersistentDomainObject {
    private static final long serialVersionUID = 1L;

    private Date startDate; // when product was introduced
    private Date endDate; // until when the product is available
    private ProductDurationType duration;
    private Float monthlyRate; // monthly cost
    private boolean abo;
    private Role role = Role.STANDARD;

    public ProductDurationType getDuration() {
	return duration;
    }

    public void setDuration(ProductDurationType duration) {
	this.duration = duration;
    }

    public Date getStartDate() {
	return startDate;
    }

    public void setStartDate(Date startDate) {
	this.startDate = startDate;
    }

    public Date getEndDate() {
	return endDate;
    }

    public void setEndDate(Date endDate) {
	this.endDate = endDate;
    }

    /**
     * @return monthly rate in standard currency (EUR)
     */
    public Float getMonthlyRate() {
	return monthlyRate;
    }

    public void setMonthlyRate(Float monthlyRate) {
	this.monthlyRate = monthlyRate;
    }

    public boolean isAbo() {
	return abo;
    }

    public void setAbo(boolean abo) {
	this.abo = abo;
    }

    public Role getRole() {
	return role;
    }

    public void setRole(Role role) {
	this.role = role;
    }

    // public Product clone() {
    // Product clone = new Product();
    // clone.setAbo(isAbo());
    // clone.setDuration(getDuration());
    // clone.setMonthlyRate(getMonthlyRate());
    // clone.setRole(getRole());
    // return clone;
    // }
}
