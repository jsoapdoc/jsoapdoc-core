package com.jsoapdoc.soaplib.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@ConfigurationProperties(prefix = "soapdoc")
public class SoapDocConfig {

    private String controllerPackage;

    @PostConstruct
    public void init() {
        System.out.println("Caricamento configurazioni SOAP:");
        System.out.println("Package Controller: " + controllerPackage);
    }

    public String getControllerPackage() {
        return controllerPackage;
    }

    public void setControllerPackage(String controllerPackage) {
        this.controllerPackage = controllerPackage;
    }
}
