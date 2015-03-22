package com.pairoo.domain.enums;


/**
 * @author Ralf Eichinger
 */
public enum Role {
    ADMIN("ADMIN"), STANDARD("FREE_USER"), PREMIUM("PREMIUM_USER");
    
    private String code;
    
    private Role(String code) {
	this.code = code;
    }
    
    public String getCode() {
	return this.code;
    }
}
