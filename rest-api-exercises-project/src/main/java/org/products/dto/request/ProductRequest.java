package org.products.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO request to create and update a product")
public class ProductRequest {

    @NotBlank(message = "Name of the product cant be empty.")
    @Schema(description = "Name of the product", example = "Washing machine")
    private String name;

    @NotNull(message = "Price of the product cant be null.")
    @Positive(message = "Price of the product cant zero or lower.")
    @Schema(description = "Price of the product", example = "250.99")
    private Double price;

    @NotNull(message = "Stock cant be null.")
    @PositiveOrZero(message = "Stock must be zero or higher.")
    @Schema(description = "Stock of the product", example = "22")
    private Integer stock;

    @NotNull(message = "Type of product cant be null.")
    @Schema(description = "Type of product", example = "HOME_APPLIANCE")
    private Long productTypeId;

}
