insert into customer (id, name, surname, balance, initial_credit) values
    (1, 'John', 'Doe', 100, 0),
    (2, 'Andy', 'Smith', 200, 20),
    (3, 'Mark', 'Miller', 300, 10);

insert into account (id, customer_id) values
    (4, 1),
    (5, 2),
    (6, 1),
    (7, 1);

insert into transactions (id, account_id, transactions) values
    (8, 4, 10),
    (9, 4, 20),
    (10, 5, 100),
    (11, 6, 50);
