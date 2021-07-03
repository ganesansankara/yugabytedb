import pyodbc,sys
import db
""" 
if ( len(sys.argv) < 3 ) :
    print ("Usage:", sys.argv[0], ' <prefix> <maxrecordstobeinserted>')
    sys.exit(1)
    
prefix=sys.argv[1]
records_max=int(sys.argv[2])

print ('Arguments:', prefix, records_max) """

conn = db.dremio_connect()
cur = conn.cursor()



#records_max=100000000
try:
    cur.execute('SELECT COUNT(*) FROM "s2b"."vaaccounts"."vds_reports"')
    res = cur.fetchone()
    print(f'RecordsCount={res}')


except pyodbc.Error as e:
    print ( f'ERROROCCURED:{e.pgerror}')
    conn.rollback()
#all = cur.fetchall()
