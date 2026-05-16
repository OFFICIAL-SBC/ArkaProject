-- =============================
-- users shcema
-- =============================

CREATE TYPE user_role AS ENUM ('ADMIN', 'CUSTOMER', 'EMPLOYEE');

CREATE TABLE IF NOT EXISTS users.client(
	client_id BIGSERIAL PRIMARY KEY,
	name VARCHAR(30) NOT NULL,
	nit VARCHAR(30) NOT NULL,
	phone VARCHAR(30) NOT NULL,
	email_billing VARCHAR(30) NOT NULL,
	created_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS users.users (
    user_id       BIGSERIAL PRIMARY KEY,
    client_id     BIGINT REFERENCES users.client(client_id) ON DELETE CASCADE, -- optional FK not shown
	address_id    BIGINT REFERENCES public.address(address_id),
    email         VARCHAR(255) NOT NULL UNIQUE,
    full_name     VARCHAR(255) NOT NULL,
    password_hash TEXT        NOT NULL,            -- store hash, not plaintext
    role_user    user_role NOT NULL DEFAULT 'CUSTOMER',            
    phone         VARCHAR(30),
    created_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- DROP TABLE users.users CASCADE;

SELECT * FROM users.client;

INSERT INTO users.client (name, nit, phone, email_billing)
VALUES 
('TECHCOL','900123456-7', '+57-3011111111', 'billing@acme.com'),
('TECHZIL','901987654-3', '+57-3022222222', 'billing@globex.com');


INSERT INTO users.users (
    client_id,
    address_id,
    email,
    full_name,
    password_hash,
    role_user,
    phone
)
VALUES
(1, 1,  'user1@example.com',  'User One',   'hashed_password_1',  'CUSTOMER', '3000000001'),
(1, 2,  'user2@example.com',  'User Two',   'hashed_password_2',  'CUSTOMER', '3000000002'),
(1, 3,  'user3@example.com',  'User Three', 'hashed_password_3',  'CUSTOMER', '3000000003'),
(1, 4,  'user4@example.com',  'User Four',  'hashed_password_4',  'CUSTOMER', '3000000004'),
(1, 5,  'user5@example.com',  'User Five',  'hashed_password_5',  'CUSTOMER', '3000000005'),
(1, 6,  'user6@example.com',  'User Six',   'hashed_password_6',  'CUSTOMER', '3000000006'),
(1, 7,  'user7@example.com',  'User Seven',  'hashed_password_7', 'CUSTOMER',           '3000000007'),
(2, 8,  'user8@example.com',  'User Eight',   'hashed_password_8',  'CUSTOMER', '3000000008'),
(2, 9,  'user9@example.com',  'User Nine',    'hashed_password_9',  'CUSTOMER', '3000000009'),
(2, 10, 'user10@example.com', 'User Ten',     'hashed_password_10', 'CUSTOMER', '3000000010'),
(2, 11, 'user11@example.com', 'User Eleven',  'hashed_password_11', 'CUSTOMER', '3000000011'),
(2, 12, 'user12@example.com', 'User Twelve',  'hashed_password_12', 'CUSTOMER', '3000000012'),
(2, 13, 'user13@example.com', 'User Thirteen','hashed_password_13', 'CUSTOMER', '3000000013'),
(2, 14, 'user14@example.com', 'User Fourteen','hashed_password_14', 'CUSTOMER', '3000000014'),
(2, 15, 'user15@example.com', 'User Fifteen', 'hashed_password_15', 'CUSTOMER', '3000000015');

SELECT * FROM users.users;

SELECT * FROM public.address;