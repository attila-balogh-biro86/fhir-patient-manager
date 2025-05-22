package nl.sdbgroep.patient.manager.api;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import nl.sdbgroep.patient.manager.dao.OrganizationRepository;
import nl.sdbgroep.patient.manager.dao.PatientRepository;
import nl.sdbgroep.patient.manager.model.PatientEntity;

@Slf4j
@Component
public class PatientResourceProvider implements IResourceProvider {

  private final PatientRepository patientRepository;

  private final OrganizationRepository organizationRepository;

  @Autowired
  public PatientResourceProvider(PatientRepository patientRepository, OrganizationRepository organizationRepository) {
    this.patientRepository = patientRepository;
    this.organizationRepository = organizationRepository;
  }

  @Override
  public Class<Patient> getResourceType() {
    return Patient.class;
  }

  @Read
  public Patient getPatientById(@IdParam IdType id, RequestDetails requestDetails) {
     log.debug("Patient queried by id {}", id);
     Patient patient = new Patient();
     patient.setIdElement(new IdType("Patient", id.getIdPart()));
     return patient;
  }

  @Update
  public MethodOutcome update(@ResourceParam Patient patient) {
    log.debug("Patient updated {}", patient.getId());
    return new MethodOutcome(new IdType("Patient", 4452L), false);
  }

  @Create
  public MethodOutcome createPatient(@ResourceParam  Patient patient) {
    log.debug("Patient created {}", patient);
    return new MethodOutcome(new IdType("Patient", patient.getId()), true);
  }

  @Search
  public IBundleProvider searchPatientByFamilyName(@RequiredParam(name = Patient.SP_FAMILY) StringParam name, RequestDetails requestDetails) {
    log.debug("Searching for patient by family name {}", name);
    List<PatientEntity> patients = patientRepository.findByFamilyName(name.getValue());
    List<Patient> patientList = new ArrayList<Patient>();
    patients.forEach(patient -> patientList.add(patient.getFhirPayload()));
    return new SimpleBundleProvider(patientList);
  }
}

