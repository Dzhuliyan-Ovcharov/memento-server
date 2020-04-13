package com.memento.service.impl;

import com.memento.model.EmailVerificationToken;
import com.memento.model.User;
import com.memento.repository.UserRepository;
import com.memento.service.EmailService;
import com.memento.service.EmailVerificationService;
import com.memento.service.RoleService;
import com.memento.service.UserService;
import com.memento.shared.exception.ResourceNotFoundException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Primary
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final EmailService emailService;
    private final EmailVerificationService verificationService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(final UserRepository userRepository,
                           final RoleService roleService,
                           final EmailService emailService,
                           final EmailVerificationService verificationService,
                           final BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.emailService = emailService;
        this.verificationService = verificationService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public Set<User> getAll() {
        return Set.copyOf(userRepository.findAll());
    }

    @Override
    @Transactional
    public void register(@NonNull final User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new DuplicateKeyException("Този имейл вече съществува.");
        }

        final User newUser = User.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .agencyName(user.getAgencyName())
                .agencyPhoneNumber(user.getAgencyPhoneNumber())
                .role(roleService.findRoleByPermission(user.getRole().getPermission()))
                .password(bCryptPasswordEncoder.encode(user.getPassword()))
                .build();

        final User savedUser = userRepository.save(newUser);
        final EmailVerificationToken emailVerificationToken = EmailVerificationToken.from(savedUser);
        emailService.sendMail(newUser.getEmail(), emailVerificationToken.getToken());
        verificationService.save(emailVerificationToken);
    }

    @Override
    public User update(@NonNull final User user) {
        final User oldUser = userRepository.findById(user.getId()).orElseThrow(() -> new ResourceNotFoundException("Липсва акаунт с идентификатор: " + user.getId()));
        final User newUser = User.builder()
                .id(oldUser.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .agencyName(user.getAgencyName())
                .agencyPhoneNumber(user.getAgencyPhoneNumber())
                .password(bCryptPasswordEncoder.encode(user.getPassword()))
                .role(user.getRole())
                .build();
        return userRepository.save(newUser);
    }

    @Override
    public User findByEmail(@NonNull final String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Липсва акаунт с имейл: " + email));
    }

    @Override
    public User findById(@NonNull final Long id) {
        log.info("Search for user with id: " + id);
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Липсва акаунт с идентификатор: " + id));
    }
}
