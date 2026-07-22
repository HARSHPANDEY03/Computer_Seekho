package com.example.entities;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "enquiry")
public class Enquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enquiry_id")
    private Integer enquiryId;

    @Column(name = "enquirer_name")
    private String enquirerName;

    @Column(name = "enquirer_address")
    private String enquirerAddress;

    @Column(name = "enquirer_mobile")
    private Long enquirerMobile;

    @Column(name = "enquirer_alternate_mobile")
    private Long enquirerAlternateMobile;

    @Column(name = "enquirer_email_id", unique = true)
    private String enquirerEmailId;

    @Column(name = "enquiry_date")
    private LocalDate enquiryDate;

    @Column(name = "enquirer_query")
    private String enquirerQuery;

    // Only used when Closure Reason = Other
    @Column(name = "closure_reason")
    private String closureReasonText;

    @Column(name = "enquiry_processed_flag")
    private Boolean enquiryProcessedFlag;

    @Column(name = "inquiry_counter")
    private Integer inquiryCounter;

    @Column(name = "followup_date")
    private LocalDate followupDate;

    @Column(name = "enquiry_source")
    private String enquirySource;

    // Foreign Key to Course
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    // Foreign Key to Staff
    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;

    // Foreign Key to ClosureReason
    @ManyToOne
    @JoinColumn(name = "closure_reason_id")
    private ClosureReason closureReason;
    
    
//Default Constructor
    public Enquiry() {
    }

    
    //Getter  and Setter
    public Integer getEnquiryId() {
        return enquiryId;
    }

    public void setEnquiryId(Integer enquiryId) {
        this.enquiryId = enquiryId;
    }

    public String getEnquirerName() {
        return enquirerName;
    }

    public void setEnquirerName(String enquirerName) {
        this.enquirerName = enquirerName;
    }

    public String getEnquirerAddress() {
        return enquirerAddress;
    }

    public void setEnquirerAddress(String enquirerAddress) {
        this.enquirerAddress = enquirerAddress;
    }

    public Long getEnquirerMobile() {
        return enquirerMobile;
    }

    public void setEnquirerMobile(Long enquirerMobile) {
        this.enquirerMobile = enquirerMobile;
    }

    public Long getEnquirerAlternateMobile() {
        return enquirerAlternateMobile;
    }

    public void setEnquirerAlternateMobile(Long enquirerAlternateMobile) {
        this.enquirerAlternateMobile = enquirerAlternateMobile;
    }

    public String getEnquirerEmailId() {
        return enquirerEmailId;
    }

    public void setEnquirerEmailId(String enquirerEmailId) {
        this.enquirerEmailId = enquirerEmailId;
    }

    public LocalDate getEnquiryDate() {
        return enquiryDate;
    }

    public void setEnquiryDate(LocalDate enquiryDate) {
        this.enquiryDate = enquiryDate;
    }

    public String getEnquirerQuery() {
        return enquirerQuery;
    }

    public void setEnquirerQuery(String enquirerQuery) {
        this.enquirerQuery = enquirerQuery;
    }

    public String getClosureReasonText() {
        return closureReasonText;
    }

    public void setClosureReasonText(String closureReasonText) {
        this.closureReasonText = closureReasonText;
    }

    public Boolean getEnquiryProcessedFlag() {
        return enquiryProcessedFlag;
    }

    public void setEnquiryProcessedFlag(Boolean enquiryProcessedFlag) {
        this.enquiryProcessedFlag = enquiryProcessedFlag;
    }

    public Integer getInquiryCounter() {
        return inquiryCounter;
    }

    public void setInquiryCounter(Integer inquiryCounter) {
        this.inquiryCounter = inquiryCounter;
    }

    public LocalDate getFollowupDate() {
        return followupDate;
    }

    public void setFollowupDate(LocalDate followupDate) {
        this.followupDate = followupDate;
    }

    public String getEnquirySource() {
        return enquirySource;
    }

    public void setEnquirySource(String enquirySource) {
        this.enquirySource = enquirySource;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public ClosureReason getClosureReason() {
        return closureReason;
    }

    public void setClosureReason(ClosureReason closureReason) {
        this.closureReason = closureReason;
    }
    
   
//Parametrized Constructor
	public Enquiry(Integer enquiryId, String enquirerName, String enquirerAddress, Long enquirerMobile,
			Long enquirerAlternateMobile, String enquirerEmailId, LocalDate enquiryDate, String enquirerQuery,
			String closureReasonText, Boolean enquiryProcessedFlag, Integer inquiryCounter, LocalDate followupDate,
			String enquirySource, Course course, Staff staff, ClosureReason closureReason) {
		super();
		this.enquiryId = enquiryId;
		this.enquirerName = enquirerName;
		this.enquirerAddress = enquirerAddress;
		this.enquirerMobile = enquirerMobile;
		this.enquirerAlternateMobile = enquirerAlternateMobile;
		this.enquirerEmailId = enquirerEmailId;
		this.enquiryDate = enquiryDate;
		this.enquirerQuery = enquirerQuery;
		this.closureReasonText = closureReasonText;
		this.enquiryProcessedFlag = enquiryProcessedFlag;
		this.inquiryCounter = inquiryCounter;
		this.followupDate = followupDate;
		this.enquirySource = enquirySource;
		this.course = course;
		this.staff = staff;
		this.closureReason = closureReason;
	}

	
	 //ToString
	@Override
	public String toString() {
		return "Enquiry [enquiryId=" + enquiryId + ", enquirerName=" + enquirerName + ", enquirerAddress="
				+ enquirerAddress + ", enquirerMobile=" + enquirerMobile + ", enquirerAlternateMobile="
				+ enquirerAlternateMobile + ", enquirerEmailId=" + enquirerEmailId + ", enquiryDate=" + enquiryDate
				+ ", enquirerQuery=" + enquirerQuery + ", closureReasonText=" + closureReasonText
				+ ", enquiryProcessedFlag=" + enquiryProcessedFlag + ", inquiryCounter=" + inquiryCounter
				+ ", followupDate=" + followupDate + ", enquirySource=" + enquirySource + ", course=" + course
				+ ", staff=" + staff + ", closureReason=" + closureReason + "]";
	}
    
   
}