package io.github.jsoapdoc.model;

import java.util.List;

public class OperationInfo {

    private String operationName;
    private String description;
    private String version;
    private String[] rolesAllowed;
    private List<ParameterInfo> inputParameters;
    private List<ParameterInfo> outputParameters;
    private Class<?> inputClass;
    private Class<?> outputClass;

    public OperationInfo(String operationName, List<ParameterInfo> inputParameters, List<ParameterInfo> outputParameters, String description, String version, String[] rolesAllowed, Class<?> inputClass, Class<?> outputClass) {
        this.operationName = operationName;
        this.inputParameters = inputParameters;
        this.outputParameters = outputParameters;
        this.description = description;
        this.version = version;
        this.rolesAllowed = rolesAllowed;
        this.inputClass = inputClass;
        this.outputClass = outputClass;
    }

    public OperationInfo() {
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public List<ParameterInfo> getInputParameters() {
        return inputParameters;
    }

    public void setInputParameters(List<ParameterInfo> inputParameters) {
        this.inputParameters = inputParameters;
    }

    public List<ParameterInfo> getOutputParameters() {
        return outputParameters;
    }

    public void setOutputParameters(List<ParameterInfo> outputParameters) {
        this.outputParameters = outputParameters;
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

    public Class<?> getInputClass() {
        return inputClass;
    }

    public void setInputClass(Class<?> inputClass) {
        this.inputClass = inputClass;
    }

    public Class<?> getOutputClass() {
        return outputClass;
    }

    public void setOutputClass(Class<?> outputClass) {
        this.outputClass = outputClass;
    }

}
