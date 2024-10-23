package io.github.jsoapdoc.service;

import io.github.jsoapdoc.annotation.AnnotationProcessor;
import io.github.jsoapdoc.model.EndpointHtmlResult;
import io.github.jsoapdoc.model.OperationInfo;
import io.github.jsoapdoc.model.ParameterInfo;
import io.github.jsoapdoc.model.WsdlInfo;
import io.github.jsoapdoc.config.SoapDocConfig;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;

@Component
public class HtmlGenerator {

    private final SoapDocConfig config;

    public HtmlGenerator(SoapDocConfig config) {
        this.config = config;
    }

    /**
     * Generates HTML for the given endpoint class.
     *
     * @param endpointClass The class of the endpoint to generate HTML for.
     * @return The generated HTML for the endpoint.
     */
    public EndpointHtmlResult generateHtmlForEndpoint(Class<?> endpointClass) {
        StringBuilder htmlBuilder = new StringBuilder();

        WsdlInfo wsdlInfo = AnnotationProcessor.collectWsdlInfo(endpointClass);
        if (wsdlInfo == null) {
            return null;
        }

        String serviceName = wsdlInfo.getServiceName();
        String controllerId = "controller-" + sanitizeId(serviceName);

        htmlBuilder.append("<div class=\"controller-content\" id=\"")
                .append(controllerId)
                .append("\" style=\"display:none;\">");

        htmlBuilder.append("<div class=\"header-container\">");
        htmlBuilder.append("<h2>Endpoint: ").append(serviceName).append("</h2>");
        htmlBuilder.append("</div>");

        htmlBuilder.append("<p><strong>Description:</strong> ").append(wsdlInfo.getDescription()).append("</p>");
        htmlBuilder.append("<p><strong>Version:</strong> ").append(wsdlInfo.getVersion()).append("</p>");
        htmlBuilder.append("<p><strong>Roles Allowed:</strong> ").append(String.join(", ", wsdlInfo.getRolesAllowed())).append("</p>");

        if (wsdlInfo.getOperations() != null && !wsdlInfo.getOperations().isEmpty()) {
            List<OperationInfo> operations = wsdlInfo.getOperations();

            htmlBuilder.append("<div id=\"").append(controllerId).append("-operations\" class=\"content-container active\">");

            for (OperationInfo operation : operations) {
                htmlBuilder.append("<hr>");
                htmlBuilder.append("<div class=\"operation\">");
                htmlBuilder.append("<h3>Method: ").append(operation.getOperationName()).append("</h3>");
                htmlBuilder.append("<p><strong>Description:</strong> ").append(operation.getDescription()).append("</p>");
                htmlBuilder.append("<p><strong>Version:</strong> ").append(operation.getVersion()).append("</p>");
                htmlBuilder.append("<p><strong>Roles Allowed:</strong> ").append(String.join(", ", operation.getRolesAllowed())).append("</p>");

                // INPUT PARAMETERS
                htmlBuilder.append("<h3>Request Parameters</h3>");
                if (operation.getInputParameters() != null && !operation.getInputParameters().isEmpty()) {
                    htmlBuilder.append("<table>");
                    htmlBuilder.append("<tr><th>Name</th><th>Type</th><th>Required</th></tr>");
                    for (ParameterInfo param : operation.getInputParameters()) {
                        htmlBuilder.append("<tr>");
                        htmlBuilder.append("<td>").append(param.getName()).append("</td>");
                        htmlBuilder.append("<td>").append(param.getType()).append("</td>");
                        htmlBuilder.append("<td>").append(param.isRequired() ? "Yes" : "No").append("</td>");
                        htmlBuilder.append("</tr>");
                    }
                    htmlBuilder.append("</table>");
                } else {
                    htmlBuilder.append("<p>No request parameters.</p>");
                }

                String operationId = operation.getOperationName().replaceAll("[^a-zA-Z0-9]", "-");

                // TEXTAREA FOR SAMPLE REQUEST
                htmlBuilder.append("</table>");
                htmlBuilder.append("<h4>Sample Request XML</h4>");
                htmlBuilder.append("<div class=\"textarea-container\">");
                htmlBuilder.append("<textarea readonly id=\"request-xml-").append(operationId).append("\">");
                htmlBuilder.append(generateSampleXml(operation.getInputClass()));
                htmlBuilder.append("</textarea>");
                htmlBuilder.append("<button class=\"copy-button\" onclick=\"copyToClipboard('request-xml-").append(operationId).append("')\">Copy</button>");
                htmlBuilder.append("</div>");

                // OUTPUT PARAMETERS
                htmlBuilder.append("<h3>Response Parameters</h3>");
                if (operation.getOutputParameters() != null && !operation.getOutputParameters().isEmpty()) {
                    htmlBuilder.append("<table>");
                    htmlBuilder.append("<tr><th>Name</th><th>Type</th><th>Required</th></tr>");
                    for (ParameterInfo param : operation.getOutputParameters()) {
                        htmlBuilder.append("<tr>");
                        htmlBuilder.append("<td>").append(param.getName()).append("</td>");
                        htmlBuilder.append("<td>").append(param.getType()).append("</td>");
                        htmlBuilder.append("<td>").append(param.isRequired() ? "Yes" : "No").append("</td>");
                        htmlBuilder.append("</tr>");
                    }
                    htmlBuilder.append("</table>");
                } else {
                    htmlBuilder.append("<p>No response parameters.</p>");
                }

                //TEXTAREA FOR SAMPLE RESPONSE
                htmlBuilder.append("</table>");
                htmlBuilder.append("<h4>Sample Response XML</h4>");
                htmlBuilder.append("<div class=\"textarea-container\">");
                htmlBuilder.append("<textarea readonly id=\"response-xml-").append(operationId).append("\">");
                htmlBuilder.append(generateSampleXml(operation.getOutputClass()));
                htmlBuilder.append("</textarea>");
                htmlBuilder.append("<button class=\"copy-button\" onclick=\"copyToClipboard('response-xml-").append(operationId).append("')\">Copy</button>");
                htmlBuilder.append("</div>");



                htmlBuilder.append("</div>");
            }
            //END OPERATIONS
            htmlBuilder.append("</div>");

        } else {
            htmlBuilder.append("<p>No operations available.</p>");
        }

        //END CONTROLLER
        htmlBuilder.append("</div>");

        return new EndpointHtmlResult(controllerId, serviceName, htmlBuilder.toString());
    }

