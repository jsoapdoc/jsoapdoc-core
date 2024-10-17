package io.github.jsoapdoc.config;

import io.github.jsoapdoc.annotation.EnableSoapDocs;
import io.github.jsoapdoc.annotation.EndpointInfo;
import io.github.jsoapdoc.model.EndpointHtmlResult;
import io.github.jsoapdoc.service.HtmlGenerator;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@Component
public class SoapDocInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final SoapDocConfig soapDocConfig;
    private final HtmlGenerator htmlGenerator;

    public SoapDocInitializer(SoapDocConfig soapDocConfig, HtmlGenerator htmlGenerator) {
        this.soapDocConfig = soapDocConfig;
        this.htmlGenerator = htmlGenerator;
        System.out.println("Inizializzazione documentazione SOAP in corso...");
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println("Evento ApplicationReadyEvent intercettato...");

        // Verifica se la classe principale ha l'annotazione @EnableSoapDocs
        if (event.getSpringApplication().getMainApplicationClass().isAnnotationPresent(EnableSoapDocs.class)) {
            System.out.println("@EnableSoapDocs presente, procedo con la generazione...");

            String controllerPackage = soapDocConfig.getControllerPackage();  // Leggi il package dai config

            if (controllerPackage == null || controllerPackage.isEmpty()) {
                System.out.println("Il package dei controller non è stato configurato correttamente.");
                return;
            }

            try {
                // Usa Reflections per cercare le classi nel package dinamico
                Reflections reflections = new Reflections(new ConfigurationBuilder().forPackages(controllerPackage));
                Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(EndpointInfo.class);

                List<EndpointHtmlResult> controllerInfos = new ArrayList<>();

                for (Class<?> controllerClass : controllers) {
                    System.out.println("Generazione HTML per controller: " + controllerClass.getName());
                    EndpointHtmlResult result = htmlGenerator.generateHtmlForEndpoint(controllerClass);
                    if (result != null && !result.getContent().isEmpty()) {
                        controllerInfos.add(result);
                    }
                }

                // Genera la pagina HTML completa
                String fullHtml = generateFullHtml(controllerInfos);

                // Salva la documentazione in un file HTML
                writeToFile("soap-docs.html", fullHtml);

                System.out.println("Documentazione SOAP generata con successo.");

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Errore durante la generazione della documentazione SOAP.");
                // Genera una pagina di errore
                generateErrorPage();
            }
        } else {
            System.out.println("@EnableSoapDocs non è presente nella classe principale.");
        }
    }

    private String generateFullHtml(List<EndpointHtmlResult> controllerInfos) {
        StringBuilder htmlBuilder = new StringBuilder();

        htmlBuilder.append("<!DOCTYPE html>");
        htmlBuilder.append("<html><head><title>SOAP Service Documentation</title>");
        // CSS
        htmlBuilder.append("<style>");
        // BODY
        htmlBuilder.append("body {font-family: Roboto, sans-serif; margin: 20px; padding: 0; background-color: #f4f4f9;}");
        // HEADER
        htmlBuilder.append(".header-container {display: flex; justify-content: space-around; align-items: center;}");
        htmlBuilder.append(".header-container img {width: 100px; height: 100px;}");
        htmlBuilder.append(".header-container h1 {color: #12264B; padding-bottom: 10px; font-size: 26px; text-align: center;}");
        // HR
        htmlBuilder.append("hr {background-color: #12264B; border: 0; height: 10px; margin: 10px 0; border-radius: 5px;}");
//        htmlBuilder.append("hr {border: 0; height: 10px; background-image: linear-gradient(90deg, #81D8D0, #5BEAEE); background-size: 200% 100%; animation: gradientMove 5s ease infinite; margin: 10px 0; border-radius: 5px;}");
        // HR ANIMATION
//        htmlBuilder.append("@keyframes gradientMove {");
//        htmlBuilder.append("0% {background-position: 0% 50%;}");
//        htmlBuilder.append("50% {background-position: 100% 50%;}");
//        htmlBuilder.append("100% {background-position: 0% 50%;}");
//        htmlBuilder.append("}");
        // DROPDOWN
        htmlBuilder.append(".dropdown {margin-bottom: 20px;}");
        htmlBuilder.append(".dropdown label {font-size: 18px; margin-right: 10px;}");
        htmlBuilder.append(".dropdown select {padding: 5px 10px; font-size: 16px;}");
        htmlBuilder.append(".tab-menu {display: flex; justify-content: center; margin-bottom: 20px;}");
        htmlBuilder.append(".tab-button {background-color: rgb(2, 187, 255); color: white; border: none; padding: 10px 20px; cursor: pointer; margin: 0 5px; font-size: 16px; border-radius: 5px;}");
        htmlBuilder.append(".tab-button.active {background-color: rgb(0, 88, 121);}");
        htmlBuilder.append(".content-container {display: none;}");
        htmlBuilder.append(".content-container.active {display: block;}");
        // H2
        htmlBuilder.append("h2 {color: #333; margin-top: 20px; text-align: center;}");
        // H3
        htmlBuilder.append("h3 {color: #333; margin-top: 20px; text-align: center;}");
        htmlBuilder.append(".container {display: grid; grid-template-columns: 1fr 1fr; gap: 10px;}");
        htmlBuilder.append("ul {list-style-type: none; padding: 0; margin: 0;}");
        htmlBuilder.append("li {background-color: #ffffff; border: 1px solid #ddd; margin-bottom: 10px; padding: 15px; border-radius: 5px;}");
        htmlBuilder.append("li strong {font-size: 18px; color: #333;}");
        htmlBuilder.append("li ul {padding-left: 20px;}");
        htmlBuilder.append("li ul li {background-color: #f9f9f9; border: none; margin-bottom: 5px; padding: 8px; border-radius: 3px;}");
        // TABLE
        htmlBuilder.append("table {width: 100%; border-collapse: collapse; margin-bottom: 20px;}");
        htmlBuilder.append("th, td {border: 1px solid #ddd; padding: 8px; text-align: left;}");
        htmlBuilder.append("th {background-color: #f2f2f2;}");
        htmlBuilder.append(".operation {margin-bottom: 30px;}");
        // TEXT AREA
        htmlBuilder.append(".textarea-container {position: relative; margin-top: 10px;}");
        htmlBuilder.append(".textarea-container textarea { width: 100%; height: 200px; padding: 10px; font-family: monospace; font-size: 14px; border: 1px solid #ddd; border-radius: 5px; background-color: #f9f9f9; resize: none; box-sizing: border-box;}");
        htmlBuilder.append(".textarea-container button.copy-button {position: absolute; top: 10px; right: 10px; padding: 5px 10px; font-size: 12px; background-color: #008CBA; color: white; border: none; border-radius: 3px; cursor: pointer;}");
        htmlBuilder.append(".textarea-container button.copy-button:hover {background-color: #006F9A;}");
        // BUTTON
        htmlBuilder.append("button {margin-top: 5px; padding: 10px 15px; font-size: 14px; background-color: #008CBA; color: white; border: none; border-radius: 5px; cursor: pointer;}");
        htmlBuilder.append("button:hover {background-color: #006F9A;}");
        // FOOTER
        htmlBuilder.append("footer { background-color: #12264B; color: #81D8D0; text-align: center; padding: 20px 0; position: relative; bottom: 0; width: 100%; border-radius: 5px; } ");
        htmlBuilder.append(".footer-container p { margin: 0; font-size: 14px; opacity: 0.8; } ");
        htmlBuilder.append("</style>");
        htmlBuilder.append("</head><body>");

        // HEADER
        htmlBuilder.append("<div class=\"header-container\">");
        htmlBuilder.append("<h1>SOAP Service Documentation</h1>");
        htmlBuilder.append("<img src=\"https://i.postimg.cc/dZZPzzJR/soap.png\" alt=\"logo\">");
        htmlBuilder.append("</div><hr>");

        // DROPDOWN
        htmlBuilder.append("<div class=\"dropdown\">");
        htmlBuilder.append("<label for=\"controllerSelect\">Select Endpoint:</label>");
        htmlBuilder.append("<select id=\"controllerSelect\" onchange=\"showControllerContent()\">");
        htmlBuilder.append("<option value=\"\" disabled selected>-- Select Endpoint --</option>");
        for (EndpointHtmlResult controllerInfo : controllerInfos) {
            htmlBuilder.append("<option value=\"").append(controllerInfo.getControllerId()).append("\">")
                    .append(controllerInfo.getDisplayName()).append("</option>");
        }
        htmlBuilder.append("</select>");
        htmlBuilder.append("</div>");

        // DEFAULT CONTENT
        htmlBuilder.append("<div id=\"defaultContent\" class=\"default-content\">");
        htmlBuilder.append("<h2>Welcome to the SOAP Service Documentation!</h2>");
        htmlBuilder.append("<p style=\"text-align: center;\">Please select an endpoint from the dropdown above to view its details.</p>");
        htmlBuilder.append("</div>");

        // CONTENT OF CONTROLLER
        for (EndpointHtmlResult controllerInfo : controllerInfos) {
            htmlBuilder.append(controllerInfo.getContent());
        }

        //FOOTER
        htmlBuilder.append("<footer>");
        htmlBuilder.append("<div class=\"footer-container\">");
        htmlBuilder.append("<p>&copy;Copyright 2024 Danilo Pichilli. JSoapDoc All rights reserved.</p>");
        htmlBuilder.append("</div>");
        htmlBuilder.append("</footer>");

        // SCRIPT
        htmlBuilder.append("<script>");
        // showControllerContent()
        htmlBuilder.append("function showControllerContent() {");
        htmlBuilder.append("var select = document.getElementById('controllerSelect');");
        htmlBuilder.append("var selectedId = select.value;");
        htmlBuilder.append("var contents = document.getElementsByClassName('controller-content');");
        htmlBuilder.append("var defaultContent = document.getElementById('defaultContent');");
        htmlBuilder.append("for (var i = 0; i < contents.length; i++) {");
        htmlBuilder.append("contents[i].style.display = 'none';");
        htmlBuilder.append("} ");
        htmlBuilder.append("if (selectedId) {");
        htmlBuilder.append("var selectedElement = document.getElementById(selectedId);");
        htmlBuilder.append("if (selectedElement) {");
        htmlBuilder.append("selectedElement.style.display = 'block';");
        htmlBuilder.append("} ");
        htmlBuilder.append("defaultContent.style.display = 'none';");
        htmlBuilder.append("} else {");
        htmlBuilder.append("defaultContent.style.display = 'block';");
        htmlBuilder.append("}");
        htmlBuilder.append("} ");
        // copyToClipboard(elementId)
        htmlBuilder.append("function copyToClipboard(elementId) {");
        htmlBuilder.append("var textarea = document.getElementById(elementId);");
        htmlBuilder.append("textarea.select();");
        htmlBuilder.append("textarea.setSelectionRange(0, 99999);");
        htmlBuilder.append("document.execCommand('copy');");
        htmlBuilder.append("alert('Content copied to clipboard.');");
        htmlBuilder.append("}");
        htmlBuilder.append("</script>");

        htmlBuilder.append("</body></html>");

        return htmlBuilder.toString();
    }

    private void writeToFile(String fileName, String content) throws IOException {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(content);
        }
    }

    private void generateErrorPage() {
        String errorContent = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<title>Error</title>" +
                "<style>" +
                "body {font-family: Roboto, sans-serif; margin: 0; padding: 0; display: flex; justify-content: center; align-items: center; height: 100vh; background-color: #f4f4f9;}" +
                ".error-box {text-align: center; background-color: #fff; padding: 40px; border-radius: 10px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);}" +
                "h1 {color: #ff4f4f; font-size: 24px; margin: 0;}" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class=\"error-box\">" +
                "<h1>Error when generating SOAP docs</h1>" +
                "</div>" +
                "</body>" +
                "</html>";

        try {
            writeToFile("error.html", errorContent);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Errore durante la generazione della pagina di errore", e);
        }
    }
}
