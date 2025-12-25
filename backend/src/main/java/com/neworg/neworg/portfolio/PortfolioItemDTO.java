package com.neworg.neworg.portfolio;

import java.math.BigDecimal;

public record PortfolioItemDTO(
    String ticker,
    String name,
    BigDecimal quantity,
    BigDecimal averagePrice
) {
    
}
