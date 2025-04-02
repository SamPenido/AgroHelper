package com.agrohelper.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public class ProductRequestDTO {

    @NotBlank(message = "Título não pode estar em branco")
    @Size(max = 150, message = "Título não pode exceder 150 caracteres")
    private String title;

    @NotBlank(message = "Descrição não pode estar em branco")
    private String description;

    @NotNull(message = "Preço não pode ser nulo")
    @DecimalMin(value = "0.01", message = "Preço deve ser positivo")
    private BigDecimal price;

    @NotBlank(message = "Categoria não pode estar em branco")
    @Size(max = 50, message = "Categoria não pode exceder 50 caracteres")
    private String category;

    // Seller ID will be determined from the authenticated user in the service layer

    // --- Getters and Setters ---
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
