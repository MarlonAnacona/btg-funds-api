package com.btgpactual.fondos.services;

import com.btgpactual.fondos.models.document.User;
import com.btgpactual.fondos.models.dto.RegisterRequest;
import com.btgpactual.fondos.models.dto.Response;
import com.btgpactual.fondos.models.enums.Role;
import com.btgpactual.fondos.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements  IUserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Response create(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setRole(Role.CLIENT);
        user.setBalance(BigDecimal.valueOf(500000));
        user.setNotificationPreference(request.getNotificationPreference());
        user.setInvestments(new ArrayList<>());

        userRepository.save(user);

        return new Response("Usuario creado exitosamente");
    }

    @Override
    public List<User> get() {
    return  userRepository.findAll();
    }
}
