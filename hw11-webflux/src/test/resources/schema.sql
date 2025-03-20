create table authors (
    id bigserial not null,
    full_name varchar(255) not null,
    primary key (id)
);

create table genres (
    id bigserial not null,
    name varchar(255) not null,
    primary key (id)
);

create table books (
    id bigserial not null,
    title varchar(255) not null,
    author_id bigint not null references authors (id),
    genre_id bigint references genres(id),
    primary key (id)
);

create table book_comments (
    id bigserial not null,
    book_id bigint not null references books(id),
    comment varchar(1000) not null,
    primary key (id)
);
