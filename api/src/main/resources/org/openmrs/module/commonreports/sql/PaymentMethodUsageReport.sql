select cpm.name as PaymentMethod,
       sum(cbp.amount)                                        as TotalAmount,
       ROUND(cbp.amount * 100.0 / SUM(cbp.amount) OVER (), 2) AS percentage_of_total,
       count(*)                                               as TransactionsCount
from cashier_bill_payment cbp
         left join openmrs.cashier_payment_mode cpm on cbp.payment_mode_id = cpm.payment_mode_id
where cbp.date_created >= :fromDate
  AND cbp.date_created <= :toDate
group by cbp.payment_mode_id;
