-- =============================
-- SCHEMAS
-- =============================
CREATE SCHEMA IF NOT EXISTS users;
CREATE SCHEMA IF NOT EXISTS cart;

-- =============================
-- ACCESS
-- =============================

GRANT USAGE ON SCHEMA public TO app_owner;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO app_owner;

-- =============================
-- ENUMS
-- =============================
CREATE TYPE user_role AS ENUM ('ADMIN', 'CUSTOMER', 'EMPLOYEE');
CREATE TYPE cart_state_enum AS ENUM ('OPEN', 'INACTIVE', 'ABANDONED', 'CANCELLED','CONVERTED');
CREATE TYPE currency_enum AS ENUM ('USD', 'ARS', 'COP', 'BRL','CLP');


-- =============================
-- users shcema
-- =============================

CREATE TABLE IF NOT EXISTS users.client(
	client_id BIGSERIAL PRIMARY KEY,
	nit VARCHAR(30) NOT NULL,
	phone VARCHAR(30) NOT NULL,
	email_billing VARCHAR(30) NOT NULL,
	created_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS users.users (
    user_id       BIGSERIAL PRIMARY KEY,
    client_id     BIGINT REFERENCES users.client(client_id) ON DELETE CASCADE, -- optional FK not shown
    email         VARCHAR(255) NOT NULL UNIQUE,
    full_name     VARCHAR(255) NOT NULL,
    password_hash TEXT        NOT NULL,            -- store hash, not plaintext
    role_user    user_role NOT NULL DEFAULT 'CUSTOMER',            
    phone         VARCHAR(30),
    created_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);




-- =============================
-- public.currency  (shared lookup)
-- =============================
CREATE TABLE IF NOT EXISTS public.currency (
    currency_id  SERIAL PRIMARY KEY,
    currency     currency_enum NOT NULL DEFAULT 'COP',      -- e.g., 'USD','EUR','COP'
    created_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at   TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- =============================
-- cart.cart_state  (cart-specific lookup)
-- =============================
CREATE TABLE IF NOT EXISTS cart.cart_state (
    cart_state_id SERIAL PRIMARY KEY,
    cart_state    cart_state_enum  NOT NULL DEFAULT 'OPEN',     
    created_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- =============================
-- cart.cart  (belongs to a user, has state & currency)
-- =============================
CREATE TABLE IF NOT EXISTS cart.cart (
    cart_id       BIGSERIAL PRIMARY KEY,
    user_id       BIGINT   NOT NULL
                 REFERENCES users.users(user_id) ON DELETE CASCADE,
    cart_state_id INTEGER  NOT NULL
                 REFERENCES cart.cart_state(cart_state_id),
    currency_id   INTEGER  NOT NULL
                 REFERENCES public.currency(currency_id),
    created_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);



-- =============================
-- cart.cart_detail (items inside a cart)
-- =============================
CREATE TABLE IF NOT EXISTS cart.cart_detail (
    cart_detail_id BIGSERIAL PRIMARY KEY,
    cart_id        BIGINT  NOT NULL
                  REFERENCES cart.cart(cart_id) ON DELETE CASCADE,
    product_id     BIGINT  NOT NULL
                  REFERENCES product.product(product_id),
    amount         INTEGER NOT NULL CHECK (amount > 0),
    created_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uq_cart_product UNIQUE (cart_id, product_id)  -- one row per product per cart
);

-- =============================================================================================================================================================
-- Iserting dummy data
-- =============================================================================================================================================================

-- Users

INSERT INTO users.client (nit, phone, email_billing)
VALUES 
('900123456-7', '+57-3011111111', 'billing@acme.com'),
('901987654-3', '+57-3022222222', 'billing@globex.com');


INSERT INTO users.users (client_id, email, full_name, password_hash, role_user, phone)
VALUES 
(1, 'admin@acme.com', 'Alice Admin', 'hashed_pwd_1', 'ADMIN', '+57-3011111111'),
(1, 'employee@acme.com', 'Eve Employee', 'hashed_pwd_2', 'EMPLOYEE', '+57-3011111122'),
(2, 'customer@globex.com', 'Charlie Customer', 'hashed_pwd_3', 'CUSTOMER', '+57-3022222222');

SELECT * FROM users.users;

-- Public

INSERT INTO public.currency (currency)
VALUES 
('USD'),
('ARS'),
('COP'),
('BRL'),
('CLP');

SELECT * FROM public.currency;

-- Cart

INSERT INTO cart.cart_state (cart_state)
VALUES 
('OPEN'),
('INACTIVE'),
('ABANDONED'),
('CANCELLED'),
('CONVERTED');

SELECT * FROM cart.cart_state;


-- Alice (ADMIN) → Open cart in COP
INSERT INTO cart.cart (user_id, cart_state_id, currency_id)
VALUES (1, 1, 3);

-- Eve (EMPLOYEE) → Converted cart in USD
INSERT INTO cart.cart (user_id, cart_state_id, currency_id)
VALUES (2, 5, 1);

-- Charlie (CUSTOMER) → Cancelled cart in CLP
INSERT INTO cart.cart (user_id, cart_state_id, currency_id)
VALUES (3, 4, 5);

SELECT * FROM cart.cart;


INSERT INTO cart.cart_detail (cart_id, product_id, amount)
VALUES 
(1, 1, 2),  -- 2 units of product 1
(1, 2, 1);  -- 1 unit of product 2

-- Eve’s cart (cart_id = 2)
INSERT INTO cart.cart_detail (cart_id, product_id, amount)
VALUES 
(2, 2, 5),  -- 5 units of product 2
(2, 3, 1);  -- 1 unit of product 3

-- Charlie’s cart (cart_id = 3)
INSERT INTO cart.cart_detail (cart_id, product_id, amount)
VALUES 
(3, 1, 1);  -- 1 unit of product 1


