package com.neworg.neworg.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.neworg.neworg.asset.Asset;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "transactions")
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "asset", nullable = false)
    private Asset asset;
    @Column(name = "quantity", nullable = false, precision = 19, scale = 4)
    private BigDecimal quantity;
    @Column(name = "price", nullable = false, precision = 19, scale = 4)
    private BigDecimal price;
    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    public Transactions(){}
    public Transactions(Asset asset, BigDecimal quantity, BigDecimal price, LocalDateTime transactionDate, TransactionType type) {
        this.asset = asset;
        this.quantity = quantity;
        this.price = price;
        this.transactionDate = transactionDate;
        this.type = type;
    }
    public Asset getAsset() {
        return asset;
    }
    public void setAsset(Asset asset) {
        this.asset = asset;
    }
    public BigDecimal getQuantity() {
        return quantity;
    }
    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }
    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
    public TransactionType getType() {
        return type;
    }
    public void setType(TransactionType type) {
        this.type = type;
    }
}
