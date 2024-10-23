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

    /**
     * Gets the name of the operation.
     *
     * @return the name of the operation.
     */
    public String getOperationName() {
        return operationName;
    }

    /**
     * Sets the name of the operation.
     *
     * @param operationName the name of the operation to set.
     */
    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    /**
     * Gets the list of input parameters.
     *
     * @return the list of input parameters.
     */
    public List<ParameterInfo> getInputParameters() {
        return inputParameters;
    }

    /**
     * Sets the list of input parameters for the operation.
     *
     * @param inputParameters the list of input parameters to set.
     */
    public void setInputParameters(List<ParameterInfo> inputParameters) {
        this.inputParameters = inputParameters;
    }

    /**
     * Gets the list of output parameters.
     *
     * @return the list of output parameters.
     */
    public List<ParameterInfo> getOutputParameters() {
        return outputParameters;
    }

    /**
     * Sets the list of output parameters for the operation.
     *
     * @param outputParameters the list of output parameters to set.
     */
    public void setOutputParameters(List<ParameterInfo> outputParameters) {
        this.outputParameters = outputParameters;
    }

    /**
     * Gets the description of the operation.
     *
     * @return the description of the operation.
     */
    public String getDescription() {
        return description;
    }


    /**
     * Sets the description of the operation.
     *
     * @param description the description of the operation.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the version of the operation.
     *
     * @return the version of the operation.
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the version of the operation.
     *
     * @param version the version to set for the operation.
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Gets the list of roles that are allowed to access this operation.
     *
     * @return the array of roles that are allowed to access this operation.
     */
    public String[] getRolesAllowed() {
        return rolesAllowed;
    }

    /**
     * Sets the array of roles that are allowed to access this operation.
     *
     * @param rolesAllowed the array of roles to be assigned.
     */
    public void setRolesAllowed(String[] rolesAllowed) {
        this.rolesAllowed = rolesAllowed;
    }

    /**
     * Gets the class of the object that is passed as input to this operation.
     *
     * @return the class of the input object.
     */
    public Class<?> getInputClass() {
        return inputClass;
    }

    /**
     * Sets the class type of the object that is expected as input for this operation.
     *
     * @param inputClass the class type to set as the input for the operation.
     */
    public void setInputClass(Class<?> inputClass) {
        this.inputClass = inputClass;
    }

    /**
     * Gets the class of the object that is returned as output by this operation.
     *
     * @return the class of the output object.
     */
    public Class<?> getOutputClass() {
        return outputClass;
    }

    /**
     * Sets the class type of the object that is returned as output by this operation.
     *
     * @param outputClass the class type to set as the output for the operation.
     */
    public void setOutputClass(Class<?> outputClass) {
        this.outputClass = outputClass;
    }

}
