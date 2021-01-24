package com.zuken.jax;

public class ZukenInstance {

    private String name;
    private String status;

    public ZukenInstance() {
    }

    public ZukenInstance(String name, String status) {
        this.name = name;
        this.status = status;
    }

    // Getter
    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    // Setter
    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
