CREATE SCHEMA IF NOT EXISTS inventory; -- I already crested this schema ✅
CREATE SCHEMA IF NOT EXISTS supply; -- I already crested this schema ✅

-- =================================================================================
-- Granting schema access to the app_owner 
-- =================================================================================
GRANT USAGE ON SCHEMA inventory TO app_owner; -- I already did this ✅
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA inventory TO app_owner; -- I already did this ✅

GRANT USAGE ON SCHEMA supply TO app_owner; -- I already did this ✅
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA supply TO app_owner; -- I already did this ✅

GRANT USAGE ON SCHEMA public TO app_owner; -- I already did this ✅
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO app_owner; -- I already did this ✅


-- =================================================================================
-- Granting secuence access to the app_owner 
-- =================================================================================

SELECT pg_get_serial_sequence('inventory.movement', 'movement_id'); -- I already did this ✅

GRANT USAGE ON SEQUENCE inventory.movement_movement_id_seq TO app_owner; -- I already did this ✅
GRANT SELECT, UPDATE ON SEQUENCE inventory.movement_movement_id_seq TO app_owner; -- I already did this ✅

-- =============================================================================
-- Public Schema
-- =============================================================================
CREATE TABLE public.country (
    country_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    Created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ==========================
-- Table: City
-- ==========================
CREATE TABLE public.city (
    city_id SERIAL PRIMARY KEY,
    country_id INT NOT NULL REFERENCES public.country(country_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    name VARCHAR(255),
    Created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE IF NOT EXISTS public.address (
    address_id  SERIAL PRIMARY KEY,
    city_id     SERIAL NOT NULL REFERENCES public.city(city_id),      
	country_id   SERIAL NOT NULL REFERENCES public.country(country_id),
	address VARCHAR(255) NOT NULL,
	LAT NUMERIC(10,8),
    LON NUMERIC(10,8),
    Type VARCHAR(50) CHECK (Type IN ('HOME', 'WORK', 'OTHER')),
    Created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.address
  ALTER COLUMN lat TYPE NUMERIC(9,6) USING ROUND(lat::numeric, 6),
  ALTER COLUMN lon TYPE NUMERIC(9,6) USING ROUND(lon::numeric, 6);


-- =============================================================================
-- =============================================================================
-- Supply Schema
-- =============================================================================
CREATE TABLE supply.provider (
    provider_id SERIAL PRIMARY KEY,
    Name VARCHAR(255) NOT NULL,
    NIT VARCHAR(100) UNIQUE NOT NULL,
    Phone VARCHAR(50),
    Email VARCHAR(255),
    Created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
-- =============================================================================
-- =============================================================================
-- Inventory Schema
-- =============================================================================

CREATE TABLE inventory.warehouse (
    warehouse_id SERIAL PRIMARY KEY,
    address_id INT NOT NULL REFERENCES public.address(address_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    client_id INT REFERENCES users.client(client_id)
        ON UPDATE CASCADE
        ON DELETE SET NULL,
    provider_id INT REFERENCES supply.provider(provider_id)
        ON UPDATE CASCADE
        ON DELETE SET NULL,
    ownership BOOLEAN NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE inventory.warehouse
ADD CONSTRAINT chk_ownership_rules
CHECK (
    -- Rule 1: If ownership = TRUE, both client_id and provider_id must be NULL
    (ownership = TRUE  AND client_id IS NULL AND provider_id IS NULL)
    OR
    -- Rule 2: If ownership = FALSE, exactly one of client_id or provider_id must be NOT NULL
    (ownership = FALSE AND (
        (client_id IS NULL AND provider_id IS NOT NULL)
        OR
        (client_id IS NOT NULL AND provider_id IS NULL)
    ))
);


CREATE TABLE IF NOT EXISTS inventory.inventory (
    inventory_id BIGSERIAL PRIMARY KEY,
    product_id  SERIAL NOT NULL REFERENCES product.product(product_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    warehouse_id INT NOT NULL REFERENCES inventory.warehouse(warehouse_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    availableStock INTEGER DEFAULT 0,
    thresholdStock INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_product_warehouse UNIQUE (product_id, warehouse_id)
);

-- =============================================================================
-- Types
-- =============================================================================

CREATE TYPE inventory_movement AS ENUM ('ADD', 'SUBTRACT');
CREATE TYPE inventory_movement_source AS ENUM ('CART', 'PROVIDER', 'RETURNED');

CREATE TABLE IF NOT EXISTS inventory.movement(
	movement_id BIGSERIAL PRIMARY KEY,
	inventory_id BIGSERIAL NOT NULL REFERENCES inventory.inventory(inventory_id)
		ON UPDATE CASCADE
		ON DELETE RESTRICT,
	movement_type VARCHAR(50) NOT NULL DEFAULT 'SUBTRACT',
	reference_type VARCHAR(50) NOT NULL DEFAULT 'CART',
	quantity INT NOT NULL DEFAULT 0,
	created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
	updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE inventory.movement
    ALTER COLUMN created_at TYPE TIMESTAMPTZ USING created_at AT TIME ZONE 'UTC',
    ALTER COLUMN updated_at TYPE TIMESTAMPTZ USING updated_at AT TIME ZONE 'UTC';


SELECT* FROM inventory.movement;

-- ============================================================================

UPDATE inventory.inventory
SET availableStock = 25
WHERE inventory_id = 2;


-- ============================================================================
-- Filling the tables with dummy data
-- ============================================================================
-- ==============================
-- 1. COUNTRY
-- ==============================
INSERT INTO public.country (name)
VALUES 
  ('Colombia'),
  ('Mexico'),
  ('Ecuador'),
  ('Argentina'),
  ('Chile');

  SELECT * FROM public.country;	

  -- ==============================
-- 2. CITY
-- ==============================
INSERT INTO public.city (country_id, name)
VALUES
  (1, 'Medellín'),
  (1, 'Bogotá'),
  (2, 'Ciudad de México'),
  (2, 'Guadalajara'),
  (3, 'Quito'),
  (3, 'Guayaquil'),
  (4, 'Buenos Aires'),
  (4, 'Córdoba'),
  (5, 'Santiago'),
  (5, 'Valparaíso');

DELETE FROM public.city;
ALTER SEQUENCE public.city_city_id_seq RESTART WITH 1;

 SELECT * FROM public.city;	

-- =========================================================
-- 3️⃣ ADDRESS
-- =========================================================
INSERT INTO public.address (city_id, country_id, address, lat, lon, type)
VALUES
  (1, 1, 'Cra 45 #10-23', 6.251840, -75.563591, 'HOME'),
  (2, 1, 'Av. Caracas 123', 4.609710, -74.081750, 'WORK'),
  (3, 2, 'Av. Reforma 500', 19.432608, -99.133209, 'WORK'),
  (4, 2, 'Av. Vallarta 3000', 20.673789, -103.335006, 'HOME'),
  (5, 3, 'Av. Amazonas 1200', -0.180653, -78.467834, 'HOME'),
  (6, 3, 'Malecón 2000', -2.189412, -79.889067, 'WORK'),
  (7, 4, 'Av. 9 de Julio 100', -34.603722, -58.381592, 'OTHER'),
  (8, 4, 'Calle San Martín 250', -31.420083, -64.188776, 'WORK'),
  (9, 5, 'Av. Libertador 120', -33.448890, -70.669265, 'HOME'),
  (10, 5, 'Calle Prat 55', -33.045847, -71.619674, 'OTHER');

  -- =========================================================
-- 4. PROVIDER
-- =========================================================
INSERT INTO supply.provider (name, nit, phone, email)
VALUES
  ('Proveedor Andino', '900123456-1', '+57 3011234567', 'andino@supply.com'),
  ('Distribuidora Azteca', '800987654-2', '+52 5512345678', 'azteca@supply.com'),
  ('Comercial del Pacífico', '700345678-3', '+593 987654321', 'pacifico@supply.com'),
  ('Argentine Trading', '600567890-4', '+54 91122334455', 'argtrade@supply.com'),
  ('Chilean Logistics', '500678901-5', '+56 987123456', 'chilelog@supply.com');

  -- =========================================================
-- 5. WAREHOUSE
-- =========================================================
INSERT INTO inventory.warehouse (address_id, client_id, provider_id, ownership, name)
VALUES
  (1, NULL, NULL, TRUE,  'Warehouse Medellín'),     -- ✅ Valid
  (2, NULL, 1, FALSE, 'Warehouse Bogotá'),          -- ✅ Valid
  (3, NULL, NULL, TRUE,  'Warehouse CDMX'),         -- ✅ Valid
  (5, NULL, 3, FALSE,  'Warehouse Quito'),          -- ✅ Valid
  (7, 2, NULL, FALSE, 'Warehouse Buenos Aires'),    -- ✅ Valid
  (9, NULL, NULL, TRUE, 'Warehouse Santiago');      -- ✅ Valid

 SELECT * FROM inventory.warehouse;

 SELECT pg_get_serial_sequence('inventory.warehouse', 'warehouse_id');

DELETE FROM inventory.warehouse;
ALTER SEQUENCE inventory.warehouse_warehouse_id_seq RESTART WITH 1;

-- =========================================================
-- 6. INVENTORY
-- =========================================================
INSERT INTO inventory.inventory (product_id, warehouse_id, availableStock, thresholdStock)
VALUES
  (1, 1, 200, 50),
  (2, 1, 150, 30),
  (3, 3, 120, 25),
  (4, 3, 80,  10),
  (5, 6, 60,  15),
  (6, 6, 300, 40);

 SELECT pg_get_serial_sequence('inventory.inventory', 'inventory_id');

 SELECT * FROM inventory.inventory;
 SELECT * FROM inventory.inventory WHERE availablestock < thresholdstock;

DELETE FROM inventory.inventory;
ALTER SEQUENCE inventory.inventory_inventory_id_seq RESTART WITH 1;


-- =============================================================================
-- Queries
-- =============================================================================


-- inventory.inventory
 SELECT * FROM inventory.inventory;
 SELECT * FROM inventory.inventory WHERE availablestock < thresholdstock;



 -- inventory.warehouse
 SELECT * FROM inventory.warehouse;