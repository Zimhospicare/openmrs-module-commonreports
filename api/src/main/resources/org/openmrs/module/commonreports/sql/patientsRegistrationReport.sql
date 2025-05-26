SELECT name.given_name        AS GivenName,
       name.middle_name       AS MiddleName,
       name.family_name       AS family_name,
       identifier_.identifier AS identifier,
       ps.gender              AS Gender,
       ps.birthdate           AS DateOfBirth,
       address.city_village   AS city,
       address.address1       AS address1,
       address.address2       AS address2,
       address.state_province AS Provice,
       patient.date_created   as RegistrationDate

FROM patient
         LEFT JOIN person ps ON patient.patient_id = ps.person_id
         LEFT JOIN person_name name ON ps.person_id = name.person_id
         LEFT JOIN person_address address ON ps.person_id = address.person_id
         LEFT JOIN patient_identifier identifier_ ON patient.patient_id = identifier_.patient_id
         LEFT JOIN encounter on patient.patient_id = encounter.patient_id
         JOIN encounter_type et ON encounter.encounter_type = et.encounter_type_id
         JOIN location l ON encounter.location_id = l.location_id
WHERE et.uuid = 'de1f9d67-b73e-4e1b-90d0-036166fc6995'
  AND encounter.encounter_datetime >= :startDate
  AND encounter.encounter_datetime < DATE_ADD(:endDate, INTERVAL 1 DAY)
  AND ps.gender = :gender
  AND TIMESTAMPDIFF(YEAR, ps.birthdate, CURDATE()) BETWEEN :minAge AND :maxAge
  AND patient.voided = 0
ORDER BY encounter.encounter_datetime DESC;
