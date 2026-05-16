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

GRANT USAGE, SELECT, UPDATE ON SEQUENCE product.product_product_id_seq TO app_owner;

SHOW search_path;

-- =============================
-- ENUMS
-- =============================
CREATE TYPE cart_state_enum AS ENUM ('OPEN', 'INACTIVE', 'ABANDONED', 'CANCELLED','CONVERTED');
CREATE TYPE currency_enum AS ENUM ('USD', 'ARS', 'COP', 'BRL','CLP');

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

DELETE FROM cart.cart where cart_id > 6;


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


SELECT * FROM cart.cart_detail;
SELECT * FROM cart.cart;
SELECT * FROM cart.cart_state;

SELECT c.cart_id, c.amount, c.product_id, p.name FROM cart.cart_detail AS c INNER JOIN product.product AS p on c.product_id = p.product_id WHERE c.cart_id = 10;\

UPDATE cart.cart
SET cart_state_id = 1
WHERE cart_id=10;
