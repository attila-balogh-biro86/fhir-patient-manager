package nl.sdbgroep.patient.manager.model;


import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.hl7.fhir.r4.model.Patient;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import nl.sdbgroep.patient.manager.dao.FhirJsonConverter;

@Getter
@Setter
@Entity
@Table(name = "patients")
public class PatientEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String patientFhirId;
  @Column(nullable = false)
  private String patientName;

  @JdbcTypeCode(SqlTypes.JSON)
  @Convert(converter = FhirJsonConverter.class)
  @Column(columnDefinition = "jsonb")
  private Patient fhirPayload;
}