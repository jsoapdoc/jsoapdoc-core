package com.jsoapdoc.soaplib.annotation;

import com.jsoapdoc.soaplib.model.OperationInfo;
import com.jsoapdoc.soaplib.model.ParameterInfo;
import com.jsoapdoc.soaplib.model.WsdlInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AnnotationProcessor {

    public static void processMethodDetailsAnnotation(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(MethodDetails.class)) {
                MethodDetails methodAnnotation = method.getAnnotation(MethodDetails.class);

                if (methodAnnotation.printInfo()) {
                    System.out.println("Method: " + method.getName());
                    System.out.println("Description: " + methodAnnotation.description());
                    System.out.println("Request Type: " + methodAnnotation.requestType().getSimpleName());
                    System.out.println("Response Type: " + methodAnnotation.responseType().getSimpleName());
                    System.out.println("Version: " + methodAnnotation.version());
                    System.out.println("Roles Allowed: " + String.join(", ", methodAnnotation.rolesAllowed()));
                    System.out.println();
                }
            }
        }
    }

    public static void processEndpointAnnotation(Class<?> clazz) {
        if (clazz.isAnnotationPresent(EndpointInfo.class)) {
            EndpointInfo endpointInfo = clazz.getAnnotation(EndpointInfo.class);

            System.out.println("Endpoint Name: " + endpointInfo.name());
            System.out.println("Description: " + endpointInfo.description());
            System.out.println("Version: " + endpointInfo.version());
            System.out.println("Roles Allowed: " + String.join(", ", endpointInfo.rolesAllowed()));
            System.out.println();
        }
    }

    public static WsdlInfo collectWsdlInfo(Class<?> endpointClass) {
        EndpointInfo endpointInfo = endpointClass.getAnnotation(EndpointInfo.class);
        if (endpointInfo == null) {
            return null;
        }

        WsdlInfo wsdlInfo = new WsdlInfo();
        wsdlInfo.setServiceName(endpointInfo.name());
        wsdlInfo.setDescription(endpointInfo.description());
        wsdlInfo.setVersion(endpointInfo.version());
        wsdlInfo.setRolesAllowed(endpointInfo.rolesAllowed());

        List<OperationInfo> operations = new ArrayList<>();

        Method[] methods = endpointClass.getDeclaredMethods();
        for (Method method : methods) {
            MethodDetails methodDetails = method.getAnnotation(MethodDetails.class);
            if (methodDetails != null) {
                OperationInfo operationInfo = new OperationInfo();
                operationInfo.setOperationName(method.getName());
                operationInfo.setDescription(methodDetails.description());
                operationInfo.setVersion(methodDetails.version());
                operationInfo.setRolesAllowed(methodDetails.rolesAllowed());

                // Process requestType
                Class<?> requestType = methodDetails.requestType();
                List<ParameterInfo> inputParameters = extractFieldsFromClass(requestType);
                operationInfo.setInputParameters(inputParameters);

                // Process responseType
                Class<?> responseType = methodDetails.responseType();
                List<ParameterInfo> outputParameters = extractFieldsFromClass(responseType);
                operationInfo.setOutputParameters(outputParameters);

                operationInfo.setInputClass(requestType);
                operationInfo.setOutputClass(responseType);

                operations.add(operationInfo);
            }
        }

        wsdlInfo.setOperations(operations);
        return wsdlInfo;
    }

    private static List<ParameterInfo> extractFieldsFromClass(Class<?> clazz) {
        List<ParameterInfo> parameters = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            ParameterInfo paramInfo = new ParameterInfo();
            paramInfo.setName(field.getName());
            paramInfo.setType(field.getType().getSimpleName());

            boolean required = false;

            if (field.isAnnotationPresent(Required.class)) {
                required = field.getAnnotation(Required.class).value();
            }

            paramInfo.setRequired(required);
            parameters.add(paramInfo);
        }
        return parameters;
    }
}
