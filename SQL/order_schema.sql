CREATE SCHEMA IF NOT EXISTS orders; -- I already crested this schema ✅


GRANT USAGE ON SCHEMA orders TO app_owner; -- I already did this ✅
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA orders TO app_owner; -- I already did this ✅


-- 1. ENUM FOR ORDER STATE
CREATE TYPE order_state_enum AS ENUM (
    'PENDING',
    'PAID',
    'CANCELLED'
);

-- 2. ORDER STATE
CREATE TABLE orders.order_state (
    order_state_id BIGSERIAL PRIMARY KEY,
    state          order_state_enum NOT NULL DEFAULT 'PENDING',
    created_at     TIMESTAMPTZ        NOT NULL DEFAULT NOW(),
    updated_at     TIMESTAMPTZ        NOT NULL DEFAULT NOW()
);

SELECT column_name, data_type, is_nullable, column_default
FROM information_schema.columns
WHERE table_schema = 'orders'
  AND table_name   = 'order_state';

INSERT INTO orders.order_state (state)
VALUES 
    ('PENDING'),
    ('PAID'),
    ('CANCELLED');

SELECT * FROM orders.order_state;
	
-- 3. ORDER (main order header)

CREATE TABLE IF NOT EXISTS orders.orders(
    order_id       BIGSERIAL PRIMARY KEY,
    client_id      BIGINT      NOT NULL,
    user_id        BIGINT      NOT NULL,
    warehouse_id   BIGINT      NOT NULL,
    order_state_id BIGINT      NOT NULL,
    currency_id    BIGINT      NOT NULL,
    total          NUMERIC(8,2)  NOT NULL,
    taxes          NUMERIC(4,2)  NOT NULL,
    discount       NUMERIC(4,2)  NOT NULL,
    created_at     TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    updated_at     TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
	CONSTRAINT fk_orders_order_state
        FOREIGN KEY (order_state_id) REFERENCES orders.order_state(order_state_id),
    CONSTRAINT fk_orders_client
        FOREIGN KEY (client_id)    REFERENCES users.client(client_id),
    CONSTRAINT fk_orders_user
        FOREIGN KEY (user_id)      REFERENCES users.users(user_id),
    CONSTRAINT fk_orders_warehouse
        FOREIGN KEY (warehouse_id) REFERENCES inventory.warehouse(warehouse_id),
    CONSTRAINT fk_orders_currency
        FOREIGN KEY (currency_id)  REFERENCES public.currency(currency_id)
);

SELECT * FROM orders.orders;

-- 4. BILL
CREATE TABLE orders.bill (
    bill_id         BIGSERIAL PRIMARY KEY,
    order_id        BIGINT   NOT NULL,
    currency_id     BIGINT   NOT NULL,
    user_id         BIGINT   NOT NULL,
    reference_number VARCHAR(255) NOT NULL,
    value            NUMERIC(8,2) NOT NULL,
    url_pdf          VARCHAR(500),
    created_at       TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_bill_order
        FOREIGN KEY (order_id)   REFERENCES orders.orders (order_id),
    CONSTRAINT fk_bill_currency
        FOREIGN KEY (currency_id) REFERENCES public.currency (currency_id),
    CONSTRAINT fk_bill_user
        FOREIGN KEY (user_id)    REFERENCES users.users(user_id)
);

-- 5. ORDER DETAIL
CREATE TABLE orders.order_detail (
    order_detail_id BIGSERIAL PRIMARY KEY,
    order_id        BIGINT   NOT NULL,
    product_id      BIGINT   NOT NULL,
    amount          INTEGER  NOT NULL,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_order_detail_order
        FOREIGN KEY (order_id)   REFERENCES orders.orders(order_id),
    CONSTRAINT fk_order_detail_product
        FOREIGN KEY (product_id) REFERENCES product.product(product_id)
);


-- ======================================================================================
-- Queries
-- ======================================================================================

SELECT * FROM orders.orders;

SELECT * FROM orders.bill;

SELECT * FROM orders.order_detail;
