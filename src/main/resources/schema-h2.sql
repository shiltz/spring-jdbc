create schema vas;

CREATE TABLE vas.order_mine (
    id UUID NOT NULL,
    product_name VARCHAR(200)
);
--
--
-- CREATE TABLE vas.order_tbl (
--                                id INT NOT NULL,
--                                title VARCHAR(50) NOT NULL,
--                                author VARCHAR(20) NOT NULL,
--                                submission_date DATE
-- );