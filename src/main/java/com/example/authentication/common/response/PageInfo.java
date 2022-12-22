package com.example.authentication.common.response;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * A value object representing a paging
 */
@Getter
@Setter
@NoArgsConstructor
public class PageInfo<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private long total;
    private int limit;
    private int pages;
    private int page;
    private List<T> result;
}

