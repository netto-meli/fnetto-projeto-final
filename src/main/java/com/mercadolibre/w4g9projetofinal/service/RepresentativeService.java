package com.mercadolibre.w4g9projetofinal.service;

import com.mercadolibre.w4g9projetofinal.entity.Representative;
import com.mercadolibre.w4g9projetofinal.entity.User;
import com.mercadolibre.w4g9projetofinal.exceptions.ExistingUserException;
import com.mercadolibre.w4g9projetofinal.exceptions.ObjectNotFoundException;
import com.mercadolibre.w4g9projetofinal.repository.RepresentativeRepository;
import com.mercadolibre.w4g9projetofinal.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/***
 * Classe de serviço para Representative
 *
 * @author Marcos Sá
 */

@Service
@AllArgsConstructor
public class RepresentativeService {

    /*** Instancia de BCryptPasswordEncoder: <b>BCryptPasswordEncoder</b>.
     */
    @Autowired
    private BCryptPasswordEncoder pe;

    /*** Instancia de repositório: <b>UserRepository</b>.
     */
    @Autowired
    private UserRepository userRepository;

    /*** Instancia de repositório: <b>RepresentativeRepository</b>.
     */
    @Autowired
    private RepresentativeRepository repository;


    /*** Método que retorna lista de Representatives */
    public List<Representative> findAll() {
        return repository.findAll();
    }


    /*** Método que busca Representative por Id
     * @param id ID do Representative a ser retornado
     */
    public Representative findById(Long id) {
        Optional<Representative> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Representante não encontrado! Por favor verifique o id."));
    }

    /*** Método que busca Representative por Email
     * @param email email do Representative a ser retornado
     */
    public Representative findByEmail(String email) {
        Representative obj = repository.findByEmail(email);
        if (obj == null) {
            throw new ObjectNotFoundException(
                    "Usuário não encontrado");
        }
        return obj;
    }

    /*** Método que insere um Representative
     * @param obj objeto Representative a ser inserido
     */
    public Representative insert(Representative obj) {
        String email = findByEmailDB(obj.getEmail());
        if(email == null) {
            obj.setPassword(pe.encode(obj.getPassword()));
            return repository.save(obj);
        }
        else {
            throw new ExistingUserException("Usuário existente na base de dados");
        }
    }

    /*** Método que atualiza um Representative já existente
     *
     * @param newObj Objeto com informações para atualização de um Representative existente
     */
    public Representative update(Representative newObj) {
        Representative obj = findById(newObj.getId());
        updateRepresentation(newObj, obj);
        return repository.save(obj);
    }

    /*** Método deleta um Representative do Bando de dados
     *
     * @param id ID do Representative a ser deletado
     */
    public void delete(Long id) {
        Representative obj = findById(id);
        repository.delete(obj);
    }

    //Método para update de Representation
    private static void updateRepresentation(Representative obj, Representative newObj) {
        newObj.setName(obj.getName());
        newObj.setJob(obj.getJob());
        newObj.setEmail(obj.getEmail());
    }

    //Método para verificar se existe o email informado no banco de dados
    private String findByEmailDB(String email) {
        User user = userRepository.findByEmail(email);
        if(user == null) {
            return null;
        }
        return user.getEmail();
    }


}
