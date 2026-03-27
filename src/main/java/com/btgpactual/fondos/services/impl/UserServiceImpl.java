package com.btgpactual.fondos.services.impl;

import com.btgpactual.fondos.models.document.User;
import com.btgpactual.fondos.models.dto.RegisterRequest;
import com.btgpactual.fondos.models.dto.Response;
import com.btgpactual.fondos.models.enums.Role;
import com.btgpactual.fondos.repositories.UserRepository;
import com.btgpactual.fondos.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * Creates a new user in the system.
     *
     * This method validates that the email is not already registered, hashes the password,
     * initializes the default balance and role, and persists the user.
     *
     * @param request RegisterRequest containing user details such as email, password, name,
     *                cellphone, and notification preference.
     * @return Response Message indicating that the user was successfully created.
     * @throws RuntimeException if the email is already in use.
     */
    @Override
    public Response create(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setRole(request.getRole());
        user.setBalance(BigDecimal.valueOf(500000));
        user.setNotificationPreference(request.getNotificationPreference());
        user.setInvestments(new ArrayList<>());
        user.setCelphone(request.getCelphone());

        userRepository.save(user);

        return new Response("Usuario creado exitosamente");
    }

    /**
     * Retrieves all users from the system.
     *
     * @return List<User> A list of all registered users.
     */
    @Override
    public List<User> get() {
    return  userRepository.findAll();
    }
}
