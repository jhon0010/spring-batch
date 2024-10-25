package com.example.demo.model;

import lombok.Data;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "record")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class MyRecord {

    @XmlAttribute(name = "removed")
    private boolean removed;

    @XmlAttribute(name = "uuid")
    private String uuid;

    @XmlElement(name = "id")
    private String id;

    @XmlElement(name = "name")
    private String name;

    @XmlElement(name = "email")
    private Email email;
}
