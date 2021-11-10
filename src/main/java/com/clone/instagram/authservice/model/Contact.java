package com.clone.instagram.authservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Contact {
    @Id
    private String contactId;

    @NotBlank
    @Size(max = 15)
    private String contactOwnerName; //username Того кто добавил контакт

    @NotBlank
    @Size(max = 15)
    private String friendName;

    public Contact(String friendName) {
        this.friendName = friendName;
    }

    public Contact(Contact contact){
        this.contactId = contact.contactId;
        this.contactOwnerName = contact.contactOwnerName;
        this.friendName = contact.friendName;
    }
}
