package com.pairoo.domain.search;

/**
 * A Visitor DTO.
 *
 * @author ralf
 */
public class VisitSearchResult extends UserSearchResult {

    private Long visitorId;

    public Long getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(Long visitorId) {
        this.visitorId = visitorId;
    }
}
