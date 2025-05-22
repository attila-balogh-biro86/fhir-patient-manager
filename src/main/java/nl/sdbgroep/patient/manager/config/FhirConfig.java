package nl.sdbgroep.patient.manager.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.LoggingInterceptor;

import nl.sdbgroep.patient.manager.api.PatientResourceProvider;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

import jakarta.servlet.Servlet;

@EnableJpaRepositories(basePackages = "nl.sdbgroep.patient.manager.dao")
@EntityScan(basePackages = "nl.sdbgroep.patient.manager.model")
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

    ServletRegistrationBean<Servlet> servletRegistrationBean = new ServletRegistrationBean<Servlet>(server, "/fhir/r4/*");
    servletRegistrationBean.setLoadOnStartup(1);
    return servletRegistrationBean;
  }
}

