package com.venble.boot.system.service.criteria;

import com.venble.boot.jpa.service.Criteria;
import com.venble.boot.jpa.service.filter.LongFilter;
import com.venble.boot.jpa.service.filter.StringFilter;

import java.io.Serializable;

public class UserCriteria implements Serializable, Criteria {

    private String searchKeywords;

    private Boolean useOr = false;

    private LongFilter id;

    private StringFilter username;

    private StringFilter password;

    private StringFilter email;

    private StringFilter activated;

    private StringFilter avatarUrl;

    @Override
    public Criteria copy() {
        return null;
    }
}
