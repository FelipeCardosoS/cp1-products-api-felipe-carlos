package com.felipe.cp1.cp1_products_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record ProductDTO(
        @NotBlank String name,
        @Positive Double price,
        @Positive Integer stock
) {}