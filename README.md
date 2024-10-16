# JSOAPDOC

### JSOAPDOC is a Java library designed to automatically generate HTML documentation for SOAP web services built with Spring Boot. By annotating your endpoints and methods, you can produce comprehensive documentation that includes endpoint details, operations, request and response structures, and sample XML messages.

1. Add jsoapdoc to Your Project
2. Configure the Library
3. Annotate Your Endpoints and Methods
4. Enable jsoapdoc


   Automatic Documentation Generation
   
   Generates an HTML page with detailed documentation of your SOAP endpoints.

   ### **Endpoint and Operation Details**: *Includes service names, descriptions, versions, and roles allowed.*

   ### **Request and Response Structures**: *Displays parameters, types, and whether they are required.*

   ### **Sample XML Messages**: *Provides sample request and response XML messages based on your classes.*

   ### **Copy Functionality**: *Allows users to copy sample XML messages directly from the documentation.*

   **Installation**

   To include jsoapdoc in your project, add the following dependency to your pom.xml:


    <dependency>
    <groupId>jsoapdoc.soaplib</groupId>
    <artifactId>jsoapdoc</artifactId>
    <version>0.0.1</version>
    </dependency>

   **Usage**

1. **Add jsoapdoc to Your Project.** Include the dependency in your pom.xml as shown above.

2. **Configure the Library.** Specify the package where your endpoint classes are located in your application's application.properties or application.yml file.

For application.properties:

    soapdoc.controllerPackage=com.yourcompany.yourapp.controller

For application.yml:

    soapdoc:
        controllerPackage: com.yourcompany.yourapp.controller

Note: Replace com.yourcompany.yourapp.controller with the actual package name where your endpoint classes are located.

3. **Enable jsoapdoc**

   Add the @EnableSoapDocs annotation to your main application class and include a component scan for com.jsoapdoc.soaplib.
 ```java
    import com.jsoapdoc.soaplib.annotation.EnableSoapDocs;
    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;
    import org.springframework.context.annotation.ComponentScan;
    
    @SpringBootApplication
    @EnableSoapDocs
    @ComponentScan(basePackages = "com.jsoapdoc.soaplib")
    public class YourApplication {
    
        public static void main(String[] args) {
            SpringApplication.run(YourApplication.class, args);
        }
    }
```
***Note:*** **The @ComponentScan annotation is necessary to ensure that jsoapdoc's components are discovered by Spring Boot.**

**Ensure that the controllerPackage property matches the package where your endpoint classes are located.**

4. **Annotate Your Endpoints and Methods**
   - Annotate Endpoint Classes:

Use @EndpointInfo to provide general information about your service.

```java
import com.jsoapdoc.soaplib.annotation.EndpointInfo;
import org.springframework.ws.server.endpoint.annotation.Endpoint;

@Endpoint
@EndpointInfo(
    name = "Calculator Service",
    description = "Provides basic arithmetic operations",
    version = "1.0",
    rolesAllowed = {"USER", "ADMIN"}
    )
public class CalculatorEndpoint {
    // ...
}
```
   - Annotate Methods:

Use @MethodDetails to provide information about each operation.
```java
import com.jsoapdoc.soaplib.annotation.MethodDetails;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
    
@PayloadRoot(namespace = NAMESPACE_URI, localPart = "AddRequest")
@ResponsePayload
@MethodDetails(
    description = "Adds two numbers",
    requestType = AddRequest.class,
    responseType = AddResponse.class,
    version = "1.0",
    rolesAllowed = {"USER", "ADMIN"},
)
public AddResponse add(AddRequest request) {
    // Implementation
}
```

Annotate Fields for Required Parameters:

Use @Required to specify if a field is mandatory in your request or response classes.

```java
import com.jsoapdoc.soaplib.annotation.Required;
    
public class AddRequest {
    
    @Required
    private Integer a;
    
    @Required
    private Integer b;
    
    private String comment; // Optional field
}
```

Annotations Reference

    @EndpointInfo

Annotates an endpoint class to provide service-level metadata.

    name (required): The name of the service.
    description: A brief description of the service.
    version: The version of the service.
    rolesAllowed: An array of roles that are allowed to access the service.

Annotates a method to provide operation-level metadata.

    @MethodDetails

    description: A brief description of the operation.
    requestType (required): The class representing the request type.
    responseType (required): The class representing the response type.
    version: The version of the operation.
    rolesAllowed: An array of roles that are allowed to access the operation.







License

This project is licensed under the MIT License - see the LICENSE file for details.

Additional Notes:

Component Scan Requirement: The @ComponentScan annotation in your main application class is necessary for Spring to detect jsoapdoc's components. Ensure that basePackages is set to "com.jsoapdoc.soaplib".

Generated Documentation Location: The soap-docs.html file will be generated in the root of your project directory. Ensure your application has write permissions to this location.

For reference documentation, see [jSOAPDoc](https://github.com/danilopichilli/jsoapdoc).

This project uses [jSOAPDoc](https://github.com/danilopichilli/jsoapdochelper) for automatic SOAP documentation generation.