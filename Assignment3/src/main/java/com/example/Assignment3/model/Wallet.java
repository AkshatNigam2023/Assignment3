package com.example.Assignment3.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "wallet_table")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long walletId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Double balance;

    private Double offlineBalance;


    @ElementCollection
    @CollectionTable(name = "wallet_codes", joinColumns = @JoinColumn(name = "wallet_id"))
    @Column(name = "code")
    @Builder.Default
    private Set<String> codes = new HashSet<>();

    public Wallet(Long walletId, User user, Double balance, Double offlineBalance, Set<String> codes) {
        this.walletId = walletId;
        this.user = user;
        this.balance = balance;
        this.offlineBalance = offlineBalance;
        this.codes = codes;
    }

    public Set<String> getCodes() {
        return codes;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "walletId=" + walletId +
                ", user=" + user +
                ", balance=" + balance +
                ", offlineBalance=" + offlineBalance +
                ", codes=" + codes +
                '}';
    }

    public void setCodes(Set<String> codes) {
        this.codes = codes;
    }

    public Wallet() {
        this.setBalance(Double.valueOf(0));
        this.setOfflineBalance(Double.valueOf(0));
    }

    public Wallet(int walletId) {
        this.setWalletId(Long.valueOf(walletId));
        this.setBalance(Double.valueOf(0));
        this.setOfflineBalance(Double.valueOf(0));
    }

    public Wallet(Long walletId, User user, Double balance, Double offlineBalance) {
        this.walletId = walletId;
        this.user = user;
        this.balance = balance;
        this.offlineBalance = offlineBalance;
    }
    public Wallet(User user) {
        this.user = user;
        this.balance = Double.valueOf(0);
        this.offlineBalance = Double.valueOf(0);
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getOfflineBalance() {
        return offlineBalance;
    }

    public void setOfflineBalance(Double offlineBalance) {
        this.offlineBalance = offlineBalance;
    }

}
