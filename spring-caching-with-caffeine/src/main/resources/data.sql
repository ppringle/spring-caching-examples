DROP TABLE IF EXISTS billionaires;

CREATE TABLE billionaires (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  first_name VARCHAR(250) NOT NULL,
  last_name VARCHAR(250) NOT NULL,
  career VARCHAR(250) DEFAULT NULL
);

INSERT INTO billionaires (first_name, last_name, career) VALUES
  ('Jeff', 'Bezos', 'Tech Entrepreneur'),
  ('Qin', 'Yinglin', 'Pig Breeder'),
  ('Mark ', 'Zuckerberg', 'Tech Entrepreneur'),
  ('Bernard ', 'Arnault', 'Fashion'),
  ('Jim ', 'Walton', 'Walmart'),
  ('Aliko', 'Dangote', 'Industrialist'),
  ('Bill', 'Gates', 'Tech Entrepreneur'),
  ('Folrunsho', 'Alakija', 'Oil Magnate');