    /**
     * Sanitizes the given input string by replacing all non-alphanumeric characters
     * and hyphens with a hyphen.
     *
     * @param input the input string to be sanitized.
     * @return a sanitized string with only alphanumeric characters and hyphens.
     */
    private String sanitizeId(String input) {
        return input.replaceAll("[^a-zA-Z0-9\\-]", "-");
    }

    /**
     * Generates a sample XML representation of the given class.
     * <p>
     * The sample XML is generated by traversing the class' fields and their
     * values. The method returns a string containing the sample XML.
     *
     * @param clazz the class to generate the sample XML for.
     * @return the sample XML representation of the given class.
     */
    private String generateSampleXml(Class<?> clazz) {
        return generateSampleXml(clazz, 0);
    }

    /**
     * Generates a sample XML representation of the given class at the given
     * indentation level.
     * <p>
     * The sample XML is generated by traversing the class' fields and their
     * values. The method returns a string containing the sample XML. The
     * indentation level is used to generate the correct indentation for the XML.
     *
     * @param clazz the class to generate the sample XML for.
     * @param indentLevel the indentation level to generate the XML with.
     * @return the sample XML representation of the given class at the given
     *         indentation level.
     */
    private String generateSampleXml(Class<?> clazz, int indentLevel) {
        StringBuilder xmlBuilder = new StringBuilder();
//        String indent = "  ".repeat(indentLevel);
        StringBuilder indentBuilder = new StringBuilder();
        for (int i = 0; i < indentLevel; i++) {
            indentBuilder.append("  ");
        }
        String indent = indentBuilder.toString();
        String rootElement = clazz.getSimpleName();
        xmlBuilder.append(indent).append("<").append(rootElement).append(">\n");

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            Class<?> fieldType = field.getType();
            xmlBuilder.append(indent).append("  <").append(fieldName).append(">");

            if (isPrimitiveOrWrapper(fieldType) || fieldType == String.class) {
                xmlBuilder.append(getSampleValue(fieldType.getSimpleName()));
            } else {
                xmlBuilder.append("\n");
                xmlBuilder.append(generateSampleXml(fieldType, indentLevel + 2));
                xmlBuilder.append(indent).append("  ");
            }

            xmlBuilder.append("</").append(fieldName).append(">\n");
        }

        xmlBuilder.append(indent).append("</").append(rootElement).append(">\n");
        return xmlBuilder.toString();
    }

    /**
     * Returns whether the given class is a primitive type or a wrapper type.
     * Wrapper types are classes that wrap primitive types, such as Integer,
     * Long, Double, Float, Boolean, Byte, Short, and Character.
     *
     * @param type the class to check.
     * @return true if the given class is a primitive type or a wrapper type,
     *         false otherwise.
     */
    private boolean isPrimitiveOrWrapper(Class<?> type) {
        return type.isPrimitive() ||
                type == Integer.class ||
                type == Long.class ||
                type == Double.class ||
                type == Float.class ||
                type == Boolean.class ||
                type == Byte.class ||
                type == Short.class ||
                type == Character.class;
    }

    /**
     * Returns a sample value for the given field type.
     * <p>
     * The sample value is a string that represents a default value for the given
     * field type. The value is chosen so that it is valid for the given type
     * and is easy to understand.
     *
     * @param fieldType the field type to generate a sample value for.
     * @return a sample value for the given field type.
     */
    private String getSampleValue(String fieldType) {
        switch (fieldType) {
            case "String":
                return "example";
            case "char":
            case "Character":
                return "a";
            case "int":
            case "Integer":
                return "0";
            case "long":
            case "Long":
                return "0";
            case "short":
            case "Short":
                return "0";
            case "byte":
            case "Byte":
                return "0";
            case "float":
            case "Float":
                return "0.0";
            case "double":
            case "Double":
                return "0.0";
            case "boolean":
            case "Boolean":
                return "true";
            case "BigInteger":
                return "0";
            case "BigDecimal":
                return "0.0";
            case "Date":
                return "2023-01-01T00:00:00Z";
            case "LocalDate":
                return "2023-01-01";
            case "LocalDateTime":
                return "2023-01-01T00:00:00";
            case "List":
            case "ArrayList":
            case "LinkedList":
                return "\n    <!-- List -->\n";
            case "Set":
            case "HashSet":
            case "TreeSet":
                return "\n    <!-- Set -->\n";
            case "Map":
            case "HashMap":
            case "TreeMap":
                return "\n    <!-- Map key-value -->\n";
            default:
                return "";
        }
    }


}
