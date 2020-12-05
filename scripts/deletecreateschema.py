#!/usr/bin/env python

import psycopg2
import db

conn = db.postgres_connect()
cur = conn.cursor()


try:
    print ("Dropping schema")
    cur.execute("DROP TABLE IF EXISTS accounts")
except psycopg2.Error as e:
    print ( "ERROROCCURED",e.pgerror)
    conn.rollback()

    
try:
    print ("Creating schema****")
    cur.execute("""
CREATE TABLE accounts (
    acct_no VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    balance NUMERIC(15,2) NOT NULL,
    delete_flag VARCHAR(1) NOT NULL,
    version_no NUMERIC(10) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
    )
    """)
except psycopg2.Error   as e:
    print ( "ERROROCCURED",e.pgerror)
    conn.rollback()

try:
    cur.execute("""
CREATE INDEX ACCOUNTS_ACCTNO ON accounts ( 
    acct_no, delete_flag, version_no
    )
    """)
except psycopg2.Error   as e:
    print ( "ERROROCCURED",e.pgerror)
    conn.rollback()

conn.commit()

print ("Successful creation of schema")