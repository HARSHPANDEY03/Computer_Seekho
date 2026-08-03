// ReceiptController.java
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

    @GetMapping("/{receiptId}")
    public ResponseEntity<Receipt> getReceiptById(@PathVariable("receiptId") Integer receiptId) {
        Receipt receipt = receiptService.getReceiptById(receiptId);
        return ResponseEntity.ok(receipt);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Receipt>> getReceiptsByStudentName(
            @RequestParam("studentName") String studentName) {
        List<Receipt> receipts = receiptService.getReceiptsByStudentName(studentName);
        return ResponseEntity.ok(receipts);
    }
}