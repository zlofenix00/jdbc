CREATE TABLE contact
(
    id     SERIAL PRIMARY KEY,
    number VARCHAR(128) NOT NULL,
    type   VARCHAR(128)
);

CREATE TABLE employee_contact
(
    employee_id INT REFERENCES employee (id) ON DELETE CASCADE,
    contact_id  INT REFERENCES contact (id) ON DELETE CASCADE
);

insert into contact (number, type)
values ('234-56-78', 'домашний'),
       ('987-65-43', 'рабочий'),
       ('565-25-91', 'мобильный'),
       ('332-55-67', NULL),
       ('465-11-22', NULL);

insert into employee_contact (employee_id, contact_id)
values ((SELECT id FROM employee WHERE first_name = 'Ivan'), (SELECT id FROM contact WHERE number = '234-56-78')),
       ((SELECT id FROM employee WHERE first_name = 'Ivan'), (SELECT id FROM contact WHERE number = '987-65-43')),
       ((SELECT id FROM employee WHERE first_name = 'Sveta'), (SELECT id FROM contact WHERE number = '565-25-91')),
       ((SELECT id FROM employee WHERE first_name = 'Petr'), (SELECT id FROM contact WHERE number = '332-55-67')),
       ((SELECT id FROM employee WHERE first_name = 'Petr'), (SELECT id FROM contact WHERE number = '465-11-22'));

-- DROP TABLE contact;

SELECT c.name,
       employee.first_name || ' ' || employee.last_name fio,
       ec.contact_id,
       c2.number
FROM employee
         JOIN company c
              ON c.id = employee.company_id
         JOIN employee_contact ec
              ON employee.id = ec.employee_id
         JOIN contact c2
              ON c2.id = ec.contact_id;

SELECT *
FROM company
         CROSS JOIN (SELECT count(*) FROM employee) t;

SELECT c.name,
       e.first_name
FROM company c
         LEFT JOIN employee e
                   ON c.id = e.company_id;

SELECT c.name,
       e.first_name
FROM employee e
         RIGHT JOIN company c
                    ON c.id = e.company_id;

SELECT c.name,
       e.first_name
FROM employee e
         FULL JOIN company c
                   ON c.id = e.company_id;

--------------------------

select company.name
--        e.first_name
--        count(e.id)
from company
         left join company_storage.employee e on company.id = e.company_id
-- where company.name ILIKE 'goo%'
GROUP BY company.id
HAVING count(e.id) > 0;

--------------------------

select company.name,
       e.last_name,
       count(e.id) OVER (),
       max(e.salary) OVER (partition by company.name),
       avg(e.salary) OVER (partition by company.name),
       row_number() over (partition by company.name),
       dense_rank() over (partition by company.name ORDER BY salary nulls first ),
       e.salary
from company
         left join company_storage.employee e
                   on company.id = e.company_id
order by company.name;

select company.name,
       e.last_name,
       lag(e.salary) over (ORDER BY e.salary) - e.salary,
       e.salary
from company
         left join company_storage.employee e
                   on company.id = e.company_id
order by company.name;

--------------------------

CREATE VIEW employee_view AS
select company.name,
       e.last_name,
       e.salary,
       max(e.salary) over (PARTITION BY company.name),
       min(e.salary) over (PARTITION BY company.name),
       lag(e.salary) over (ORDER BY e.salary) - e.salary
from company
         left join company_storage.employee e
                   on company.id = e.company_id
order by company.name;

select *
from employee_view
where name ilike 'goo%';

CREATE MATERIALIZED VIEW materialized_view AS
select company.name,
       e.last_name,
       e.salary,
       max(e.salary) over (PARTITION BY company.name),
       min(e.salary) over (PARTITION BY company.name),
       lag(e.salary) over (ORDER BY e.salary) - e.salary
from company
         left join company_storage.employee e
                   on company.id = e.company_id
order by company.name;

SELECT *
FROM materialized_view
WHERE materialized_view.max = 2000;

REFRESH MATERIALIZED VIEW materialized_view;

SELECT *
FROM materialized_view;

----------------------------------

ALTER TABLE IF EXISTS employee
ADD COLUMN gender INT;

UPDATE employee
SET gender = 0
WHERE id > 10;

ALTER TABLE employee
ALTER COLUMN gender SET NOT NULL ;




