package io.github.jsoapdoc.model;

public class EndpointHtmlResult {
    private final String controllerId;
    private final String displayName;
    private final String content;

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
