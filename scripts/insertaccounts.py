#!/usr/bin/env python

import psycopg2,sys
import db
import utils

def commit_recs(conn,cur,start_time):
    conn.commit()
    utils.elpased_time(start_time)

    cur.execute('SELECT COUNT(*) FROM accounts')
    res = cur.fetchone()
    print (prefix, f'TotalRecords inserted={res}')

    start_time=utils.start_time()
    return start_time

def insert_recs():
    conn = db.postgres_connect()
    cur = conn.cursor()

    ins_stmt = "INSERT INTO accounts ( acct_no, name, balance,version_no,delete_flag,created_at,updated_at) VALUES(?,?,?,?,?, CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)"
    acct_fmt=prefix+"acct-{:03d}"
    acct=''
    name=''
    balance=0.00

    start_time=utils.start_time()
    #records_max=100000000
    try:
        for idx in range(1,records_max+1,1):

            if ( acct == '' or ((idx % 1000) == 0) ) :
                
                start_time=commit_recs(conn,cur,start_time)
                
                acct=acct_fmt.format(idx)
                name = f'AcctName-{acct}'
                balance=10000.99+(idx*10);
                print (prefix, acct,name,balance)
            
            cur.execute (ins_stmt, (acct,name, balance,1,'N'))
            
        start_time=commit_recs(conn,cur,start_time)

    
    except psycopg2.Error as e:
        print ( f'ERROROCCURED:{e.pgerror}')
        conn.rollback()
#all = cur.fetchall()

if ( len(sys.argv) < 3 ) :
    print ("Usage:", sys.argv[0], ' <prefix> <maxrecordstobeinserted>')
    sys.exit(1)
    
prefix=sys.argv[1]
records_max=int(sys.argv[2])

print ('Arguments:', prefix, records_max)
insert_recs ()