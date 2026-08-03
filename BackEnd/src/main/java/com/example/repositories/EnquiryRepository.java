package com.example.repositories;

import com.example.entities.Enquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EnquiryRepository extends JpaRepository<Enquiry, Integer> {
	List<Enquiry> findByEnquirerEmailId(String enquirerEmailId);

	// Custom query to find unassigned inquiries older than or equal to the cutoff
	// date
	@Query("SELECT e FROM Enquiry e WHERE e.staff IS NULL AND e.enquiryDate <= :cutoffDate")
	List<Enquiry> findUnassignedOlderThan(@Param("cutoffDate") LocalDate cutoffDate);
	@Query("SELECT e FROM Enquiry e WHERE e.staff.staffId = :staffId AND e.followupDate = :today AND e.enquiryProcessedFlag = false")
	List<Enquiry> findTodayDueForStaff(@Param("staffId") Integer staffId, @Param("today") LocalDate today);

	@Query("SELECT e FROM Enquiry e WHERE e.staff.staffId = :staffId AND e.followupDate < :today AND e.enquiryProcessedFlag = false")
	List<Enquiry> findOverdueForStaff(@Param("staffId") Integer staffId, @Param("today") LocalDate today);

	// "Open" = not yet processed - i.e. neither registered (admitted) nor
	// closed (marked not-joining). Matches the frontend's deriveEnquiryStatus:
	// enquiryProcessedFlag only gets set true once an enquiry is admitted or
	// explicitly closed, so everything else (new/contacted/follow-up-due)
	// counts as still open.
	@Query("SELECT COUNT(e) FROM Enquiry e WHERE e.enquiryProcessedFlag IS NULL OR e.enquiryProcessedFlag = false")
	Long countOpen();
}