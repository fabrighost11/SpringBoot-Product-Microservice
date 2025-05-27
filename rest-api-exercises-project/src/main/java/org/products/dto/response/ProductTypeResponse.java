package org.products.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response to types of product requests made.")
public class ProductTypeResponse {

    @Schema(description = "Type ID", example = "1")
    private Long id;

    @Schema(description = "Type name", example = "HOME_APPLIANCE")
    private String name;

}
