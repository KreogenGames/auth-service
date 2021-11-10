package com.clone.instagram.authservice.payload;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactSummary {

    private String contactId;
    private String contactOwnerName;
    private String friendName;
}