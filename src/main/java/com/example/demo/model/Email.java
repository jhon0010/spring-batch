package com.example.demo.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Email {

    @XmlElement(name = "name")
    private String name;

    @XmlElement(name = "domain")
    private String domain;
}