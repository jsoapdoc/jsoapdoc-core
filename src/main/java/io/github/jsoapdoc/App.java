//package com.mylibrary.soap;
//
//import com.mylibrary.soap.service.WsdlService;
//
//import java.io.FileWriter;
//import java.io.IOException;
//
//public class App {
//
//    public static void main(String[] args) {
//        WsdlService wsdlService = new WsdlService();
//
//        try {
//            String htmlContent = wsdlService.getSoapUi("http://localhost:8085/ws/calculator.wsdl");
//
//            writeHtmlToFile(htmlContent, "soap_ui_generated.html");
//            System.out.println("Html generato correttamente");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void writeHtmlToFile(String htmlContent, String fileName) throws IOException {
//        try (FileWriter writer = new FileWriter(fileName)) {
//            writer.write(htmlContent);
//        }
//    }
//}
