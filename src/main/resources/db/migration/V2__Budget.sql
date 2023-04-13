UPDATE budget
SET type='Расход'
FROM (SELECT * FROM budget WHERE type='Комиссия') AS subquery
WHERE budget.id = subquery.id;