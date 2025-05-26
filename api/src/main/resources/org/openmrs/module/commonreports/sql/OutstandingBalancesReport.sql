SELECT patientName,
       sub.TotalBilled,
       sub.AmountPaid,
       sub.TotalBilled - sub.AmountPaid AS BalanceDue
FROM (SELECT SUM(cbli.price * cbli.quantity)            AS TotalBilled,
             SUM(cbp.amount)                            AS AmountPaid,
             CONCAT(pn.given_name, ' ', pn.family_name) AS PatientName
      FROM cashier_bill cb
               LEFT JOIN openmrs.cashier_bill_line_item cbli ON cb.bill_id = cbli.bill_id
               LEFT JOIN openmrs.cashier_bill_payment cbp ON cb.bill_id = cbp.bill_id
               LEFT JOIN openmrs.patient p on cb.patient_id = p.patient_id
               LEFT JOIN person ps on p.patient_id = ps.person_id
               left join openmrs.person_name pn on ps.person_id = pn.person_id
      GROUP BY cb.patient_id) sub;