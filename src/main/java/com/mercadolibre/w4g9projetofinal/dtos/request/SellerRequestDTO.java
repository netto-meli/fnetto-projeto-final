package com.mercadolibre.w4g9projetofinal.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerRequestDTO {
    @NotEmpty(message = "Campo Obrigatório")
    private String username;
    private String name;
    private String email;
    @NotEmpty(message = "Campo obrigatório")
    private String pass;
}
