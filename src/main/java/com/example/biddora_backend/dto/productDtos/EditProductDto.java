package com.example.biddora_backend.dto.productDtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditProductDto {

    @NotBlank(message = "Product must have a name!")
    private String name;

}
