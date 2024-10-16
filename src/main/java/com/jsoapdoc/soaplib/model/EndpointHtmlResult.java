package com.jsoapdoc.soaplib.model;

public class EndpointHtmlResult {
    private String controllerId;
    private String displayName;
    private String content;

    public EndpointHtmlResult(String controllerId, String displayName, String content) {
        this.controllerId = controllerId;
        this.displayName = displayName;
        this.content = content;
    }

    public String getControllerId() {
        return controllerId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getContent() {
        return content;
    }
}
