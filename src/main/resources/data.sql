insert into customer (id, name, surname, balance) values
    (1, 'John', 'Doe', 100),
    (2, 'Andy', 'Smith', 200),
    (3, 'Mark', 'Miller', 300);

insert into account (id, customer_id) values
    (100, 1),
    (200, 2),
    (300, 1),
    (400, 1);

insert into transactions (id, account_id, transactions) values
    (8, 100, 10),
    (9, 100, 20),
    (10, 200, 100),
    (11, 300, 50);
