package com.clone.instagram.authservice.service;

import com.clone.instagram.authservice.exception.ContactAlreadyExistsException;
import com.clone.instagram.authservice.exception.EmailAlreadyExistsException;
import com.clone.instagram.authservice.exception.UsernameAlreadyExistsException;
import com.clone.instagram.authservice.exception.UsernameIsNotExistsException;
import com.clone.instagram.authservice.model.Role;
import com.clone.instagram.authservice.repository.ContactRepository;
import com.clone.instagram.authservice.repository.UserRepository;
import com.clone.instagram.authservice.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class UserService {

    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private UserRepository userRepository;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtTokenProvider tokenProvider;

    public String loginUser(String username, String password) {
       Authentication authentication = authenticationManager
               .authenticate(new UsernamePasswordAuthenticationToken(username, password));

       return tokenProvider.generateToken(authentication);
    }

    public User registerUser(User user, Role role) {
        log.info("registering user {}", user.getUsername());

        if(userRepository.existsByUsername(user.getUsername())) {
            log.warn("username {} already exists.", user.getUsername());

            throw new UsernameAlreadyExistsException(
                    String.format("username %s already exists", user.getUsername()));
        }

        if(userRepository.existsByEmail(user.getEmail())) {
            log.warn("email {} already exists.", user.getEmail());

            throw new EmailAlreadyExistsException(
                    String.format("email %s already exists", user.getEmail()));
        }
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>() {{
            add(role);
        }});
        return userRepository.save(user);
    }


    public User addContact(String userName, String friendName){
        log.info("adding contact {} to user {}", friendName, userName);
        User user = userRepository.findByUsername(userName).get();
        if(Boolean.FALSE.equals(userRepository.existsByUsername(userName))){
            log.warn("username {} isn't exists.", userName);
            throw new UsernameIsNotExistsException(
                    String.format("username %s isn't exists", userName));
        } else if(user.getContacts().contains(userRepository.findByUsername(friendName).get())){
            log.warn("contact {} is already exists.", friendName);
            throw new ContactAlreadyExistsException(
                    String.format("contact %s is already exists", friendName));
        } else if(Boolean.TRUE.equals(userRepository.existsByUsername(friendName))) {
            user.getContacts().add(userRepository.findByUsername(friendName).get());
            log.info("user {} contacts {}", userName, user.getContacts().toString());

        } else {
            log.warn("user {} isn't exists.", friendName);
            throw new UsernameIsNotExistsException(
                    String.format("user %s isn't exists", friendName));
        }

        return userRepository.save(user);
    }

    public List<User> findAll() {
        log.info("retrieving all users");
        return userRepository.findAll();
    }

    public List<User> getAllContacts(String userName){
        return userRepository.getByUsername(userName).getContacts();
    }

    public Optional<User> findByUsername(String username) {
        log.info("retrieving user {}", username);
        return userRepository.findByUsername(username);
    }

    public Optional<User> findContactsByUsernameList(String username) {
        log.info("retrieving user {}", username);
        return userRepository.findByUsername(username);
    }

    public Optional<User> findById(String id) {
        log.info("retrieving user {}", id);
        return userRepository.findById(id);
    }
}
