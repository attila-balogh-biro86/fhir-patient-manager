package nl.sdbgroep.patient.manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import nl.sdbgroep.patient.manager.dao.PatientRepository;
import nl.sdbgroep.patient.manager.model.PatientEntity;

@Testcontainers
@DataJpaTest
@TestPropertySource(properties = {
    "DATABASE_HOST=localhost",
    "DATABASE_PORT=15433"
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PatientRepositoryTest {

  private final FhirContext fhirContext = FhirContext.forR4();
  private final IParser jsonParser = fhirContext.newJsonParser();

  @Container
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
      .withDatabaseName("fhir")
      .withUsername("fhir")
      .withPassword("fhir");

  @DynamicPropertySource
  static void overrideProps(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
  }

  @Autowired
  private PatientRepository patientRepository;

  @Test
  void testPatientCreation() throws Exception {

    Path path = new ClassPathResource("test-patient.json").getFile().toPath();
    String json = Files.readString(path);

    // Deserialize into Patient
    Patient patient = jsonParser.parseResource(Patient.class, json);

    assertNotNull(patient);

    PatientEntity patientEntity = new PatientEntity();
    patientEntity.setPatient(patient);
    patientEntity.setPatientFhirId(patient.getId());
    patientEntity.setPatientName(patient.getName().get(0).getNameAsSingleString());


    PatientEntity savedEntity = patientRepository.save(patientEntity);
    PatientEntity retrievedEntity = patientRepository.findById(savedEntity.getId()).get();

    assertNotNull(savedEntity);
    assertNotNull(retrievedEntity);
  }

  @Test
  void testSearchByFamilyName() throws Exception {

    Path path = new ClassPathResource("test-patient.json").getFile().toPath();
    String json = Files.readString(path);

    // Deserialize into Patient
    Patient patient = jsonParser.parseResource(Patient.class, json);

    assertNotNull(patient);

    PatientEntity patientEntity = new PatientEntity();
    patientEntity.setPatient(patient);
    patientEntity.setPatientFhirId(patient.getId());
    patientEntity.setPatientName(patient.getName().get(0).getNameAsSingleString());

    PatientEntity savedEntity = patientRepository.save(patientEntity);
    final String familyName = savedEntity.getPatient().getName().get(0).getFamily();
    List<PatientEntity> retrievedEntity = patientRepository.findByFamilyName(patient.getName().get(0).getFamily());

    assertNotNull(savedEntity);
    assertNotNull(retrievedEntity);
    assertEquals(1, retrievedEntity.size());
    assertEquals(familyName, retrievedEntity.get(0).getPatient().getName().get(0).getFamily());
  }

}