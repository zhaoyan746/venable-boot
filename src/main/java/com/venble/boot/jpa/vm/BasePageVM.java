package com.venble.boot.jpa.vm;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;


@Data
public abstract class BasePageVM {

    @NotNull
    @Range(min = 0)
    private Integer pageIndex = 1;

    @NotNull
    @Range(min = 0, max = 200)
    private Integer pageSize = 10;

    private String searchText;

}