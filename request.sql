CREATE DATABASE company_repository;

CREATE SCHEMA company_storage;

DROP SCHEMA company_storage;

CREATE TABLE company_storage.company
(
    id   INT PRIMARY KEY,
    name varchar(128) UNIQUE NOT NULL,
    date DATE                NOT NULL CHECK ( date > '1995-01-01' AND date < '2020-01-01')
);

-- DROP TABLE company_storage.company;
-- DROP TABLE public.company;

INSERT INTO company (id, name, date)
VALUES (1, 'Google', '2001-01-01'),
       (2, 'Apple', '2002-10-29'),
       (3, 'Facebook', '1998-09-13');

CREATE TABLE employee
(
    id         SERIAL PRIMARY KEY,
    first_name VARCHAR(128) NOT NULL,
    last_name  VARCHAR(128) NOT NULL,
    company_id INT REFERENCES company (id) ON DELETE CASCADE,
    salary     INT,
    UNIQUE (first_name, last_name)
--     FOREIGN KEY (company_id) REFERENCES company
);

DROP TABLE employee;

insert into employee(first_name, last_name, salary, company_id)
values ('Ivan', 'Sidorov', 500, 1),
       ('Ivan', 'Ivanov', 1000, 2),
       ('Arni', 'Paramonov', NULL, 2),
       ('Petr', 'Petrov', 2000, 3),
       ('Sveta', 'Svetislav', 1500, NULL);

SELECT DISTINCT id,
                first_name f_name,
                last_name  l_name,
                salary
FROM employee empl
WHERE salary <> 1000
ORDER BY first_name, salary;

SELECT DISTINCT id,
                first_name f_name,
                last_name  l_name,
                salary
FROM employee empl
WHERE first_name ILIKE 'pet%'
ORDER BY first_name, salary;

SELECT DISTINCT id,
                first_name f_name,
                last_name  l_name,
                salary
FROM employee empl
WHERE salary BETWEEN 1000 AND 1500
ORDER BY first_name, salary;

SELECT DISTINCT id,
                first_name f_name,
                last_name  l_name,
                salary
FROM employee empl
WHERE salary IN (1000, 1100, 2000)
   OR (first_name LIKE 'Iv%'
    AND last_name ILIKE '%ov%')
ORDER BY first_name, salary DESC;

SELECT sum(salary)
FROM employee empl;

-- sum, avg, max, min, count
SELECT count(*)
FROM employee empl;

SELECT lower(first_name),
       concat(first_name, ' ', last_name) fio,
       now()
FROM employee empl;

SELECT id, first_name
FROM employee
WHERE company_id IS NOT NULL
UNION
SELECT id, first_name
FROM employee
where salary IS NULL;

SELECT avg(empl.salary)
FROM (SELECT *
      FROM employee
      ORDER BY salary DESC
      LIMIT 2) empl;

SELECT *,
       (SELECT avg(salary) FROM employee)
FROM employee;

SELECT *,
       (SELECT max(salary) FROM employee) - employee.salary diff
FROM employee;

select *
from employee
where company_id in (select company.id from company);

DELETE
FROM employee
WHERE salary IS NULL;

DELETE
FROM employee
WHERE salary = (SELECT max(salary) FROM employee);

DELETE
FROM employee
where company_id = 1;
DELETE
FROM company
WHERE id = 1;

DELETE
FROM company
WHERE id = 2;

UPDATE employee
SET company_id = 1,
    salary     = 1700
WHERE id = 10 OR id = 9
RETURNING id, first_name || ' ' || employee.last_name fio;














