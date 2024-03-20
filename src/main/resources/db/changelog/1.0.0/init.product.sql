create table if not exists products
(
    id    uuid primary key,
    type  uuid    not null references product_type on delete restrict,
    price numeric not null,
    count bigint default 0,
    place text    not null,
    unique (type, price)
);

comment on table products is 'Товары';
comment on column products.type is 'Тип товара';
comment on column products.price is 'Цена за единицу';
comment on column products.count is 'Количество товара';
comment on column products.place is 'Место хранения';