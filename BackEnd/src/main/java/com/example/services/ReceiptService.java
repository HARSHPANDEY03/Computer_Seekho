package com.example.services;

import java.util.List;

import com.example.entities.Receipt;

public interface ReceiptService {

    /**
     * Builds a full, print-ready receipt for the given receipt id by
     * pulling the receipt row plus its related payment, student, course
     * and batch data.
     *
     * @param receiptId the receipt primary key
     * @return the receipt entity with all related data loaded
     */
    Receipt getReceiptById(Integer receiptId);

    /**
     * Finds every receipt belonging to students whose name matches (partial,
     * case-insensitive) the given text, together with related payment,
     * student, course and batch data.
     *
     * @param studentName full or partial student name to search for
     * @return matching receipts; empty list if none found
     */
    List<Receipt> getReceiptsByStudentName(String studentName);
}
