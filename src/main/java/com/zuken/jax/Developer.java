package com.zuken.jax;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Developer {

    @XmlElement(name="firstname")
    private String name;
    private String last;

    public Developer() {
    }

    public Developer(String name, String last) {
        this.name = name;
        this.last = last;
    }
}
