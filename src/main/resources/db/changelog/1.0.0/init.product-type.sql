create table if not exists product_type
(
    id   uuid primary key,
    name text not null
);

comment on table product_type is 'Тип товара';
comment on column product_type.name is 'Наименование товара';