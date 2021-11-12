package com.clone.instagram.authservice.payload;

import com.clone.instagram.authservice.model.Contact;
import com.clone.instagram.authservice.model.User;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class UserSummary {

    private String id;
    private String username;
    private String name;
    private String profilePicture;
    private List<User> contacts;
}
