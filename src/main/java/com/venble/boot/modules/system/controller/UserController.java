package com.venble.boot.modules.system.controller;

import com.venble.boot.common.vo.R;
import com.venble.boot.modules.system.domain.User;
import com.venble.boot.modules.system.service.UserService;
import com.venble.boot.modules.system.controller.vm.LoginVM;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/admin")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PermitAll
    @PostMapping("/createUser")
    public R<?> createUser(@Valid @RequestBody LoginVM loginVM) {
        User user = new User();
        user.setUsername(loginVM.getUsername());
        user.setPassword(loginVM.getPassword());
        userService.createUser(user);
        return R.ok(LocalDateTime.now());
    }
}
