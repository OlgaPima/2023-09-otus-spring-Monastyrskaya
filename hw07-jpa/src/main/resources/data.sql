insert into authors(full_name)
values ('С. Кинг'),
       ('Ф. Купер'),
       ('Л.Н. Толстой');

insert into genres(name)
values ('Фантастика'),
       ('Приключения'),
       ('Классика');

insert into books(title, author_id, genre_id)
values ('Лангольеры', 1, 1),
       ('Зверобой', 2, 2),
       ('Война и мир', 3, 3);

insert into book_comments(book_id, comment)
values (1, 'Страшновато как-то'),
       (2, 'Книжка супер!'),
       (3, 'Школьная литература за 10й класс');
