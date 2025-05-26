SELECT CONCAT(pn.given_name, ' ', pn.family_name)    AS patientName,
       quatoAmount,
       IF(cb.bill_type = 'INVOICE', 'true', 'false') as invoiceGenerated,
       cb.status
FROM cashier_bill cb
         LEFT JOIN openmrs.patient p on cb.patient_id = p.patient_id
         LEFT JOIN openmrs.person p2 on p.patient_id = p2.person_id
         LEFT JOIN openmrs.person_name pn on p2.person_id = pn.person_id
         LEFT JOIN (select SUM(price * quantity) as quatoAmount, bill_id
                    from cashier_bill_line_item
                    group by bill_id) cbli
                   on cb.bill_id = cbli.bill_id
where cb.date_created >= :fromDate
  AND cb.date_created <= :toDate