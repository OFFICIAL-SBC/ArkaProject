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
    Created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.address 
DROP COLUMN Type;

-- =========================================================
-- ADDRESS
-- =========================================================
INSERT INTO public.address (city_id, country_id, address, lat, lon, type)
VALUES
  (1, 1, 'Cra 45 #10-23', 6.251840, -75.563591),
  (2, 1, 'Av. Caracas 123', 4.609710, -74.081750),
  (3, 2, 'Av. Reforma 500', 19.432608, -99.133209),
  (4, 2, 'Av. Vallarta 3000', 20.673789, -103.335006),
  (5, 3, 'Av. Amazonas 1200', -0.180653, -78.467834),
  (6, 3, 'Malecón 2000', -2.189412, -79.889067, ),
  (7, 4, 'Av. 9 de Julio 100', -34.603722, -58.381592),
  (8, 4, 'Calle San Martín 250', -31.420083, -64.188776),
  (9, 5, 'Av. Libertador 120', -33.448890, -70.669265),
  (10, 5, 'Calle Prat 55', -33.045847, -71.619674);

  INSERT INTO public.address (
    city_id,
    country_id,
    address,
    lat,
    lon
)
VALUES
(1, 1, '123 Main St, Downtown',      6.24420300,  -75.58121100),
(2, 1, '456 Oak Avenue, El Poblado', 6.20876300,  -75.56744800),
(3, 1, '789 Pine Road, Laureles',    6.25184000,  -75.59370000),
(4, 2, '321 Sunset Blvd',            40.71277600, -74.00597400),
(5, 2, '654 Ocean Drive',            25.76168100, -80.19178800);

SELECT * FROM public.address;
SELECT * FROM public.city;
