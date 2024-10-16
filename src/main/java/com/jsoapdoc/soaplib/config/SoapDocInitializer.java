package com.jsoapdoc.soaplib.config;

import com.jsoapdoc.soaplib.annotation.EnableSoapDocs;
import com.jsoapdoc.soaplib.annotation.EndpointInfo;
import com.jsoapdoc.soaplib.model.EndpointHtmlResult;
import com.jsoapdoc.soaplib.service.HtmlGenerator;
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
        // Include CSS
        htmlBuilder.append("<style>");
        // Inserisci qui il tuo CSS esistente
        htmlBuilder.append("body {font-family: Arial, sans-serif; margin: 20px; padding: 0; background-color: #f4f4f9;}");
        htmlBuilder.append(".header-container {display: flex; align-items: center; justify-content: center;}");
        htmlBuilder.append(".header-container img {width: 100px; height: 100px; position: absolute; right: 0; top: 0;}");
        htmlBuilder.append(".header-container h1 {color: #333; padding-bottom: 10px; font-size: 26px; text-align: center;}");
        htmlBuilder.append("hr {border: 0; height: 2px; background-color: rgb(0, 255, 242); margin: 10px 0;}");
        htmlBuilder.append(".dropdown {margin-bottom: 20px;}");
        htmlBuilder.append(".dropdown label {font-size: 18px; margin-right: 10px;}");
        htmlBuilder.append(".dropdown select {padding: 5px 10px; font-size: 16px;}");
        htmlBuilder.append(".tab-menu {display: flex; justify-content: center; margin-bottom: 20px;}");
        htmlBuilder.append(".tab-button {background-color: rgb(2, 187, 255); color: white; border: none; padding: 10px 20px; cursor: pointer; margin: 0 5px; font-size: 16px; border-radius: 5px;}");
        htmlBuilder.append(".tab-button.active {background-color: rgb(0, 88, 121);}");
        htmlBuilder.append(".content-container {display: none;}");
        htmlBuilder.append(".content-container.active {display: block;}");
        htmlBuilder.append("h2 {color: rgb(2, 187, 255); margin-top: 20px; font-size: 22px; text-align: center;}");
        htmlBuilder.append(".container {display: grid; grid-template-columns: 1fr 1fr; gap: 10px;}");
        htmlBuilder.append("ul {list-style-type: none; padding: 0; margin: 0;}");
        htmlBuilder.append("li {background-color: #ffffff; border: 1px solid #ddd; margin-bottom: 10px; padding: 15px; border-radius: 5px;}");
        htmlBuilder.append("li strong {font-size: 18px; color: #333;}");
        htmlBuilder.append("li ul {padding-left: 20px;}");
        htmlBuilder.append("li ul li {background-color: #f9f9f9; border: none; margin-bottom: 5px; padding: 8px; border-radius: 3px;}");
        htmlBuilder.append("table {width: 100%; border-collapse: collapse; margin-bottom: 20px;}");
        htmlBuilder.append("th, td {border: 1px solid #ddd; padding: 8px; text-align: left;}");
        htmlBuilder.append("th {background-color: #f2f2f2;}");
        htmlBuilder.append(".operation {margin-bottom: 30px;}");
        //htmlBuilder.append("textarea { width: 100%; padding: 10px; font-family: monospace; font-size: 14px; }");
        htmlBuilder.append(".textarea-container {position: relative; margin-top: 10px;}");
        htmlBuilder.append(".textarea-container textarea { width: 100%; height: 200px; padding: 10px; font-family: monospace; font-size: 14px; border: 1px solid #ddd; border-radius: 5px; background-color: #f9f9f9; resize: none; box-sizing: border-box;}");
        htmlBuilder.append(".textarea-container button.copy-button {position: absolute; top: 10px; right: 10px; padding: 5px 10px; font-size: 12px; background-color: #008CBA; color: white; border: none; border-radius: 3px; cursor: pointer;}");
        htmlBuilder.append(".textarea-container button.copy-button:hover {background-color: #006F9A;}");
        htmlBuilder.append("button {margin-top: 5px; padding: 10px 15px; font-size: 14px; background-color: #008CBA; color: white; border: none; border-radius: 5px; cursor: pointer;}");
        htmlBuilder.append("button:hover {background-color: #006F9A;}");
        htmlBuilder.append("</style>");
        htmlBuilder.append("</head><body>");

        // Contenuto principale
        htmlBuilder.append("<div class=\"header-container\">");
        htmlBuilder.append("<h1>SOAP Service Documentation</h1>");
        htmlBuilder.append("<img src=\"https://i.postimg.cc/dZZPzzJR/soap.png\" alt=\"logo\">");
        htmlBuilder.append("</div><hr>");

        // Dropdown per la selezione dei controller
        htmlBuilder.append("<div class=\"dropdown\">");
        htmlBuilder.append("<label for=\"controllerSelect\">Seleziona Endpoint:</label>");
        htmlBuilder.append("<select id=\"controllerSelect\" onchange=\"showControllerContent()\">");
        htmlBuilder.append("<option value=\"\">--Seleziona Endpoint--</option>");
        for (EndpointHtmlResult controllerInfo : controllerInfos) {
            htmlBuilder.append("<option value=\"").append(controllerInfo.getControllerId()).append("\">")
                    .append(controllerInfo.getDisplayName()).append("</option>");
        }
        htmlBuilder.append("</select>");
        htmlBuilder.append("</div>");

        // Contenuti dei controller
        for (EndpointHtmlResult controllerInfo : controllerInfos) {
            htmlBuilder.append(controllerInfo.getContent());
        }

        // Script JavaScript per gestire la visualizzazione
        htmlBuilder.append("<script>");
        htmlBuilder.append("function showControllerContent() {");
        htmlBuilder.append("var select = document.getElementById('controllerSelect');");
        htmlBuilder.append("var selectedId = select.value;");
        htmlBuilder.append("var contents = document.getElementsByClassName('controller-content');");
        htmlBuilder.append("for (var i = 0; i < contents.length; i++) {");
        htmlBuilder.append("contents[i].style.display = 'none';");
        htmlBuilder.append("}");
        htmlBuilder.append("if (selectedId) {");
        htmlBuilder.append("var selectedElement = document.getElementById(selectedId);");
        htmlBuilder.append("if (selectedElement) {");
        htmlBuilder.append("selectedElement.style.display = 'block';");
        htmlBuilder.append("}");
        htmlBuilder.append("}}");
        htmlBuilder.append("function copyToClipboard(elementId) {");
        htmlBuilder.append("var textarea = document.getElementById(elementId);");
        htmlBuilder.append("textarea.select();");
        htmlBuilder.append("textarea.setSelectionRange(0, 99999);");
        htmlBuilder.append("document.execCommand('copy');");
        htmlBuilder.append("alert('Contenuto copiato negli appunti');");
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
                "body {font-family: Arial, sans-serif; margin: 0; padding: 0; display: flex; justify-content: center; align-items: center; height: 100vh; background-color: #f4f4f9;}" +
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
