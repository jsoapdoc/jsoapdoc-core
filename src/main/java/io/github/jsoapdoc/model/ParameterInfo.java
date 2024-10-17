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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRequired() {
        return required;
    }

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
