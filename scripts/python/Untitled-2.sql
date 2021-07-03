SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;



SET default_tablespace = '';

SET default_with_oids = false;


---
--- drop tables
---

CREATE SCHEMA GANESAN;
---SET default_schema = 'GANESAN';

DROP TABLE IF EXISTS accounts;

CREATE TABLE accounts (
    acct_no VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    balance DEC(15,2) NOT NULL
    --- PRIMARY KEY(id)
);

SELECT * FROM public.accounts;