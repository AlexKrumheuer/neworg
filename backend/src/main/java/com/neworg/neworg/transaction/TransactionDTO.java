package com.neworg.neworg.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public record TransactionDTO(
    @NotNull(message = "Asset cannot be null")
    Long assetId,
    @NotNull(message = "Quantity cannot be null")
    BigDecimal quantity,
    @NotNull(message = "Price cannot be null")
    BigDecimal price,
    @NotNull(message = "Transaction date cannot be null")
    @PastOrPresent(message = "Transaction date cannot be in the future")
    LocalDateTime transactionDate,
    @NotNull(message = "Transaction type cannot be null")
    TransactionType type
) {
    
}
