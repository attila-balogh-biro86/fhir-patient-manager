package nl.sdbgroep.patient.manager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.spring.boot.autoconfigure.FhirRestfulServerCustomizer;
import nl.sdbgroep.patient.manager.api.PatientResourceProvider;

@Configuration
public class DefaultFhir4Customizer implements FhirRestfulServerCustomizer {

  private final PatientResourceProvider patientResourceProvider;

  @Autowired
  public DefaultFhir4Customizer(PatientResourceProvider patientResourceProvider) {
    this.patientResourceProvider = patientResourceProvider;
  }

  @Override
  public void customize(RestfulServer restfulServer) {
    restfulServer.setFhirContext(FhirContext.forR4());
    restfulServer.registerProvider(patientResourceProvider);
  }
}