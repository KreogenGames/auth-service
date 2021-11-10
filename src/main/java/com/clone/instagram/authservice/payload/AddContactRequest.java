package com.clone.instagram.authservice.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AddContactRequest {

    @NotBlank
    @Size(min = 3, max = 15)
    private String friendName;
}
