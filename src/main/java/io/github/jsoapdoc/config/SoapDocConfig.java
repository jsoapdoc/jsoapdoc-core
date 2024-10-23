package io.github.jsoapdoc.config;

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

    /**
     * Get the package name of the controllers to be documented.
     *
     * @return the package name
     */
    public String getControllerPackage() {
        return controllerPackage;
    }

    /**
     * Set the package name of the controllers to be documented.
     *
     * @param controllerPackage
     *            the package name
     */
    public void setControllerPackage(String controllerPackage) {
        this.controllerPackage = controllerPackage;
    }
}
