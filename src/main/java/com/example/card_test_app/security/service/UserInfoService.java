package com.example.card_test_app.security.service;

import com.example.card_test_app.card.model.dto.RegistrationUserDto;
import com.example.card_test_app.card.model.exceptions.UserAlreadyExistException;
import com.example.card_test_app.card.model.exceptions.UserNotFoundException;
import com.example.card_test_app.security.model.UserInfo;
import com.example.card_test_app.security.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserInfoService implements UserDetailsService {

    private final UserInfoRepository userInfoRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserInfoService(UserInfoRepository userInfoRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userInfoRepository = userInfoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserInfo> userDetail = userInfoRepository.findByEmail(email);

        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }

    public String addUser(RegistrationUserDto userInfo) {

        Optional<UserInfo> userFromDb = userInfoRepository.findByEmail(userInfo.getEmail());

        if (userFromDb.isPresent()) {
            throw new UserAlreadyExistException("User already exists with the same email");
        }

        UserInfo user = new UserInfo();
        user.setFirstName(userInfo.getFirstName());
        user.setLastName(userInfo.getLastName());
        user.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        user.setEmail(userInfo.getEmail());
        user.setRoles(userInfo.getRoles());

        userInfoRepository.save(user);

        return "User Added Successfully";
    }

    public UserInfo getUserById(Long userId) {
        return userInfoRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
    }

    public void deleteUser(Long userId) {
        userInfoRepository.deleteById(userId);
    }
}
