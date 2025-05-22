package nl.sdbgroep.patient.manager.api;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import nl.sdbgroep.patient.manager.dao.OrganizationRepository;
import nl.sdbgroep.patient.manager.model.OrganizationEntity;

@Slf4j
@Component
public class OrganizationResourceProvider implements IResourceProvider {

  private final OrganizationRepository organizationRepository;

  @Autowired
  public OrganizationResourceProvider(OrganizationRepository organizationRepository) {
    this.organizationRepository = organizationRepository;
  }

  @Override
  public Class<Organization> getResourceType() {
    return Organization.class;
  }

  @Read
  public Organization getOrganizationById(@IdParam IdType id) {
    log.debug("Organization queried by id {}", id);
    OrganizationEntity organizationEntity = organizationRepository.findByOrganizationFhirId(id.getIdPart());
    if(organizationEntity == null) {
      throw new ResourceNotFoundException(id);
    }
    return organizationEntity.getOrganization();
  }

  @Update
  public MethodOutcome update(@ResourceParam Organization organization) {
    log.debug("Organization updated {}", organization.getId());
    OrganizationEntity organizationEntity = organizationRepository.findByOrganizationFhirId(organization.getIdPart());
    organizationEntity.setOrganization(organization);
    organizationRepository.save(organizationEntity);
    return new MethodOutcome(new IdType(organization.getResourceType().name(), organization.getId()), false);
  }

  @Create
  public MethodOutcome createOrganization(@ResourceParam  Organization organization) {
    log.debug("Organization created {}", organization);
    String newId = UUID.randomUUID().toString().replace("-", "");
    organization.setId(new IdType(organization.getResourceType().name(), newId));
    OrganizationEntity organizationEntity = new OrganizationEntity();
    organizationEntity.setOrganization(organization);
    organizationEntity.setOrganizationFhirId(organization.getIdPart());
    if(!organization.getName().isEmpty()) {
      organizationEntity.setOrganizationName(organization.getName());
    }
    organizationRepository.save(organizationEntity);
    return new MethodOutcome(new IdType(organization.getResourceType().name(), organization.getId()), true);
  }

  @Search
  public IBundleProvider searchAllOrganization() {
    log.debug("Retrieving all organizations");
    List<Organization> organizationList = new ArrayList<Organization>();
    List<OrganizationEntity> organizationEntities = StreamSupport
        .stream(organizationRepository.findAll().spliterator(), false)
        .toList();
    organizationEntities.forEach(organizationEntity -> organizationList.add(organizationEntity.getOrganization()));
    return new SimpleBundleProvider(organizationList);
  }
}

