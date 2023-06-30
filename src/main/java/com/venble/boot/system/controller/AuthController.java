package com.venble.boot.system.controller;

import com.venble.boot.common.vo.R;
import com.venble.boot.security.JwtTokenProvider;
import com.venble.boot.system.controller.vm.LoginVM;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    @PermitAll
    @PostMapping("/login")
    public R<String> login(@Valid @RequestBody LoginVM loginVM) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (BadCredentialsException e) {
            return R.error("用户名或密码错误");
        }
        return R.ok(jwtTokenProvider.createToken(authentication, loginVM.isRememberMe()));
    }
}
