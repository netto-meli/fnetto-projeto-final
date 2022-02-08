package com.mercadolibre.w4g9projetofinal.service;

import com.mercadolibre.w4g9projetofinal.entity.User;
import com.mercadolibre.w4g9projetofinal.entity.enums.Profile;
import com.mercadolibre.w4g9projetofinal.repository.UserRepository;
import com.mercadolibre.w4g9projetofinal.security.entity.UserSS;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    @Autowired
    private UserRepository repository;

    public List<User> findAll() {
        return repository.findAll();
    }

    public static UserSS authenticated() {
        try{
            UserSS user = (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(user == null) throw new AccessDeniedException("Acesso negado");
            return user;
        }
        catch (Exception e) {
            throw new AccessDeniedException("Acesso negado");
        }
    }

    public static UserSS adminOrSameUser(Long id) {
        UserSS user = UserService.authenticated();
        if (!(user.hasRole(Profile.ADMIN) || id.equals(user.getId()))) {
            throw new AccessDeniedException("Acesso Negado");
        }
        return user;
    }
}
