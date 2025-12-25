package com.neworg.neworg.portfolio;

import java.math.BigDecimal;
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
        System.out.println("DEBUG: Total de transações no banco: " + transactions.size());
        Map<String, List<Transactions>> transactionByTicker = new HashMap();
        for (Transactions tx : transactions) {
            String ticker = tx.getAsset().getTicker();
            transactionByTicker.putIfAbsent(ticker, new ArrayList<>());
            transactionByTicker.get(ticker).add(tx);    
        }
        System.out.println("DEBUG: Total de Tickers únicos encontrados: " + transactionByTicker.size());
        List<PortfolioItemDTO> portfolio = new ArrayList<>();
        for(Map.Entry<String, List<Transactions>> entry : transactionByTicker.entrySet()) {
            String ticker = entry.getKey();
            List<Transactions> allTransactions = entry.getValue();

            BigDecimal quantityBalance = BigDecimal.ZERO;
            String assetName = allTransactions.get(0).getAsset().getName();

            for(Transactions t : allTransactions) {
                System.out.println("   -> Transação Tipo: " + t.getType() + " | Qtd: " + t.getQuantity());

                if(t.getType() == TransactionType.BUY) {
                    quantityBalance = quantityBalance.add(t.getQuantity());
                } else if(t.getType() == TransactionType.SELL) {
                    quantityBalance = quantityBalance.subtract(t.getQuantity());
                }
            }

            System.out.println("DEBUG: Saldo final de " + ticker + ": " + quantityBalance);
            if (quantityBalance.compareTo(BigDecimal.ZERO) > 0) {
                portfolio.add(new PortfolioItemDTO(
                    ticker, 
                    assetName, 
                    quantityBalance, 
                    BigDecimal.ZERO
                ));
            }
        }

        return portfolio;
    }
}