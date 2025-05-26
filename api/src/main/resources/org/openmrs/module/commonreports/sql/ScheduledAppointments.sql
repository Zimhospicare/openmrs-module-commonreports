SELECT
    pn.given_name, pn.family_name,
       pv.name AS PROVIDER,
       pa.start_date_time AS START_TIME,
       pa.end_date_time as END_TIME,
       ast.name as SERVICE_TYPE,
       pa.status AS appointment_status,
       pa.comments AS comments,
       apv.response AS provider_response
FROM patient_appointment pa
    LEFT JOIN patient p ON pa.patient_id = p.patient_id
    lEFT JOIN person_name pn ON p.patient_id = pn.person_id
    LEFT JOIN provider pv ON pa.provider_id = pv.provider_id
    LEFT JOIN appointment_service_type ast ON pa.appointment_service_type_id = ast.appointment_service_type_id
    LEFT JOIN patient_appointment_provider apv ON apv.patient_appointment_id = pa.patient_appointment_id
WHERE
    pa.start_date_time >= :startDateTime