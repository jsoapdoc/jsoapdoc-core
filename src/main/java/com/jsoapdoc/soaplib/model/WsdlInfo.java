package com.jsoapdoc.soaplib.model;

import java.util.List;

public class WsdlInfo {

    private String serviceName;
    private String description;
    private String version;
    private String[] rolesAllowed;
    private List<OperationInfo> operations;

    public WsdlInfo(String serviceName, List<OperationInfo> operations) {
        this.serviceName = serviceName;
        this.operations = operations;
    }

    public WsdlInfo() {
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public List<OperationInfo> getOperations() {
        return operations;
    }

    public void setOperations(List<OperationInfo> operations) {
        this.operations = operations;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String[] getRolesAllowed() {
        return rolesAllowed;
    }

    public void setRolesAllowed(String[] rolesAllowed) {
        this.rolesAllowed = rolesAllowed;
    }

}
