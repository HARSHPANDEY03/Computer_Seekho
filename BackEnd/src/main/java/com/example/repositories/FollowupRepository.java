package com.example.repositories;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.entities.Followup;

@Repository	
public interface FollowupRepository extends JpaRepository<Followup ,Integer> {
	List<Followup> findByEnquiryEnquiryIdOrderByFollowupDateDesc(int enquiryId);
}
