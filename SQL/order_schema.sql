CREATE SCHEMA IF NOT EXISTS orders; -- I already crested this schema ✅


GRANT USAGE ON SCHEMA orders TO app_owner; -- I already did this ✅
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA orders TO app_owner; -- I already did this ✅
GRANT USAGE, SELECT, UPDATE ON SEQUENCE orders.orders_order_id_seq TO app_owner; 


-- 1. ENUM FOR ORDER STATE
CREATE TYPE order_state_enum AS ENUM (
    'PENDING',
    'PAID',
    'CANCELLED',
	'PROCESSING',
	'SHIPPED',
	'DELIVERED'
);

ALTER TYPE order_state_enum ADD VALUE 'PROCESSING';
ALTER TYPE order_state_enum ADD VALUE 'SHIPPED';
ALTER TYPE order_state_enum ADD VALUE 'DELIVERED';

DROP TYPE order_state_enum;

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

INSERT INTO orders.order_state (state)
VALUES 
    ('PROCESSING'),
    ('SHIPPED'),
    ('DELIVERED');

SELECT * FROM orders.order_state;
	
-- 3. ORDER (main order header)

CREATE TABLE IF NOT EXISTS orders.orders(
    order_id       BIGSERIAL PRIMARY KEY,
    client_id      BIGINT      NOT NULL,
    user_id        BIGINT      NOT NULL,
    order_state_id BIGINT      NOT NULL,
    currency_id    BIGINT      NOT NULL,
    total          NUMERIC(8,2)  NOT NULL,
    created_at     TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    updated_at     TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
	CONSTRAINT fk_orders_order_state
        FOREIGN KEY (order_state_id) REFERENCES orders.order_state(order_state_id),
    CONSTRAINT fk_orders_client
        FOREIGN KEY (client_id)    REFERENCES users.client(client_id),
    CONSTRAINT fk_orders_user
        FOREIGN KEY (user_id)      REFERENCES users.users(user_id),
    CONSTRAINT fk_orders_currency
        FOREIGN KEY (currency_id)  REFERENCES public.currency(currency_id)
);

-- 4. BILL
CREATE TABLE orders.bill (
    bill_id         BIGSERIAL PRIMARY KEY,
    order_id        BIGINT   NOT NULL,
    currency_id     BIGINT   NOT NULL,
    user_id         BIGINT   NOT NULL,
    reference_number VARCHAR(255) NOT NULL,
    total_value      NUMERIC(8,2) NOT NULL,
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
	shipment_id     BIGINT NOT NULL,
    product_id      BIGINT   NOT NULL,
    units           INTEGER  NOT NULL,
	unit_price      NUMERIC(10,2) NOT NULL,
	total_value     NUMERIC(10,2)  NOT NULL,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_order_detail_product
        FOREIGN KEY (product_id) REFERENCES product.product(product_id),
	-- composite FK guarantees the detail's shipment belongs to the same order
	CONSTRAINT fk_order_detail_shipment
        FOREIGN KEY (shipment_id, order_id)
        REFERENCES orders.shipment(shipment_id, order_id),
	CONSTRAINT chk_order_detail_units_pos CHECK (units > 0),
	CONSTRAINT chk_order_detail_price__nonneg CHECK (unit_price > 0)
);


-- 6. Shipment

CREATE TABLE IF NOT EXISTS orders.shipment(
	shipment_id		BIGSERIAL PRIMARY KEY,
	order_id        BIGINT    NOT NULL,
	warehouse_id    BIGINT    NOT NULL,
	shipment_status_id BIGINT NOT NULL,
	origin_name 	VARCHAR(120)  NOT NULL,
	origin_city 	VARCHAR(120)  NOT NULL,
	origin_country  CHAR(2)       NOT NULL,
	distance_km       NUMERIC(8,2),
	estimated_arrival TIMESTAMPTZ,
	dispached_at      TIMESTAMPTZ,      
	delivered_at 	  TIMESTAMPTZ,		
	created_at        TIMESTAMPTZ NOT NULL DEFAULT NOW(),
	updated_at		  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
	CONSTRAINT fk_shipment_order
		FOREIGN KEY (order_id) REFERENCES orders.orders(order_id),
	CONSTRAINT fk_shipment_warehouse
		FOREIGN KEY (warehouse_id) REFERENCES inventory.warehouse(warehouse_id),
	CONSTRAINT fk_shipment_status
		FOREIGN KEY (shipment_status_id) REFERENCES orders.shipment_status(shipment_status_id),
	CONSTRAINT uq_shipment_order_warehouse UNIQUE (order_id, warehouse_id),
    CONSTRAINT uq_shipment_id_order UNIQUE (shipment_id, order_id),
	CONSTRAINT chk_shipment_distance_nonneg
        CHECK (distance_km IS NULL OR distance_km >= 0)
);

CREATE TYPE shipment_state_enum AS ENUM (
	'PENDING',
    'PREPARING',
    'IN_TRANSIT',
    'DELIVERED',
	'PROCESSING',
	'CANCELLED'
);

CREATE TABLE IF NOT EXISTS orders.shipment_status (
	shipment_status_id   BIGSERIAL PRIMARY KEY,
	code 				 shipment_state_enum NOT NULL DEFAULT 'PENDING'
);


INSERT INTO orders.shipment_status (code)
values
('PENDING'),
('PREPARING'),
('IN_TRANSIT'),
('DELIVERED'),
('PROCESSING'),
('CANCELLED');

SELECT * FROM orders.shipment_status;


-- order.estimated_arrival = MAX(shipment.estimated_arrival)

-- ======================================================================================
-- Queries
-- ======================================================================================

SELECT * FROM orders.orders;

SELECT * FROM orders.bill;

SELECT * FROM orders.order_detail;

SELECT * FROM public.currency;

SELECT * FROM orders.shipment;


-- DELETE AND FROP DATA AND TABLES
DELETE FROM orders.orders;
ALTER SEQUENCE orders.orders_order_id_seq RESTART WITH 1;

DROP TABLE orders.order_detail;
DROP TABLE orders.shipment;
DROP TABLE orders.orders;
DROP TABLE orders.bill;