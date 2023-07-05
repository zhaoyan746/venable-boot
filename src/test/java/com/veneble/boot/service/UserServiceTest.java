package com.veneble.boot.service;

import com.venble.boot.modules.system.domain.Role;
import com.venble.boot.modules.system.domain.User;
import com.venble.boot.modules.system.service.UserService;
import com.veneble.boot.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import java.util.Set;

@IntegrationTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void assertThatUserCanBeCreated() {
        Role role = new Role();
        role.setId(3L);
        role.setName("test");
        User user = new User();
        user.setUsername("test");
        user.setRoles(Set.of(role));
        userService.createUser(user);
    }

    @Test
    void assertThatUserCanBePage() {
        // 从1开始，每页2条
        Pageable pageable = Pageable.ofSize(1).withPage(1);
        userService.findAll("test", pageable);
    }
}
