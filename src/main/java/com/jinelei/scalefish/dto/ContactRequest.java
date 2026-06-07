package com.jinelei.scalefish.dto;

import jakarta.validation.constraints.NotBlank;

public record ContactRequest(
    @NotBlank String name,
    String email,
    String phone,
    String organization,
    String notes
) {}
