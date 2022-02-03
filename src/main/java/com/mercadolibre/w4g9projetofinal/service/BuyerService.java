package com.mercadolibre.w4g9projetofinal.service;

import com.mercadolibre.w4g9projetofinal.entity.Buyer;
import com.mercadolibre.w4g9projetofinal.exceptions.ExistingUserException;
import com.mercadolibre.w4g9projetofinal.exceptions.ObjectNotFoundException;
import com.mercadolibre.w4g9projetofinal.repository.BuyerRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BuyerService {

    /*** Instancia de BCryptPasswordEncoder: <b>BCryptPasswordEncoder</b>.
     */
    private BCryptPasswordEncoder pe;

    private BuyerRepository repository;

    public List<Buyer> findAll() {
        return repository.findAll();
    }

    public Buyer findById(Long id) {
        Optional<Buyer> buyer = repository.findById(id);
        return buyer.orElseThrow(() ->
                new ObjectNotFoundException("Comprador não encontrado! Por favor verifique dados informados."));
    }

    public Buyer insert(Buyer buyer) {
        buyer.setPassword(pe.encode(buyer.getPassword()));
        try {
            return repository.save(buyer);
        } catch (DataIntegrityViolationException e) {
            throw new ExistingUserException("Username ou Email existente na base de dados");
        }
    }

    public Buyer update(Buyer buyer) {
        Buyer newBuyer = findById(buyer.getId());
        updateRepresentation(newBuyer, buyer);
        return repository.save(buyer);
    }

    public void delete(Long id) {
        Buyer buyer = findById(id);
        repository.delete(buyer);
    }

    private static void updateRepresentation(Buyer buyer, Buyer newBuyer) {
        newBuyer.setAddress(buyer.getAddress());
        newBuyer.setEmail(buyer.getEmail());
        newBuyer.setName(buyer.getName());
    }

}
