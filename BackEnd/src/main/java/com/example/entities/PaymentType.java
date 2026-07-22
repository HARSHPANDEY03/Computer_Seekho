package com.example.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "paymentType")
public class PaymentType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_typeID")
    private Integer paymentTypeId;

    @Column(name = "payment_type_desc", length = 255)
    private String paymentTypeDesc;

    // Default Constructor
    public PaymentType() {
    	
    }

    // Parameterized Constructor
    public PaymentType(Integer paymentTypeId, String paymentTypeDesc) {
        this.paymentTypeId = paymentTypeId;
        this.paymentTypeDesc = paymentTypeDesc;
    }

    // Getter and Setter
    public Integer getPaymentTypeId() {
        return paymentTypeId;
    }

    public void setPaymentTypeId(Integer paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
    }

    public String getPaymentTypeDesc() {
        return paymentTypeDesc;
    }

    public void setPaymentTypeDesc(String paymentTypeDesc) {
        this.paymentTypeDesc = paymentTypeDesc;
    }

    @Override
    public String toString() {
        return "PaymentType{" +
                "paymentTypeId=" + paymentTypeId +
                ", paymentTypeDesc='" + paymentTypeDesc + '\'' +
                '}';
    }
}