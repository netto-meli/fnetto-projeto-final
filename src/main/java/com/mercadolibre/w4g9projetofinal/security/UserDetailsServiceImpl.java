package com.mercadolibre.w4g9projetofinal.security;

import com.mercadolibre.w4g9projetofinal.entity.Seller;
import com.mercadolibre.w4g9projetofinal.entity.User;
import com.mercadolibre.w4g9projetofinal.repository.SellerRepository;
import com.mercadolibre.w4g9projetofinal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email);
        if(user == null) {
            throw new UsernameNotFoundException(email);
        }
        return new UserSS(user.getId(), user.getEmail(), user.getPassword(), user.getProfile());

    }
}
