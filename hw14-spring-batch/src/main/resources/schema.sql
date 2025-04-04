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


/*
create table authorH2 (
    id bigserial,
    full_name varchar(255),
    primary key (id)
);

create table genreH2 (
    id bigserial,
    name varchar(255),
    primary key (id)
);

create table bookH2 (
    id bigserial,
    title varchar(255),
    author_id bigint references authors (id) on delete cascade,
    genre_id bigint references genres(id) on delete cascade,
    primary key (id)
);

create table bookCommentH2 (
    id bigserial,
    text varchar(255),
    book_id bigint references books (id) on delete cascade,
    primary key (id)
);*/
