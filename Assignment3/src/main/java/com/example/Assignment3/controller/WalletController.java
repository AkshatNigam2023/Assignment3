package com.example.Assignment3.controller;

import com.example.Assignment3.model.User;
import com.example.Assignment3.model.Wallet;
import com.example.Assignment3.repository.UserRepository;
import com.example.Assignment3.repository.WalletRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/add-money/{userId}")
    public ResponseEntity<String> addMoneyToWallet(
            @PathVariable("userId") int userId,
            @RequestParam("amount") Double amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Wallet wallet = user.getWallet();
        if (wallet == null) {
            wallet = new Wallet(user);
            user.setWallet(wallet);
        }

        wallet.setBalance(wallet.getBalance() + amount);
        walletRepository.save(wallet);

        return ResponseEntity.ok("Money added to wallet successfully.");
    }

    @GetMapping("/check-balance/{userId}")
    public ResponseEntity<Double> checkWalletBalance(@PathVariable("userId") int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Wallet wallet = user.getWallet();
        if (wallet == null) {
            return ResponseEntity.ok(0.0);
        }

        return ResponseEntity.ok(wallet.getBalance());
    }

    @PostMapping("/transfer-to-offline/{userId}")
    public ResponseEntity<String> transferMoney(
            @PathVariable("userId") int userId,
            @RequestParam("amount") Double amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Wallet wallet = user.getWallet();
        if (wallet == null) {
            return ResponseEntity.badRequest().body("Wallet not found for the user.");
        }

        // Check if there is enough balance to transfer
        if (wallet.getBalance() < amount) {
            return ResponseEntity.badRequest().body("Insufficient funds in the wallet.");
        }

        if (wallet.getCodes().isEmpty()) {
            generateAndAddCodes(wallet);
        }

        // Transfer money from balance to offline balance
        wallet.setBalance(wallet.getBalance() - amount);
        wallet.setOfflineBalance(wallet.getOfflineBalance() + amount);
        walletRepository.save(wallet);

        return ResponseEntity.ok("Money transferred successfully.");
    }

    private void generateAndAddCodes(Wallet wallet) {
        int numberOfCodes = 5;
        for (int i = 0; i < numberOfCodes; i++) {
            String randomCode = generateRandomCode();
            wallet.getCodes().add(randomCode);
        }
    }

    private String generateRandomCode() {
        int codeLength = 8;
        return RandomStringUtils.randomAlphanumeric(codeLength);
    }

    @GetMapping("/get-codes/{userId}")
    public ResponseEntity<Set<String>> getWalletCodes(@PathVariable("userId") int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Wallet wallet = user.getWallet();
        if (wallet == null || wallet.getCodes().isEmpty()) {
            return ResponseEntity.ok(new HashSet<>()); // Return an empty set if no codes are available
        }

        return ResponseEntity.ok(wallet.getCodes());
    }
}
