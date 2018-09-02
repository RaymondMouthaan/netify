package org.mouthaan.netify.soap.config;


import org.mouthaan.netify.soap.exception.DetailSoapFaultDefinitionExceptionResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.server.endpoint.SoapFaultDefinition;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;
import org.springframework.ws.soap.server.endpoint.interceptor.PayloadValidatingInterceptor;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;
import org.springframework.ws.wsdl.wsdl11.Wsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;

import java.util.List;
import java.util.Properties;

@EnableWs
@Configuration
public class WebServiceConfiguration extends WsConfigurerAdapter {
    private static final String BASE_URL = "/movies";

    private final MessageSource messageSource;

    @Autowired
    public WebServiceConfiguration(MessageSource messageSource) {
        super();
        this.messageSource = messageSource;
    }

    @Bean
    public SoapFaultMappingExceptionResolver exceptionResolver(){
        SoapFaultMappingExceptionResolver exceptionResolver = new DetailSoapFaultDefinitionExceptionResolver(messageSource);

        SoapFaultDefinition faultDefinition = new SoapFaultDefinition();
        faultDefinition.setFaultCode(SoapFaultDefinition.SERVER);
        exceptionResolver.setDefaultFault(faultDefinition);

        Properties errorMappings = new Properties();
        errorMappings.setProperty(Exception.class.getName(), SoapFaultDefinition.SERVER.toString());
        //errorMappings.setProperty(ServiceFaultException.class.getName(), SoapFaultDefinition.SERVER.toString());
        exceptionResolver.setExceptionMappings(errorMappings);
        exceptionResolver.setOrder(1);
        return exceptionResolver;
    }

    @Override
    public void addInterceptors(List<EndpointInterceptor> interceptors) {
        PayloadValidatingInterceptor validatingInterceptor = new PayloadValidatingInterceptor();
        validatingInterceptor.setValidateRequest(true);
        validatingInterceptor.setValidateResponse(false); // Responses are not validated
        validatingInterceptor.setXsdSchema(defaultXsdSchema());
        interceptors.add(validatingInterceptor);
    }

    @Bean
    public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean(servlet, BASE_URL + "/*");
    }

    @Bean(name = "netify_v3.0")
    public Wsdl11Definition defaultWsdl11Definition() {
        SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
        wsdl11Definition.setWsdl(new ClassPathResource("ws-service-api-definition/netify_v3.0.wsdl"));
        return wsdl11Definition;
    }

    @Bean(name = "netify_schema_v3.0")
    public SimpleXsdSchema defaultXsdSchema() {
        SimpleXsdSchema simpleXsdSchema = new SimpleXsdSchema();
        simpleXsdSchema.setXsd(new ClassPathResource("ws-service-api-definition/netify_schema_v3.0.xsd"));
        return simpleXsdSchema;
    }

/*    @Bean(name = "netify_exception")
    public SimpleXsdSchema exceptionXsdSchema() {
        SimpleXsdSchema simpleXsdSchema = new SimpleXsdSchema();
        simpleXsdSchema.setXsd(new ClassPathResource("ws-service-api-definition/schema/netify_exception.xsd"));
        return simpleXsdSchema;
    }*/

    @Bean(name = "netify_general")
    public SimpleXsdSchema generalXsdSchema() {
        SimpleXsdSchema simpleXsdSchema = new SimpleXsdSchema();
        simpleXsdSchema.setXsd(new ClassPathResource("ws-service-api-definition/schema/netify_general.xsd"));
        return simpleXsdSchema;
    }

    @Bean(name = "netify_actor")
    public SimpleXsdSchema actorXsdSchema() {
        SimpleXsdSchema simpleXsdSchema = new SimpleXsdSchema();
        simpleXsdSchema.setXsd(new ClassPathResource("ws-service-api-definition/schema/netify_actor.xsd"));
        return simpleXsdSchema;
    }

    @Bean(name = "netify_role")
    public SimpleXsdSchema roleXsdSchema() {
        SimpleXsdSchema simpleXsdSchema = new SimpleXsdSchema();
        simpleXsdSchema.setXsd(new ClassPathResource("ws-service-api-definition/schema/netify_role.xsd"));
        return simpleXsdSchema;
    }

    @Bean(name = "netify_genre")
    public SimpleXsdSchema genreXsdSchema() {
        SimpleXsdSchema simpleXsdSchema = new SimpleXsdSchema();
        simpleXsdSchema.setXsd(new ClassPathResource("ws-service-api-definition/schema/netify_genre.xsd"));
        return simpleXsdSchema;
    }

    @Bean(name = "netify_movie")
    public SimpleXsdSchema movieXsdSchema() {
        SimpleXsdSchema simpleXsdSchema = new SimpleXsdSchema();
        simpleXsdSchema.setXsd(new ClassPathResource("ws-service-api-definition/schema/netify_movie.xsd"));
        return simpleXsdSchema;
    }
}
