package com.pairoo.domain.search;

/**
 * A Favorite DTO.
 *
 * @author ralf
 */
public class FavoriteSearchResult extends UserSearchResult {

    private Long targetId;

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }
}
