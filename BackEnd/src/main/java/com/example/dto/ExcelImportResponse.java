package com.example.dto;

public class ExcelImportResponse {

    private boolean success;

    private String message;

    private int totalRecords;

    private int importedRecords;

    private int failedRecords;

    public ExcelImportResponse() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public int getImportedRecords() {
        return importedRecords;
    }

    public void setImportedRecords(int importedRecords) {
        this.importedRecords = importedRecords;
    }

    public int getFailedRecords() {
        return failedRecords;
    }

    public void setFailedRecords(int failedRecords) {
        this.failedRecords = failedRecords;
    }
}