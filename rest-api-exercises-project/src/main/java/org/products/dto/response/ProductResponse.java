package org.products.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response to products requests made")
public class ProductResponse {

    @Schema(description = "ID of the product", example = "2")
    private Long id;

    @Schema(description = "Name of the product", example = "television")
    private String name;

    @Schema(description = "Price of the product", example = "480.75")
    private Double price;

    @Schema(description = "Stock of the product", example = "38")
    private Integer stock;

    @Schema(description = "Id of the Type of product", example = "1")
    private Long productTypeId;

    @Schema(description = "Type of product", example = "TECHNOLOGICAL")
    private String productTypeName;

}
