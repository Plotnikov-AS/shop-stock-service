create table if not exists products
(
    id    uuid primary key,
    type  text    not null,
    price numeric not null,
    count bigint default 0,
    place text    not null
);

comment on table products is 'Товары';
comment on column products.type is 'Тип товара';
comment on column products.price is 'Цена за единицу';
comment on column products.count is 'Количество товара';
comment on column products.place is 'Место хранения';