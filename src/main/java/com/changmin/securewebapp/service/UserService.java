package com.changmin.securewebapp.service;

import com.changmin.securewebapp.dto.UserInfoResponseDto;
import com.changmin.securewebapp.dto.UserRequestDto;
import com.changmin.securewebapp.entity.User;
import com.changmin.securewebapp.repository.UserRepository;
import com.changmin.securewebapp.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional(readOnly = true)
    public String login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return jwtUtil.generateToken(username, user.getRole());
    }

    @Transactional
    public void register(UserRequestDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        }

        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        User user = User.builder()
                .username(dto.getUsername())
                .password(encodedPassword)
                .role("ROLE_USER")
                .build();

        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없음."));
        userRepository.delete(user);
    }

    @Transactional
    public List<UserInfoResponseDto> getAllUsers(){
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> new UserInfoResponseDto(user.getUsername(), user.getRole()))
                .collect(Collectors.toList());
    }
}