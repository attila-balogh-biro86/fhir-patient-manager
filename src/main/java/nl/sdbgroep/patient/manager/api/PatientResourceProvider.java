package nl.sdbgroep.patient.manager.api;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

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
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import nl.sdbgroep.patient.manager.dao.OrganizationRepository;
import nl.sdbgroep.patient.manager.dao.PatientRepository;
import nl.sdbgroep.patient.manager.model.PatientEntity;

@Slf4j
@Component
public class PatientResourceProvider implements IResourceProvider {

  private final PatientRepository patientRepository;

  @Autowired
  public PatientResourceProvider(PatientRepository patientRepository, OrganizationRepository organizationRepository) {
    this.patientRepository = patientRepository;
  }

  @Override
  public Class<Patient> getResourceType() {
    return Patient.class;
  }

  @Read
  public Patient getPatientById(@IdParam IdType id) {
     log.debug("Patient queried by id {}", id);
     PatientEntity patientEntity = patientRepository.findByPatientFhirId(id.getIdPart());
     if(patientEntity == null) {
       throw new ResourceNotFoundException(id);
     }
     return patientEntity.getPatient();
  }

  @Update
  public MethodOutcome update(@ResourceParam Patient patient) {
    log.debug("Patient updated {}", patient.getId());
    PatientEntity patientEntity = patientRepository.findByPatientFhirId(patient.getIdPart());
    patientEntity.setPatient(patient);
    patientRepository.save(patientEntity);
    return new MethodOutcome(new IdType(patient.getResourceType().name(), patient.getId()), false);
  }

  @Create
  public MethodOutcome createPatient(@ResourceParam  Patient patient) {
    log.debug("Patient created {}", patient);
    String newId = UUID.randomUUID().toString().replace("-", "");
    patient.setId(new IdType(patient.getResourceType().name(), newId));
    PatientEntity patientEntity = new PatientEntity();
    patientEntity.setPatient(patient);
    patientEntity.setPatientFhirId(patient.getIdPart());
    if(!patient.getName().isEmpty()) {
      patientEntity.setPatientName(patient.getName().get(0).getNameAsSingleString());
    }
    patientRepository.save(patientEntity);
    return new MethodOutcome(new IdType(patient.getResourceType().name(), patient.getId()), true);
  }

  @Search
  public IBundleProvider searchPatientByFamilyName(@RequiredParam(name = Patient.SP_FAMILY) StringParam name, RequestDetails requestDetails) {
    log.debug("Searching for patient by family name {}", name);
    List<PatientEntity> patients = patientRepository.findByFamilyName(name.getValue());
    List<Patient> patientList = new ArrayList<>();
    patients.forEach(patient -> patientList.add(patient.getPatient()));
    return new SimpleBundleProvider(patientList);
  }

  @Search
  public IBundleProvider searchAllPatients() {
    log.debug("Retrieving all patients");
    List<Patient> patientList = new ArrayList<Patient>();
    List<PatientEntity> patientEntities = StreamSupport
        .stream(patientRepository.findAll().spliterator(), false)
        .toList();
    patientEntities.forEach(organizationEntity -> patientList.add(organizationEntity.getPatient()));
    return new SimpleBundleProvider(patientList);
  }
}

