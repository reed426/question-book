package com.questionbook.service;

import com.questionbook.dto.LoginRequest;
import com.questionbook.dto.SignupRequest;
import com.questionbook.entity.User;
import com.questionbook.exception.DuplicateEmailException;
import com.questionbook.exception.InvalidCredentialsException;
import com.questionbook.repository.UserRepository;
import com.questionbook.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public String signup(SignupRequest req) {
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new DuplicateEmailException("이미 가입된 이메일입니다");
        }
        User user = new User();
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setNickname(req.getNickname());
        userRepository.save(user);
        return jwtTokenProvider.createToken(user.getEmail());
    }

    public String login(LoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("이메일 또는 비밀번호가 일치하지 않습니다"));
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("이메일 또는 비밀번호가 일치하지 않습니다");
        }
        return jwtTokenProvider.createToken(user.getEmail());
    }
}
