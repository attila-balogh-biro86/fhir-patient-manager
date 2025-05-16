package nl.sdbgroep.patient.manager.api;

import java.util.List;

import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.stereotype.Component;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;

@Component
public class PatientResourceProvider implements IResourceProvider {

  @Override
  public Class<Patient> getResourceType() {
    return Patient.class;
  }

  @Read
  public Patient read(@IdParam IdType id) {
    Patient patient = new Patient();
    patient.setId(id);
    patient.addName().setFamily("Doe").addGiven("John");
    patient.setGender(Enumerations.AdministrativeGender.MALE);
    return patient;
  }

  @Search
  public List<Patient> search(@OptionalParam(name = Patient.SP_NAME) StringParam name) {

    Patient patient = new Patient();
    patient.setId(new IdType("Attila", name.getValue()));
    patient.addName().setFamily("Balogh-Biro").addGiven("Attila");
    patient.setGender(Enumerations.AdministrativeGender.MALE);

    return List.of();
  }
}

