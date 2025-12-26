package com.neworg.neworg.portfolio;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.neworg.neworg.transaction.TransactionRepository;
import com.neworg.neworg.transaction.TransactionType;
import com.neworg.neworg.transaction.Transactions;

@Service
public class PortfolioService {
    private final TransactionRepository transactionRepository;
    public PortfolioService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<PortfolioItemDTO> getPortfolio() {
        List<Transactions> transactions = transactionRepository.findAll();


        // Group transactions by ticker with a map for easier processing
        Map<String, List<Transactions>> transactionByTicker = new HashMap();
        for (Transactions tx : transactions) {
            String ticker = tx.getAsset().getTicker();
            transactionByTicker.putIfAbsent(ticker, new ArrayList<>());
            transactionByTicker.get(ticker).add(tx);    
        }


        List<PortfolioItemDTO> portfolio = new ArrayList<>();

        // Calculate portfolio item's positions and details
        for(Map.Entry<String, List<Transactions>> entry : transactionByTicker.entrySet()) {
            String ticker = entry.getKey();
            List<Transactions> allTransactions = entry.getValue();

            BigDecimal quantityBalance = BigDecimal.ZERO;
            BigDecimal averagePrice = BigDecimal.ZERO;
            String assetName = allTransactions.get(0).getAsset().getName();
        


            for(Transactions t : allTransactions) {
                if(t.getType() == TransactionType.BUY) {
                    // -----------Average Cost logic-------
                    // Total value acumulated from previous purchases
                    BigDecimal totalValueExisting = quantityBalance.multiply(averagePrice);

                    // Total value from the new purchase
                    BigDecimal totalValueNew = t.getQuantity().multiply(t.getPrice());
    
                    // Old value + new value
                    BigDecimal newQuantity = quantityBalance.add(t.getQuantity());

                    // New Average price AP = (Old value + new value) / new quantity)
                    // RoundingMode.HALF_UP to avoid rounding bias
                    if(newQuantity.compareTo(BigDecimal.ZERO) > 0) {
                        averagePrice = (totalValueExisting.add(totalValueNew))
                            .divide(newQuantity, 4, RoundingMode.HALF_UP);
                    }

                    quantityBalance = newQuantity;

                } else if(t.getType() == TransactionType.SELL) {
                    // Case of selling assets, we just reduce the quantity balance
                    quantityBalance = quantityBalance.subtract(t.getQuantity());
                }
            }

            // Only creates the DTO if there's a positive quantity balance
            if (quantityBalance.compareTo(BigDecimal.ZERO) > 0) {

                // Total cost is quantity balance multiplied by average price
                BigDecimal totalCost = quantityBalance.multiply(averagePrice);

                portfolio.add(new PortfolioItemDTO(
                    ticker, 
                    assetName, 
                    quantityBalance, 
                    averagePrice,
                    totalCost
                ));
            }
        }

        return portfolio;
    }
}