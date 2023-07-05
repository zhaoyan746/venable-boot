package com.venble.boot.system.service;


import com.venble.boot.common.util.RandomUtils;
import com.venble.boot.system.domain.Role;
import com.venble.boot.system.domain.User;
import com.venble.boot.system.repository.RoleRepository;
import com.venble.boot.system.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(User user) {
        // TODO: username can not duplicate
        String encryptedPassword = passwordEncoder.encode(RandomUtils.generatePassword());
        user.setPassword(encryptedPassword);
        user.setEnabled(true);
        if (user.getRoles() != null) {
            Set<Role> roles = user
                    .getRoles()
                    .stream()
                    .map(role -> roleRepository.findById(role.getId()))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        }
        return userRepository.save(user);
    }

    public Page<User> findAll(String username, Pageable pageable) {
        Specification<User> specification = (root, criteriaQuery, criteriaBuilder) -> {
            if (username != null) {
                return criteriaBuilder.like(root.get("username"), "%" + username + "%");
            }
            return null;
        };
        return userRepository.findAll(specification, pageable);
    }

}
