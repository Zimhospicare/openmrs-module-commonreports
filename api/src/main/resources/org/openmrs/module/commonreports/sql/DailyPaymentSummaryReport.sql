SELECT COUNT(*)    as NumberofTransactions,
       cpm.name    as PaymentMethod,
       sum(amount) as TotalAmount
from cashier_bill_payment cbp
         LEFT JOIN cashier_bill cb on cbp.bill_id = cb.bill_id
         LEFT JOIN openmrs.patient p on p.patient_id = cb.patient_id
         LEFT JOIN person ps on ps.person_id = p.patient_id
         left join openmrs.person_name pn on ps.person_id = pn.person_id
         LEFT JOIN openmrs.cashier_payment_mode cpm on cbp.payment_mode_id = cpm.payment_mode_id
where cbp.date_created >= :summaryDate
  and cbp.date_created < DATE_ADD(:summaryDate, INTERVAL 1 DAY)
group by DATE(cbp.date_created), cbp.payment_mode_id