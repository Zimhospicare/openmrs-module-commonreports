SELECT p.name                       AS ClinicianName,
       DATE(e.encounter_datetime)   AS EncounterDate,
       COUNT(DISTINCT e.patient_id) AS NumberOfEncounters
FROM encounter_provider ep
         LEFT JOIN
     openmrs.encounter e ON ep.encounter_id = e.encounter_id
         LEFT JOIN
     provider p ON ep.provider_id = p.provider_id
where e.encounter_datetime >= :startDate
  and e.encounter_datetime < DATE_ADD(:startDate, INTERVAL 1 DAY)
GROUP BY p.name, DATE(e.encounter_datetime)
ORDER BY p.name, EncounterDate;