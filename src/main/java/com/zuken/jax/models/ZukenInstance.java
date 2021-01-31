package com.zuken.jax.models;

public class ZukenInstance {

    private String name;
    private String status;
    private String zone;
    private String template;

    public ZukenInstance() {
    }

    public ZukenInstance(String name, String status, String zone, String template) {
        this.name = name;
        this.status = status;
        this.zone = zone;
        this.template = template;
    }

    // Getter
    public String getName() {
        return this.name;
    }

    public String getStatus() {
        return this.status;
    }

    public String getZone() {
        return this.zone;
    }

    public String getTemplate(){
        return this.template;
    }

    // Setter
    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public void setTemplate(String template){
        this.template = template;
    }
}
