package com.jinelei.scalefish.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ApiTokenRequest(
    @NotBlank @Size(max = 100) String name,
    String expiresIn
) {}
