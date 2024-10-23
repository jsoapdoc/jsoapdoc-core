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

    /**
     * Returns the HTML element ID for the controller, which can be used
     * as the value of the {@code id} attribute in an HTML element,
     * allowing the controller to be linked to.
     *
     * @return the HTML element ID for the controller
     */
    public String getControllerId() {
        return controllerId;
    }

    /**
     * Returns the display name for the controller, which is the name
     * that will be displayed in the table of contents and in the
     * navigation menu.
     *
     * @return the display name for the controller
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns the HTML content for the controller, which is the
     * content that will be displayed for the controller.
     *
     * @return the HTML content for the controller
     */
    public String getContent() {
        return content;
    }
}
