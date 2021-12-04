package com.hoaxify.hoaxify.hoax.vm;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class HoaxUpdateVM {

    @NotNull
    @Size(min = 10, max = 5000)
    @Column(length = 5000)
    private String content;

}
