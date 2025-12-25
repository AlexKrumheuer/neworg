package com.neworg.neworg.asset;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AssetDTO(
    @NotBlank(message = "Ticker cannot be blank")
    String ticker,
    @NotBlank(message = "Name cannot be blank")
    String name,
    @NotNull(message = "Type cannot be blank")
    AssetType type
) {
    
}
