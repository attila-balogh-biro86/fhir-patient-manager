package nl.sdbgroep.patient.manager.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import nl.sdbgroep.patient.manager.model.OrganizationEntity;

@Repository
public interface OrganizationRepository extends CrudRepository<OrganizationEntity, Long> {

  OrganizationEntity findByOrganizationFhirId(@Param("organizationFhirId") String organizationFhirId);
}
