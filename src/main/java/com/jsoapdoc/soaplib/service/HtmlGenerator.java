package com.jsoapdoc.soaplib.service;

import com.jsoapdoc.soaplib.annotation.AnnotationProcessor;
import com.jsoapdoc.soaplib.model.EndpointHtmlResult;
import com.jsoapdoc.soaplib.model.OperationInfo;
import com.jsoapdoc.soaplib.model.ParameterInfo;
import com.jsoapdoc.soaplib.model.WsdlInfo;
import com.jsoapdoc.soaplib.config.SoapDocConfig;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;

@Component
public class HtmlGenerator {

    private final SoapDocConfig config;

    public HtmlGenerator(SoapDocConfig config) {
        this.config = config;
    }

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
        htmlBuilder.append("<h1>").append(serviceName).append("</h1>");
        htmlBuilder.append("</div><hr>");

        htmlBuilder.append("<p><strong>Description:</strong> ").append(wsdlInfo.getDescription()).append("</p>");
        htmlBuilder.append("<p><strong>Version:</strong> ").append(wsdlInfo.getVersion()).append("</p>");
        htmlBuilder.append("<p><strong>Roles Allowed:</strong> ").append(String.join(", ", wsdlInfo.getRolesAllowed())).append("</p>");
        htmlBuilder.append("<hr>");

        if (wsdlInfo.getOperations() != null && !wsdlInfo.getOperations().isEmpty()) {
            List<OperationInfo> operations = wsdlInfo.getOperations();

            htmlBuilder.append("<div id=\"").append(controllerId).append("-operations\" class=\"content-container active\">");

            for (OperationInfo operation : operations) {
                htmlBuilder.append("<div class=\"operation\">");
                htmlBuilder.append("<h2>").append(operation.getOperationName()).append("</h2>");
                htmlBuilder.append("<p><strong>Description:</strong> ").append(operation.getDescription()).append("</p>");
                htmlBuilder.append("<p><strong>Version:</strong> ").append(operation.getVersion()).append("</p>");
                htmlBuilder.append("<p><strong>Roles Allowed:</strong> ").append(String.join(", ", operation.getRolesAllowed())).append("</p>");

                // Input Parameters
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

                //Text area con il SOAP input
                htmlBuilder.append("</table>");
                htmlBuilder.append("<h4>Sample Request XML</h4>");
                htmlBuilder.append("<div class=\"textarea-container\">");
                htmlBuilder.append("<textarea readonly id=\"request-xml-").append(operationId).append("\">");
                htmlBuilder.append(generateSampleXml(operation.getInputClass()));
                htmlBuilder.append("</textarea>");
                htmlBuilder.append("<button class=\"copy-button\" onclick=\"copyToClipboard('request-xml-").append(operationId).append("')\">Copy</button>");
                htmlBuilder.append("</div>");

                // Output Parameters
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

                //Text area con il SOAP output
                htmlBuilder.append("</table>");
                htmlBuilder.append("<h4>Sample Response XML</h4>");
                htmlBuilder.append("<div class=\"textarea-container\">");
                htmlBuilder.append("<textarea readonly id=\"response-xml-").append(operationId).append("\">");
                htmlBuilder.append(generateSampleXml(operation.getOutputClass()));
                htmlBuilder.append("</textarea>");
                htmlBuilder.append("<button class=\"copy-button\" onclick=\"copyToClipboard('response-xml-").append(operationId).append("')\">Copy</button>");
                htmlBuilder.append("</div>");



                htmlBuilder.append("</div><hr>");
            }

            htmlBuilder.append("</div>"); // Fine sezione operazioni

        } else {
            htmlBuilder.append("<p>No operations available.</p>");
        }

        htmlBuilder.append("</div>"); // Fine del div del controller

        return new EndpointHtmlResult(controllerId, serviceName, htmlBuilder.toString());
    }

    private String sanitizeId(String input) {
        return input.replaceAll("[^a-zA-Z0-9\\-]", "-");
    }

    private String generateSampleXml(Class<?> clazz) {
        return generateSampleXml(clazz, 0);
    }

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
                return "\n    <!-- Lista di elementi -->\n";
            case "Set":
            case "HashSet":
            case "TreeSet":
                return "\n    <!-- Set di elementi -->\n";
            case "Map":
            case "HashMap":
            case "TreeMap":
                return "\n    <!-- Mappa chiave-valore -->\n";
            default:
                return "";
        }
    }


}
