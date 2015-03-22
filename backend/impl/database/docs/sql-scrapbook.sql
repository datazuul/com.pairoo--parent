select * from geolocation where country ilike 'United_States%';
select * from id_sequences;
select * from memberships;
select * from product;
select * from schema_version;
select * from useraccount;
select * from users;
select useraccount_id from memberships, useraccount where memberships.useraccount_id=useraccount.id;

update useraccount set membership=memberships.id from memberships where memberships.useraccount_id=useraccount.id;
update memberships set product=product.id from product where product.role='STANDARD';