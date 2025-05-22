package nl.sdbgroep.patient.manager.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import nl.sdbgroep.patient.manager.model.PatientEntity;

@Repository
public interface PatientRepository extends CrudRepository<PatientEntity, Long> {

  @Query(value = """
        SELECT *
          FROM patients p
         WHERE p.fhir_payload->'identifier' @>
               jsonb_build_array(
                 jsonb_build_object(
                   'system', :system,
                   'value',  :value
                 )
               )
      """, nativeQuery = true)
  List<PatientEntity> findByIdentifierSystemAndValue(
      @Param("system") String system,
      @Param("value")  String value
  );

  @Query(value = """
        SELECT *
          FROM patients p
         WHERE jsonb_extract_path_text(
                 p.fhir_payload,
                 'name',   -- the JSON key for the array
                 '0',      -- index 0 (first element)
                 'family'  -- the field within that object
               ) = :family
      """,
      nativeQuery = true)
  List<PatientEntity> findByFamilyName(
      @Param("family") String family
  );
}
