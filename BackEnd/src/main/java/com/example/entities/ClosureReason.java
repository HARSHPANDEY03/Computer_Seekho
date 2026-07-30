package com.example.entities;

import java.util.List;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "closure_reason")
public class ClosureReason {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "closure_reason_id")
	private Integer closureReasonId;

	@Column(name = "closure_reason_desc", nullable = false)
	private String closureReasonDesc;

	// @JsonIgnore: without this, serializing an Enquiry that has this
	// ClosureReason set recurses forever (enquiry -> closureReason ->
	// enquiries -> closureReason -> ...) and crashes the server mid-response.
	@JsonIgnore
	@OneToMany(mappedBy = "closureReason", cascade = CascadeType.ALL)
	private List<Enquiry> enquiries;

	// Default Constructor
	public ClosureReason() {
	}

	// Parameterized Constructor
	public ClosureReason(Integer closureReasonId, String closureReasonDesc, List<Enquiry> enquiries) {
		this.closureReasonId = closureReasonId;
		this.closureReasonDesc = closureReasonDesc;
		this.enquiries = enquiries;
	}

	// Getters and Setters
	public Integer getClosureReasonId() {
		return closureReasonId;
	}

	public void setClosureReasonId(Integer closureReasonId) {
		this.closureReasonId = closureReasonId;
	}

	public String getClosureReasonDesc() {
		return closureReasonDesc;
	}

	public void setClosureReasonDesc(String closureReasonDesc) {
		this.closureReasonDesc = closureReasonDesc;
	}

	public List<Enquiry> getEnquiries() {
		return enquiries;
	}

	public void setEnquiries(List<Enquiry> enquiries) {
		this.enquiries = enquiries;
	}

	@Override
	public String toString() {
		return "ClosureReason [closureReasonId=" + closureReasonId + ", closureReasonDesc=" + closureReasonDesc + "]";
	}
}