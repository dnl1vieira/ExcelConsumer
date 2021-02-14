package com.vieira.daniel.ExcelConsumer.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "TAB_EXCEL_CONSUMER")
@Data
public class ExcelConsumer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "POLICY")
    private String policy;

    @Column(name = "EXPIRY")
    private String expiry;

    @Column(name = "location")
    private String location;

    @Column(name = "STATE")
    private String state;

    @Column(name = "region")
    private String region;

    @Column(name = "INSURED_VALUE")
    private String insuredValue;

    @Column(name = "CONSTRUCTION")
    private String construction;

    @Column(name = "BUSINESS_TYPE")
    private String businessType;

    @Column(name = "EARTHQUAKE")
    private String earthquake;

    @Column(name = "FLOOD")
    private String flood;

}
