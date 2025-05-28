SELECT cbs.name as SERVICE,
       cbp.name as PaymentMethod,
       cip.price as NewPrice,cip.old_price AS OldPrice, CONCAT(pn.given_name, ' ', pn.family_name) AS ChangedBy,
       cip.date_changed as DateTimeofChange
FROM cashier_item_price cip
         left join openmrs.cashier_billable_service cbs on cip.service_id = cbs.service_id
     left join cashier_payment_mode cbp on cip.payment_mode= cbp.payment_mode_id
    left join openmrs.users u on cip.changed_by = u.user_id
         left join person ps on u.person_id = ps.person_id
         left join openmrs.person_name pn on ps.person_id = pn.person_id
WHERE cip.changed_by IS NOT NULL AND cip.old_price IS NOT NULL;