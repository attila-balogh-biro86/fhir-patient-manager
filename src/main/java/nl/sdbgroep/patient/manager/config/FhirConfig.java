package nl.sdbgroep.patient.manager.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.LoggingInterceptor;
import jakarta.servlet.Servlet;
import nl.sdbgroep.patient.manager.api.PatientResourceProvider;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class FhirConfig {

  @Bean
  public FhirContext fhirContext() {
    return FhirContext.forR4();
  }

  @Bean
  public ServletRegistrationBean<Servlet> fhirServlet(PatientResourceProvider patientResourceProvider) {
    RestfulServer server = new RestfulServer(fhirContext());
    server.setResourceProviders(List.of(patientResourceProvider));
    server.registerInterceptor(new LoggingInterceptor());

    ServletRegistrationBean<Servlet> servletRegistrationBean = new ServletRegistrationBean<>(server, "/fhir/*");
    servletRegistrationBean.setLoadOnStartup(1);
    return servletRegistrationBean;
  }
}

