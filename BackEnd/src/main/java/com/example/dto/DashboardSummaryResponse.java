package com.example.dto;

public class DashboardSummaryResponse {

    private Long totalEnquiries;
    private Long todayFollowups;
    private Long totalAdmissions;
    private Double totalFeesReceived;
    private Double pendingFees;

    

    public DashboardSummaryResponse(
            Long totalEnquiries,
            Long todayFollowups,
            Long totalAdmissions) {

        this.totalEnquiries = totalEnquiries;
        this.todayFollowups = todayFollowups;
        this.totalAdmissions = totalAdmissions;
        this.totalFeesReceived = 0.0;
        this.pendingFees = 0.0;
    }

    // Getters and Setters

    public Long getTotalEnquiries() {
        return totalEnquiries;
    }

    public void setTotalEnquiries(Long totalEnquiries) {
        this.totalEnquiries = totalEnquiries;
    }

    public Long getTodayFollowups() {
        return todayFollowups;
    }

    public void setTodayFollowups(Long todayFollowups) {
        this.todayFollowups = todayFollowups;
    }

    public Long getTotalAdmissions() {
        return totalAdmissions;
    }

    public void setTotalAdmissions(Long totalAdmissions) {
        this.totalAdmissions = totalAdmissions;
    }

    public Double getTotalFeesReceived() {
        return totalFeesReceived;
    }

    public void setTotalFeesReceived(Double totalFeesReceived) {
        this.totalFeesReceived = totalFeesReceived;
    }

    public Double getPendingFees() {
        return pendingFees;
    }

    public void setPendingFees(Double pendingFees) {
        this.pendingFees = pendingFees;
    }

    @Override
    public String toString() {
        return "DashboardSummaryResponse{" +
                "totalEnquiries=" + totalEnquiries +
                ", todayFollowups=" + todayFollowups +
                ", totalAdmissions=" + totalAdmissions +
                ", totalFeesReceived=" + totalFeesReceived +
                ", pendingFees=" + pendingFees +
                '}';
    }
}