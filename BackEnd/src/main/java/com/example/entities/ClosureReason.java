package com.example.entities;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "closure_reason")
public class ClosureReason {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "closure_reason_id")
    private Integer closureReasonId;

    @Column(name = "closure_reason_desc", nullable = false)
    private String closureReasonDesc;

    @OneToMany(mappedBy = "closureReason", cascade = CascadeType.ALL)
    private List<Enquiry> enquiries;

    //Default Constructor
    public ClosureReason() {
    }

    //Getter and Setter
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
    
    
//parametrozed Constructor
	public ClosureReason(Integer closureReasonId, String closureReasonDesc, List<Enquiry> enquiries) {
		super();
		this.closureReasonId = closureReasonId;
		this.closureReasonDesc = closureReasonDesc;
		this.enquiries = enquiries;
	}

	@Override
	public String toString() {
		return "ClosureReason [closureReasonId=" + closureReasonId + ", closureReasonDesc=" + closureReasonDesc
				+ ", enquiries=" + enquiries + "]";
	}
    
   
}
