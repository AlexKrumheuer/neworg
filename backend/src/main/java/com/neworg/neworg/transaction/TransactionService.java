package com.neworg.neworg.transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.neworg.neworg.asset.Asset;
import com.neworg.neworg.asset.AssetRepository;

import jakarta.validation.Valid;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AssetRepository assetRepository;

    public TransactionService(TransactionRepository transactionRepository, AssetRepository assetRepository) {
        this.transactionRepository = transactionRepository;
        this.assetRepository = assetRepository;
    }

    public Optional<Transactions> findTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    public List<Transactions> findAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transactions> findTransactionsByAssetId(Long assetId) {
        return transactionRepository.findByAssetId(assetId);
    }

    public Transactions saveTransaction(@Valid TransactionDTO transactionDTO) {
        Asset asset = assetRepository.findById(transactionDTO.assetId())
                .orElseThrow(() -> new IllegalArgumentException("Asset with id " + transactionDTO.assetId() + " not found."));

        if (transactionDTO.type() == TransactionType.SELL) {
            BigDecimal currentBalance = transactionRepository.getBalanceByAssetId(asset.getId());

            if (currentBalance == null) {
                currentBalance = BigDecimal.ZERO;
            }
            if (transactionDTO.quantity().compareTo(currentBalance) > 0) {
                throw new IllegalArgumentException(
                    "Saldo insuficiente para venda. Você tem " + currentBalance + 
                    " mas tentou vender " + transactionDTO.quantity()
                );
            }
        }

        Transactions transaction = new Transactions(
                asset,
                transactionDTO.quantity(),
                transactionDTO.price(),
                transactionDTO.transactionDate(),
                transactionDTO.type()
        );
        return transactionRepository.save(transaction);
    }

    public Transactions editTransaction(Long id, @Valid TransactionDTO transactionDTO) {
        Transactions transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction with id " + id + " not found."));
        Asset asset = assetRepository.findById(transactionDTO.assetId())
                .orElseThrow(() -> new IllegalArgumentException("Asset with id " + transactionDTO.assetId() + " not found."));
        transaction.setAsset(asset);
        transaction.setQuantity(transactionDTO.quantity());
        transaction.setPrice(transactionDTO.price());
        transaction.setTransactionDate(transactionDTO.transactionDate());
        transaction.setType(transactionDTO.type());
        return transactionRepository.save(transaction);
    }

    public void deleteTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new IllegalArgumentException("Transaction with id " + id + " not found.");
        }
        transactionRepository.deleteById(id);
    }

}
