package com.example.Assignment3.controller;

import com.example.Assignment3.model.*;
import com.example.Assignment3.repository.*;
import com.example.Assignment3.util.PaymentMode;
import com.example.Assignment3.util.TransactionStatus;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/transactionsApi")
public class PaymentProcessingController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private AdminRepository adminRepository;

    @PostMapping("/makeOnlinePayment")
    public ResponseEntity<String> makePayment(@RequestBody PaymentRequestOnline paymentRequest) {
        // Implement payment logic
        User user = userRepository.findById(paymentRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("Not found"));

        Vendor vendor = vendorRepository.findById(paymentRequest.getVendorId())
                .orElseThrow(() -> new RuntimeException("Not found"));

        // Check if the payment is within 20km radius
        if (!isWithinRadius(paymentRequest.getLatitude(), paymentRequest.getLongitude(), vendor.getLatitude(), vendor.getLongitude(), 20)) {
            Transaction transaction = new Transaction();
            transaction.setUserId(paymentRequest.getUserId());
            transaction.setVendorId(paymentRequest.getVendorId());
            transaction.setAmount(paymentRequest.getAmount());
            transaction.setStatus(TransactionStatus.FLAGGED);
            transaction.setPaymentMode(PaymentMode.ONLINE);
            transaction.setTransactionDate(new Date());


            // Update user's wallet balance
            Wallet userWallet = user.getWallet();
            userWallet.setBalance(userWallet.getBalance() - paymentRequest.getAmount());
            walletRepository.save(userWallet);


            // Save the transaction
            transactionRepository.save(transaction);

            return ResponseEntity.ok("Payment flagged.");  //Payment from > 20 km
        } else {
            Transaction transaction = new Transaction();
            transaction.setUserId(paymentRequest.getUserId());
            transaction.setVendorId(paymentRequest.getVendorId());
            transaction.setAmount(paymentRequest.getAmount());
            transaction.setStatus(TransactionStatus.SUCCESSFUL);
            transaction.setPaymentMode(PaymentMode.ONLINE);
            transaction.setTransactionDate(new Date());


            // Update user's wallet balance
            Wallet userWallet = user.getWallet();
            userWallet.setBalance(userWallet.getBalance() - paymentRequest.getAmount());
            walletRepository.save(userWallet);

            // Update vendor's wallet balance
            Wallet vendorWallet = vendor.getStoreWallet();
            vendorWallet.setBalance(vendorWallet.getBalance() + paymentRequest.getAmount());
            walletRepository.save(vendorWallet);

            // Save the transaction
            transactionRepository.save(transaction);

            return ResponseEntity.ok("Payment successful.");
        }
    }



    @PostMapping("/makeOfflinePayment")
    public ResponseEntity<String> makePaymentOffline(@RequestBody PaymentRequestOffline paymentRequestOffline) {

        // Implement offline payment logic
        User user = userRepository.findById(paymentRequestOffline.getUserId())
                .orElseThrow(() -> new RuntimeException("Not found"));

        // Check if the provided code matches any of the codes in the user's set
        if (!user.getWallet().getCodes().contains(paymentRequestOffline.getCode())) {
            return ResponseEntity.badRequest().body("Transaction Failed."); //Invalid Code
        }

        Vendor vendor = vendorRepository.findById(paymentRequestOffline.getVendorId())
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        if (!isWithinRadius(paymentRequestOffline.getLatitude(), paymentRequestOffline.getLongitude(), vendor.getLatitude(), vendor.getLongitude(), 20)) {

            Transaction transaction = new Transaction();
            transaction.setUserId(paymentRequestOffline.getUserId());
            transaction.setVendorId(paymentRequestOffline.getVendorId());
            transaction.setAmount(paymentRequestOffline.getAmount());
            transaction.setStatus(TransactionStatus.FLAGGED);
            transaction.setPaymentMode(PaymentMode.OFFLINE);
            transaction.setTransactionDate(new Date());

            // Update user's wallet balance
            Wallet userWallet = user.getWallet();
            userWallet.setOfflineBalance(userWallet.getOfflineBalance() - paymentRequestOffline.getAmount());
            walletRepository.save(userWallet);


            // Save the transaction
            transactionRepository.save(transaction);

            return ResponseEntity.ok("payment flagged."); //Payment From > 20km

        } else {

            Transaction transaction = new Transaction();
            transaction.setUserId(paymentRequestOffline.getUserId());
            transaction.setVendorId(paymentRequestOffline.getVendorId());
            transaction.setAmount(paymentRequestOffline.getAmount());
            transaction.setStatus(TransactionStatus.SUCCESSFUL);
            transaction.setPaymentMode(PaymentMode.OFFLINE);
            transaction.setTransactionDate(new Date());

            // Update user's wallet balance
            Wallet userWallet = user.getWallet();
            userWallet.setOfflineBalance(userWallet.getOfflineBalance() - paymentRequestOffline.getAmount());
            walletRepository.save(userWallet);

            // Update vendor's wallet balance
            Wallet vendorWallet = vendor.getStoreWallet();
            vendorWallet.setBalance(vendorWallet.getBalance() + paymentRequestOffline.getAmount());
            walletRepository.save(vendorWallet);

            // Save the transaction
            transactionRepository.save(transaction);

            return ResponseEntity.ok("Offline Payment successful.");
        }
    }

    @GetMapping("/flaggedTransactions")
    public ResponseEntity<List<Transaction>> getFlaggedTransactions() {
        // Retrieve flagged transactions for admin review
        List<Transaction> flaggedTransactions = transactionRepository.findByStatus(TransactionStatus.FLAGGED).get();
        return ResponseEntity.ok(flaggedTransactions);
    }

    @PostMapping("/reviewTransaction/{adminId}/{transactionId}/{approval}")
    public ResponseEntity<String> reviewTransaction(
            @PathVariable Long adminId,
            @PathVariable Long transactionId,
            @PathVariable Boolean approval) {
        // Implement logic for admin review
        Admin admin = adminRepository.findById(Math.toIntExact(adminId))
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (transaction.getStatus() == TransactionStatus.FLAGGED) {
            if (approval) {
                // Approve transaction
                transferAmountToVendor(transaction);
            } else {
                // Reject transaction
                returnAmountToUser(transaction);
            }
        }

        return ResponseEntity.ok("Transaction reviewed successfully.");
    }
    private boolean isWithinRadius(double lat1, double lon1, double lat2, double lon2, double radius) {
        double earthRadius = 6371; // in kilometers

        double distance = haversineDistance(lat1, lon1, lat2, lon2, earthRadius);

        return distance <= radius;
    }

    private double haversineDistance(double lat1, double lon1, double lat2, double lon2, double earthRadius) {
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadius * c;
    }

    // Helper method to transfer amount to the vendor's wallet
    private void transferAmountToVendor(Transaction transaction) {
        Vendor vendor = vendorRepository.findById(transaction.getVendorId())
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        // Transfer amount to vendor's wallet
        Wallet vendorWallet = vendor.getStoreWallet();
        vendorWallet.setBalance(vendorWallet.getBalance() + transaction.getAmount());

        // Save the updated vendor's wallet
        walletRepository.save(vendorWallet);

        // Update the transaction status to SUCCESSFUL
        transaction.setStatus(TransactionStatus.SUCCESSFUL);
        transactionRepository.save(transaction);
    }

    // Helper method to return amount to the user's wallet
    private void returnAmountToUser(Transaction transaction) {
        User user = userRepository.findById(transaction.getUserId())
                .orElseThrow(() -> new RuntimeException("Not found"));

        // Return amount to user's wallet
        Wallet userWallet = user.getWallet();
        userWallet.setBalance(userWallet.getBalance() + transaction.getAmount());

        // Save the updated user's wallet
        walletRepository.save(userWallet);

        // Update the transaction status to FAILED
        transaction.setStatus(TransactionStatus.FAILED);
        transactionRepository.save(transaction);
    }

    @Data
    static class PaymentRequestOnline {
        private Integer userId;
        private Integer vendorId;
        private Double amount;
        private Double latitude;

        private Double longitude;

        public Double getLatitude() {
            return latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public Integer getUserId() {
            return userId;
        }

        public Integer getVendorId() {
            return vendorId;
        }

        public Double getAmount() {
            return amount;
        }

    }

    @Data
    static class PaymentRequestOffline {
        private Integer userId;
        private Integer vendorId;
        private Double amount;
        private Double latitude;

        private Double longitude;

        private String code;

        public String getCode() {
            return code;
        }

        public Double getLatitude() {
            return latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public Integer getUserId() {
            return userId;
        }

        public Integer getVendorId() {
            return vendorId;
        }

        public Double getAmount() {
            return amount;
        }
    }
}

