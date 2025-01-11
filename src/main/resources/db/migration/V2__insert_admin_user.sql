CREATE TABLE if not exists users (
                       id BIGSERIAL PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       first_name VARCHAR(255) NOT NULL,
                       last_name VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(255) NOT NULL,
                       usable_votes INT NOT NULL
);

insert into users(email, first_name, last_name, password, role,usable_votes)
values ('admin', 'admin', 'admin', '$2a$10$7j.OlDMLqDJcXGiHiKQ9neQjGxhw8buCNeSaRdb1LnI.ph4F37U0C','ADMIN', 1);