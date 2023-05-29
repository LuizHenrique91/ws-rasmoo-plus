ALTER TABLE user_payment_info RENAME COLUMN instalments TO installments;
ALTER TABLE user_payment_info MODIFY COLUMN installments INT;