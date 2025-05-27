package org.products.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO request to create and update a type of product")
public class ProductTypeRequest {

    @Schema(description = "Name of 'ProductType'")
    @NotBlank(message = "Name cant be empty.")
    private String name;


}
