package com.mercadolibre.w4g9projetofinal.test.unit;

import com.mercadolibre.w4g9projetofinal.email.service.AuthService;
import com.mercadolibre.w4g9projetofinal.email.service.EmailService;
import com.mercadolibre.w4g9projetofinal.entity.Seller;
import com.mercadolibre.w4g9projetofinal.entity.User;
import com.mercadolibre.w4g9projetofinal.exceptions.ObjectNotFoundException;
import com.mercadolibre.w4g9projetofinal.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

    @Test
    public void verificaSendNewPassword() {
        //Arrange
        User s1 = new Seller(1L, "felipe3", "Fe Bontempo", "email2@hotmail.com", "123456", null);
        User s2 = new Seller(1L, "felipe3", "Fe Bontempo", "email2@hotmail.com", "123456", null);

        UserRepository userRepositoryMock = Mockito.mock(UserRepository.class);
        Mockito.when(userRepositoryMock.findByEmail("naoexiste.com")).thenReturn(Optional.empty());

        AuthService authService = new AuthService(userRepositoryMock, new BCryptPasswordEncoder());
        
        //Action
        ObjectNotFoundException expectedException = assertThrows(ObjectNotFoundException.class, () -> authService.sendNewPassword("naoexiste.com"));

        //Assertation
        assertTrue(expectedException.getMessage().contains("Email não encontrado"));
    }
}
