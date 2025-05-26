select cb.receipt_number                          as InvoiceNumber
     , CONCAT(pn.given_name, ' ', pn.family_name) AS patientName
     , SUM(cbli.price * quantity)                 as TotalAmount
     , cbp.BreakdownPerPaymentMethod
from cashier_bill cb
         LEFT JOIN openmrs.patient p on cb.patient_id = p.patient_id
         LEFT JOIN openmrs.person p2 on p.patient_id = p2.person_id
         LEFT JOIN openmrs.person_name pn on p2.person_id = pn.person_id
         left join openmrs.cashier_bill_line_item cbli on cb.bill_id = cbli.bill_id
         LEFT JOIN (select cbp2.bill_id,
                           GROUP_CONCAT(CONCAT(cpm.name, ':', cbp2.amount, ' Date: ', cpm.date_created) SEPARATOR
                                        ', ') AS BreakdownPerPaymentMethod
                    from cashier_bill_payment cbp2
                             LEFT JOIN openmrs.cashier_payment_mode cpm on cbp2.payment_mode_id = cpm.payment_mode_id
                    group by bill_id) cbp
                   on cb.bill_id = cbp.bill_id
where cb.bill_type = 'INVOICE'
group by cb.bill_id