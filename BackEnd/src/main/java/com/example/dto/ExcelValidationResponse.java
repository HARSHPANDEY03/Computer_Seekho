package com.example.dto;

import java.util.ArrayList;
import java.util.List;

public class ExcelValidationResponse {

    private boolean success;

    private int totalRecords;

    private int validRecords;

    private int invalidRecords;

    private List<String> errors = new ArrayList<>();

    public ExcelValidationResponse() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public int getValidRecords() {
        return validRecords;
    }

    public void setValidRecords(int validRecords) {
        this.validRecords = validRecords;
    }

    public int getInvalidRecords() {
        return invalidRecords;
    }

    public void setInvalidRecords(int invalidRecords) {
        this.invalidRecords = invalidRecords;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}