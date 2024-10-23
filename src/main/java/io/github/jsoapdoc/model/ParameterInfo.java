package io.github.jsoapdoc.model;

public class ParameterInfo {

    private String name;
    private String type;
    private boolean required;
//    private String defaultValue;
//    private String description;
//    private String namespace;

    public ParameterInfo(String name, String type, boolean required) {
        this.name = name;
        this.type = type;
        this.required = required;
//        this.defaultValue = defaultValue;
//        this.description = description;
//        this.namespace = namespace;
    }

    public ParameterInfo() {
    }

    /**
     * Gets the name of this parameter.
     * @return The name of this parameter.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this parameter.
     *
     * @param name The name to set for this parameter.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the type of this parameter, as a fully qualified Java class name.
     * @return The type of this parameter.
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of this parameter.
     *
     * @param type The type to set for this parameter, as a fully qualified Java class name.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Determines if this parameter is required.
     *
     * @return true if this parameter is required; false otherwise.
     */
    public boolean isRequired() {
        return required;
    }

    /**
     * Sets whether this parameter is required.
     *
     * @param required true if this parameter is required; false otherwise.
     */
    public void setRequired(boolean required) {
        this.required = required;
    }

//    public String getDefaultValue() {
//        return defaultValue;
//    }
//
//    public void setDefaultValue(String defaultValue) {
//        this.defaultValue = defaultValue;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public String getNamespace() {
//        return namespace;
//    }
//
//    public void setNamespace(String namespace) {
//        this.namespace = namespace;
//    }

}
