package com.clone.instagram.authservice.service;

import com.clone.instagram.authservice.exception.ContactAlreadyExistsException;
import com.clone.instagram.authservice.model.Contact;
import com.clone.instagram.authservice.repository.ContactRepository;
import com.clone.instagram.authservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class ContactService {
    @Autowired private ContactRepository contactRepository;
    @Autowired private UserRepository userRepository;

    public Contact addUserToContacts(String username, Contact contact) {
        log.info("adding contact {} for user {}", contact.getContactId(), username);

        if(contactRepository.existsByFriendNameAndContactOwnerName(contact.getFriendName(), contact.getContactOwnerName())){
            log.warn("Contact {} for {} already exists", contact.getFriendName(), username);

            throw new ContactAlreadyExistsException(
                    String.format("Contact %s already exists", contact.getFriendName()));
        }

        return contactRepository.save(contact);
    }

    public List<Contact> findAll() {
        log.info("retrieving all contacts");
        return contactRepository.findAll();
    }

    public Optional<Contact> findByFriendName(String friendName) {
        log.info("retrieving contact {}", friendName);
        return contactRepository.findByFriendName(friendName);
    }

    public Optional<Contact> findByContactOwnerName(String contactOwnerName) {
        log.info("retrieving contacts belonging to {}", contactOwnerName);
        return contactRepository.findByContactOwnerName(contactOwnerName);
    }

    public Optional<Contact> findByFriendNameAndContactOwnerName(String friendName, String contactOwnerName) {
        log.info("retrieving contact {} belonging to {}", friendName, contactOwnerName);
        return contactRepository.findByFriendNameAndContactOwnerName(friendName,contactOwnerName);
    }

    public Optional<Contact> findAllByContactOwnerName(String contactOwnerName) {
        log.info("retrieving all contacts belonging to {}", contactOwnerName);
        return contactRepository.findAllByContactOwnerName(contactOwnerName);
    }

    public Optional<Contact> findByFriendId(String id) {
        log.info("retrieving contact {}", id);
        return contactRepository.findById(id);
    }
}
