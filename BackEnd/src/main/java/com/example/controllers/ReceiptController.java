package com.example.controllers;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.entities.Receipt;
import com.example.services.ReceiptService;

@RestController
@RequestMapping("/api/receipts")
public class ReceiptController {

    private final ReceiptService receiptService;

    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    /**
     * Called when the user presses "View Receipt" on the frontend.
     * GET /api/receipts/{receiptId}
     */
    @GetMapping("/{receiptId}")
    public ResponseEntity<Receipt> getReceiptById(@PathVariable Integer receiptId) {
        Receipt receipt = receiptService.getReceiptById(receiptId);
        return ResponseEntity.ok(receipt);
    }

    /**
     * Fetches all receipts for a student whose name matches (partial,
     * case-insensitive) the given text.
     * GET /api/receipts/search?studentName=John
     */
    @GetMapping("/search")
    public ResponseEntity<List<Receipt>> getReceiptsByStudentName(
            @RequestParam String studentName) {
        List<Receipt> receipts = receiptService.getReceiptsByStudentName(studentName);
        return ResponseEntity.ok(receipts);
    }
}
