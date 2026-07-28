package com.example.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entities.Receipt;
import com.example.exceptions.ResourceNotFoundException;
import com.example.repositories.ReceiptRepository;

@Service
public class ReceiptServiceImpl implements ReceiptService {

    private final ReceiptRepository receiptRepository;

    public ReceiptServiceImpl(ReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Receipt getReceiptById(Integer receiptId) {
        return receiptRepository.findReceiptWithDetailsById(receiptId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Receipt not found for receipt_id: " + receiptId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Receipt> getReceiptsByStudentName(String studentName) {
        return receiptRepository.findReceiptsWithDetailsByStudentName(studentName);
    }
}
