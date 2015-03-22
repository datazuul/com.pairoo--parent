package com.pairoo.domain.search;

import java.io.Serializable;

/**
 * @author ralf
 */
public class SearchResult implements Serializable {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
