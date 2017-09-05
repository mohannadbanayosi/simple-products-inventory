# --- !Ups

create table "products" (
  "id" SERIAL PRIMARY KEY,
  "name" varchar not null,
  "brand" varchar not null,
  "image_url" varchar not null,
  "price" real not null
);

# --- !Downs

-- drop table "products" if exists;
