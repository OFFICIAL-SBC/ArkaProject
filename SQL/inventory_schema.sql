CREATE SCHEMA IF NOT EXISTS inventory; -- I already crested this schema ✅
CREATE SCHEMA IF NOT EXISTS supply; -- I already crested this schema ✅


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
    ClientID INT REFERENCES Client(ClientID)
        ON UPDATE CASCADE
        ON DELETE SET NULL,
    provider_id INT REFERENCES supply.provider(provider_id)
        ON UPDATE CASCADE
        ON DELETE SET NULL,
    Ownership BOOLEAN NOT NULL,
    Name VARCHAR(255) NOT NULL,
    Created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE IF NOT EXISTS inventory.inventory(
	inventory_id BIGSERIAL PRIMARY KEY,
	product_id  SERIAL NOT NULL REFERENCES product.product(product_id),
	phone VARCHAR(30) NOT NULL,
	email_billing VARCHAR(30) NOT NULL,
	created_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);
-- ============================================================================
