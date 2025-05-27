package org.products.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseResponse {

    private Long id;
    private Long userId;
    private Long productId;
    private Long productTypeId;
    private Integer quantity;

}
