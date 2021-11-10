package com.clone.instagram.authservice.endpoint;

import com.clone.instagram.authservice.exception.*;
import com.clone.instagram.authservice.model.*;
import com.clone.instagram.authservice.payload.*;
import com.clone.instagram.authservice.service.ContactService;
import com.clone.instagram.authservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


@RestController
@Slf4j
public class UserEndpoint {

    @Autowired
    private UserService userService;
    @Autowired
    private ContactService contactService;

    @GetMapping(value = "/users/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findUser(@PathVariable("username") String username) {
        log.info("retrieving user {}", username);

        return  ResponseEntity.ok(userService
                .findByUsername(username)
                .stream()
                .map(this::convertTo));
    }

    @PostMapping(value = "/users/{username}/contacts/{friendname}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addToContacts(@PathVariable("username") String username, @PathVariable("friendname") String friendname){
        log.info("adding contacts to user {} contact list", username);
        Contact contact = Contact
                .builder()
                .contactOwnerName(username)
                .friendName(friendname)
                .build();

        try {
            contactService.addUserToContacts(username, contact);
        } catch (ContactAlreadyExistsException e) {
            throw new BadRequestException(e.getMessage());
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}/contacts")
                .buildAndExpand(username).toUri();

        return ResponseEntity
                .created(location)
                .body(new ApiResponse(true,"Contact added successfully"));
    }

    @GetMapping(value = "/users/{username}/contacts/{friendname}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findContactByFriendName(@PathVariable("username") String username,
                                                     @PathVariable("friendname") String friendname) {
        log.info("retrieving {}'s contact {}", username, friendname);

        return  ResponseEntity.ok(contactService
                .findByFriendName(friendname)
                .stream()
                .map(this::convertTo));
    }

    @GetMapping(value = "/users/{username}/contacts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> showContacts(@PathVariable("username") String username) {
        log.info("retrieving {}'s contacts", username);

        return  ResponseEntity.ok(contactService
                .findByContactOwnerName(username)
                .stream()
                .map(this::convertTo));
    }

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAll() {
        log.info("retrieving all users");

        return ResponseEntity
                .ok(userService.findAll());
    }

    @GetMapping(value = "/users/summaries", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAllUserSummaries(
            @AuthenticationPrincipal InstaUserDetails userDetails) {
        log.info("retrieving all users summaries");

        return ResponseEntity.ok(userService
                .findAll()
                .stream()
                .filter(user -> !user.getUsername().equals(userDetails.getUsername()))
                .map(this::convertTo));
    }

    @GetMapping(value = "/users/me", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USER') or hasRole('FACEBOOK_USER')")
    @ResponseStatus(HttpStatus.OK)
    public UserSummary getCurrentUser(@AuthenticationPrincipal InstaUserDetails userDetails) {
        return UserSummary
                .builder()
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .name(userDetails.getUserProfile().getDisplayName())
                .profilePicture(userDetails.getUserProfile().getProfilePictureUrl())
                .build();
    }

    @GetMapping(value = "/users/summary/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserSummary(@PathVariable("username") String username) {
        log.info("retrieving user {}", username);

        return  userService
                .findByUsername(username)
                .map(user -> ResponseEntity.ok(convertTo(user)))
                .orElseThrow(() -> new ResourceNotFoundException(username));
    }

    private UserSummary convertTo(User user) {
        return UserSummary
                .builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getUserProfile().getDisplayName())
                .profilePicture(user.getUserProfile().getProfilePictureUrl())
                .build();
    }

    private ContactSummary convertTo(Contact contact) {
        return ContactSummary
                .builder()
                .contactId(contact.getContactId())
                .contactOwnerName(contact.getContactOwnerName())
                .friendName(contact.getFriendName())
                .build();
    }
}