create table if not exists product(
	id integer primary key auto_increment,
	name varchar(100),
	amount integer,
	updated_at timestamp
);