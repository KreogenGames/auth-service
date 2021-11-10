package com.clone.instagram.authservice.repository;

import com.clone.instagram.authservice.model.Contact;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ContactRepository extends MongoRepository<Contact, String> {
    Optional<Contact> findByFriendName(String friendName);
    Optional<Contact> findByContactOwnerName(String contactOwnerName);
    Boolean existsByFriendName(String friendName);
    Boolean existsByContactOwnerName(String contactOwnerName);
    // Получение контакта
    //Contact getContact(String contactId);
}
