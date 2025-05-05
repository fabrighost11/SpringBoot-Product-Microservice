package org.products.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

@Schema(description = "Response to types of product requests made.")
public class ProductTypeResponse {

    @Schema(description = "Type ID", example = "1")
    private Long id;

    @Schema(description = "Type name", example = "HOME_APPLIANCE")
    private String name;

    public ProductTypeResponse() {
    }

    public ProductTypeResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProductTypeResponse that = (ProductTypeResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
