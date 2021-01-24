package com.zuken.jax;

public class ZukenInstance {

    private String name;
    private String status;
    private String zone;

    public ZukenInstance() {
    }

    public ZukenInstance(String name, String status, String zone) {
        this.name = name;
        this.status = status;
        this.zone = zone;
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
}
