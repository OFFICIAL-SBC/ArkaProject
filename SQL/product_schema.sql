CREATE ROLE app_owner LOGIN PASSWORD 'Geco1964Cata18';

CREATE SCHEMA IF NOT EXISTS product AUTHORIZATION app_owner;

GRANT USAGE ON SCHEMA product TO app_owner;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA product TO app_owner;

-- ==============================
-- 1. Category
-- ==============================
CREATE TABLE product.category (
    category_id SERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP DEFAULT now(),
    updated_at  TIMESTAMP DEFAULT now()
);

-- ==============================
-- 2. Brand
-- ==============================
CREATE TABLE product.brand (
    brand_id    SERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP DEFAULT now(),
    updated_at  TIMESTAMP DEFAULT now()
);

-- ==============================
-- 3. Product
-- ==============================
CREATE TABLE product.product (
    product_id  SERIAL PRIMARY KEY,
    brand_id    INT NOT NULL,
    category_id INT NOT NULL,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    price       NUMERIC(10,2) NOT NULL,
    discount    NUMERIC(6,2) DEFAULT 0,
    created_at  TIMESTAMP DEFAULT now(),
    updated_at  TIMESTAMP DEFAULT now(),
    
    -- Foreign keys
    CONSTRAINT fk_product_brand
        FOREIGN KEY (brand_id) REFERENCES product.brand (brand_id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    
    CONSTRAINT fk_product_category
        FOREIGN KEY (category_id) REFERENCES product.category (category_id)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

-- ==============================
-- 4. Images
-- ==============================
CREATE TABLE product.images (
    image_id    SERIAL PRIMARY KEY,
    product_id  INT NOT NULL,
    url         VARCHAR(500) NOT NULL,
    created_at  TIMESTAMP DEFAULT now(),
    updated_at  TIMESTAMP DEFAULT now(),

    -- Foreign key
    CONSTRAINT fk_images_product
        FOREIGN KEY (product_id) REFERENCES product.product (product_id)
        ON UPDATE CASCADE ON DELETE CASCADE
);



-- =========================================
-- 1. Categorías
-- =========================================
INSERT INTO product.category (name) VALUES
('Keyboards'),
('Mice'),
('Monitors'),
('Headsets'),
('Gaming Chairs');

SELECT * FROM product.product;

DELETE FROM product.product WHERE product_id = 8;

TRUNCATE TABLE product.category RESTART IDENTITY CASCADE;
SELECT * FROM product.category;
DELETE FROM product.category;

-- =========================================
-- 2. Marcas
-- =========================================
INSERT INTO product.brand (name) VALUES
('Logitech'),
('Razer'),
('Corsair'),
('SteelSeries'),
('MSI');

SELECT * FROM product.brand;
SELECT * FROM product.category;
SELECT * FROM product.product;

-- =========================================
-- 3. Products
-- =========================================

SELECT * FROM product.product;

-- Keyboards
INSERT INTO product.product (brand_id, category_id, name, description, price, discount)
VALUES
(1, 1, 'Logitech G915 TKL', 'Wireless mechanical keyboard with Lightsync RGB', 229.99, 10.00),
(3, 1, 'Corsair K70 RGB MK.2', 'Mechanical keyboard with Cherry MX switches and RGB lighting', 159.99, 5.00);

-- Mice
INSERT INTO product.product (brand_id, category_id, name, description, price, discount)
VALUES
(2, 2, 'Razer DeathAdder V2', 'Ergonomic mouse with 20K DPI optical sensor', 69.99, 0.00),
(4, 2, 'SteelSeries Rival 600', 'Mouse with dual sensor system and adjustable weights', 79.99, 5.00);

-- Monitors
INSERT INTO product.product (brand_id, category_id, name, description, price, discount)
VALUES
(5, 3, 'MSI Optix MAG274QRF-QD', '27” QHD gaming monitor, 165Hz, Quantum Dot technology', 449.99, 15.00);

-- Headsets
INSERT INTO product.product (brand_id, category_id, name, description, price, discount)
VALUES
(2, 4, 'Razer BlackShark V2', 'Gaming headset with surround sound and detachable mic', 99.99, 10.00);

-- Gaming Chairs
INSERT INTO product.product (brand_id, category_id, name, description, price, discount)
VALUES
(3, 5, 'Corsair T3 Rush', 'Ergonomic gaming chair with adjustable lumbar support', 269.99, 0.00);

