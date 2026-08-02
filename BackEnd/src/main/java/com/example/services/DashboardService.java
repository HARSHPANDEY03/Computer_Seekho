package com.example.services;

import com.example.dto.DashboardSummaryResponse;

public interface DashboardService {

    /**
     * Returns all summary information required
     * for the Admin Dashboard.
     */
    DashboardSummaryResponse getDashboardSummary();

}