package com.tdgroup.Logistica.Service;

import org.springframework.beans.factory.annotation.Autowired;

import com.tdgroup.Logistica.Model.User;
import com.tdgroup.Logistica.Repository.UserRepository;

public  class UserService {

	
    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private UserRepository userRepository;
/*
    public String authenticateUser(String username, String password) {
        User user = userRepository.findByUsernameAndPasswordAndAttivoTrue(username, password);

        if (user != null) {
            String tipoUtente = user.getTipoUtente();
            return jwtTokenService.generateToken(username, tipoUtente);
        } else {
            return null; // Autenticazione fallita
        }
    }
*/
}
