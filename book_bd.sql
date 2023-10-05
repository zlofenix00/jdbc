CREATE DATABASE book_reposirory;

CREATE TABLE author
(
    id         SERIAL PRIMARY KEY,
    first_name VARCHAR(128) NOT NULL,
    last_name  VARCHAR(128) NOT NULL
);

CREATE TABLE book
(
    id        BIGSERIAL PRIMARY KEY,
    name      VARCHAR(128) NOT NULL,
    year      SMALLINT     NOT NULL,
    page      SMALLINT     NOT NULL,
    author_id INT REFERENCES author (id)
);

INSERT INTO author(first_name, last_name)
values ('Кей', 'Хорстманн'),
       ('Стивен', 'Кови'),
       ('Тони', 'Роббинс'),
       ('Наполеон', 'Хилл'),
       ('Роберт', 'Кийосаки'),
       ('Дейл', 'Карнеги');

insert into book (name, year, page, author_id)
values ('Java. Библиотека профессионала. Том 1', 2010, 1102, (SELECT id FROM author WHERE last_name = 'Хорстманн')),
       ('Java. Библиотека профессионала. Том 2', 2012, 954, (SELECT id FROM author WHERE last_name = 'Хорстманн')),
       ('Java SE 8. Вводный курс', 2015, 203, (SELECT id FROM author WHERE last_name = 'Хорстманн')),
       ('7 навыков высокоэффективных людей', 1989, 396, (SELECT id FROM author WHERE last_name = 'Кови')),
       ('Разбуди в себе исполина', 1991, 576, (SELECT id FROM author WHERE last_name = 'Роббинс')),
       ('Думай и богатей', 1937, 336, (SELECT id FROM author WHERE last_name = 'Хилл')),
       ('Богатый папа, бедный папа', 1997, 352, (SELECT id FROM author WHERE last_name = 'Кийосаки')),
       ('Квадрант денежного потока', 1998, 368, (SELECT id FROM author WHERE last_name = 'Кийосаки')),
       ('Как перестать беспокоиться и начать жить', 1948, 368, (SELECT id FROM author WHERE last_name = 'Карнеги')),
       ('Как завоевать друзей и оказывать влияние на людей', 1936, 352,
        (SELECT id FROM author WHERE last_name = 'Карнеги'));


-- Написать запрос, выбирающий: название книги, год и имя автора,
-- отсортировать по году издания книги в возрастающем порядке.
-- Написать тот запрос, но для убывающего порядка.

SELECT b.name, b.year, (SELECT a.first_name FROM author as a WHERE a.id = b.author_id)
FROM book b
ORDER BY b.year;

SELECT b.name, b.year, (SELECT a.first_name FROM author as a WHERE a.id = b.author_id)
FROM book b
ORDER BY b.year DESC;

-- Написать запрос, выбирающий количество книг у заданного автора.

SELECT count(*)
FROM book
WHERE author_id IN (SELECT id FROM author WHERE first_name = 'Кей');

-- Написать запрос, выбирающий книги,
-- у которых количество страниц больше среднего количества страниц по всем книгам

SELECT *
FROM book
WHERE page > (SELECT avg(page)
              FROM book);

-- Написать запрос, выбирающий 5 самых старых книг
-- Дополнить запрос и посчитать суммарное количество страниц среди этих книг

SELECT sum(page)
FROM (SELECT *
      FROM book
      ORDER BY year
      LIMIT 5) as "b*";

-- Написать запрос, изменяющий количество страниц у одной из книг

UPDATE book
SET page = page + 5
WHERE id = 2
RETURNING *;

-- Написать запрос, удаляющий автора, который написал самую большую книгу

DELETE
FROM book
WHERE author_id = (SELECT author_id
            FROM book
            WHERE page = (SELECT max(page)
                          FROM book));

DELETE
FROM author
WHERE id = 1
RETURNING *;

SELECT author_id
FROM book
WHERE page = (SELECT max(page)
              FROM book);



