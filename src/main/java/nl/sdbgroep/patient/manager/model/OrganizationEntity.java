package nl.sdbgroep.patient.manager.model;

import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.hl7.fhir.r4.model.Organization;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import nl.sdbgroep.patient.manager.dao.FhirJsonConverter;

@Getter
@Setter
@Entity
@Table(name = "organizations")
public class OrganizationEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String organizationFhirId;
  @Column(nullable = false)
  private String organizationName;

  @JdbcTypeCode(SqlTypes.JSON)
  @Convert(converter = FhirJsonConverter.class)
  @Column(columnDefinition = "jsonb")
  private Organization organization;

}
