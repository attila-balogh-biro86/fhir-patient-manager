package nl.sdbgroep.patient.manager.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class FhirJsonConverter
    implements AttributeConverter<org.hl7.fhir.r4.model.Resource,String> {

  private static final FhirContext CTX = FhirContext.forR4();
  private static final IParser JSON = CTX.newJsonParser();

  @Override
  public String convertToDatabaseColumn(org.hl7.fhir.r4.model.Resource resource) {
    if (resource == null) {
      return null;
    }
    // return compact JSON string
    return JSON.setPrettyPrint(false)
        .encodeResourceToString(resource);
  }

  @Override
  public org.hl7.fhir.r4.model.Resource convertToEntityAttribute(String dbData) {
    if (dbData == null || dbData.isBlank()) {
      return null;
    }
    return (org.hl7.fhir.r4.model.Resource) JSON.parseResource(dbData);
  }
}

