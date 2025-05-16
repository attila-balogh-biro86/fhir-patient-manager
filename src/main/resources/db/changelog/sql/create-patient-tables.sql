CREATE TABLE Patient (
                         id IDENTITY PRIMARY KEY,
                         active BOOLEAN,
                         gender VARCHAR(20),
                         birth_date DATE,
                         deceased_boolean BOOLEAN,
                         deceased_date_time TIMESTAMP,
                         marital_status_code VARCHAR(50),
                         marital_status_display VARCHAR(100),
                         marital_status_system VARCHAR(200),
                         multiple_birth_boolean BOOLEAN,
                         multiple_birth_integer INT,
                         managing_organization_id INT,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE PatientIdentifier (
                                   id IDENTITY PRIMARY KEY,
                                   patient_id INT,
                                   use VARCHAR(20),
                                   type_code VARCHAR(50),
                                   type_display VARCHAR(100),
                                   type_system VARCHAR(200),
                                   system VARCHAR(200),
                                   "value" VARCHAR(100),
                                   period_start TIMESTAMP,
                                   period_end TIMESTAMP,
                                   assigner_display VARCHAR(255),
                                   FOREIGN KEY (patient_id) REFERENCES Patient(id)
);

CREATE TABLE PatientName (
                             id IDENTITY PRIMARY KEY,
                             patient_id INT,
                             use VARCHAR(20),
                             text VARCHAR(255),
                             family VARCHAR(100),
                             given VARCHAR(100),
                             prefix VARCHAR(100),
                             suffix VARCHAR(100),
                             period_start TIMESTAMP,
                             period_end TIMESTAMP,
                             FOREIGN KEY (patient_id) REFERENCES Patient(id)
);

CREATE TABLE PatientTelecom (
                                id IDENTITY PRIMARY KEY,
                                patient_id INT,
                                system VARCHAR(20),
                                "value" VARCHAR(100),
                                use VARCHAR(20),
                                rank INT,
                                period_start TIMESTAMP,
                                period_end TIMESTAMP,
                                FOREIGN KEY (patient_id) REFERENCES Patient(id)
);

CREATE TABLE PatientAddress (
                                id IDENTITY PRIMARY KEY,
                                patient_id INT,
                                use VARCHAR(20),
                                type VARCHAR(20),
                                text VARCHAR(255),
                                line1 VARCHAR(255),
                                line2 VARCHAR(255),
                                city VARCHAR(100),
                                district VARCHAR(100),
                                state VARCHAR(100),
                                postal_code VARCHAR(20),
                                country VARCHAR(100),
                                period_start TIMESTAMP,
                                period_end TIMESTAMP,
                                FOREIGN KEY (patient_id) REFERENCES Patient(id)
);

CREATE TABLE PatientPhoto (
                              id IDENTITY PRIMARY KEY,
                              patient_id INT,
                              content_type VARCHAR(50),
                              language VARCHAR(50),
                              data BLOB,
                              url VARCHAR(500),
                              size INT,
                              hash BLOB,
                              title VARCHAR(100),
                              creation TIMESTAMP,
                              FOREIGN KEY (patient_id) REFERENCES Patient(id)
);

CREATE TABLE PatientContact (
                                id IDENTITY PRIMARY KEY,
                                patient_id INT,
                                name_prefix VARCHAR(50),
                                name_given VARCHAR(100),
                                name_family VARCHAR(100),
                                gender VARCHAR(10),
                                address_id INT,
                                organization_id INT,
                                period_start TIMESTAMP,
                                period_end TIMESTAMP,
                                FOREIGN KEY (patient_id) REFERENCES Patient(id)
);

CREATE TABLE PatientContactRelationship (
                                            id IDENTITY PRIMARY KEY,
                                            contact_id INT,
                                            code VARCHAR(50),
                                            display VARCHAR(100),
                                            system VARCHAR(200),
                                            FOREIGN KEY (contact_id) REFERENCES PatientContact(id)
);

CREATE TABLE PatientContactTelecom (
                                       id IDENTITY PRIMARY KEY,
                                       contact_id INT,
                                       system VARCHAR(20),
                                       "value" VARCHAR(100),
                                       use VARCHAR(20),
                                       rank INT,
                                       period_start TIMESTAMP,
                                       period_end TIMESTAMP,
                                       FOREIGN KEY (contact_id) REFERENCES PatientContact(id)
);

CREATE TABLE PatientCommunication (
                                      id IDENTITY PRIMARY KEY,
                                      patient_id INT,
                                      language_code VARCHAR(50) NOT NULL,
                                      language_display VARCHAR(100),
                                      language_system VARCHAR(200),
                                      preferred BOOLEAN,
                                      FOREIGN KEY (patient_id) REFERENCES Patient(id)
);

CREATE TABLE PatientGeneralPractitioner (
                                            id IDENTITY PRIMARY KEY,
                                            patient_id INT,
                                            practitioner_type VARCHAR(50),
                                            practitioner_reference_id INT,
                                            FOREIGN KEY (patient_id) REFERENCES Patient(id)
);

CREATE TABLE PatientLink (
                             id IDENTITY PRIMARY KEY,
                             patient_id INT,
                             other_type VARCHAR(50),
                             other_reference_id INT,
                             type VARCHAR(20) NOT NULL,
                             FOREIGN KEY (patient_id) REFERENCES Patient(id)
);