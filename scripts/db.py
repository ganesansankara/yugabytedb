#import psycopg2
import config
import pyodbc

def postgres_connect():
    
    cfg = config.config()

    for section in cfg:
        print(section)
    print(cfg['ganesan-db'])

    #connstring=f'host={cfg["ganesan-db"]["host"]} port={cfg["ganesan-db"]["port"]} \
     #dbname={cfg["ganesan-db"]["dbname"]} \
     #user={cfg["ganesan-db"]["user"]}  \
     #password={cfg["ganesan-db"]["password"]}'
     #conn = psycopg2.connect(connstring)

    
    connstring = (
    "DRIVER={PostgreSQL Unicode};"
    f'DATABASE={cfg["ganesan-db"]["dbname"]};'
    f'UID={cfg["ganesan-db"]["user"]};'
    f'PWD={cfg["ganesan-db"]["password"]};'
    f'SERVER={cfg["ganesan-db"]["host"]};'
    f'PORT={cfg["ganesan-db"]["port"]};'
    )
    print(connstring)

    conn = pyodbc.connect(connstring)
    return conn

def dremio_connect():
    
    cfg = config.config()

    print(cfg['ganesan-dremio'])


    driver = "/opt/dremio-odbc/lib64/libdrillodbc_sb64.so"
    connstring = f'Driver={driver};ConnectionType=Direct;\
    HOST={cfg["ganesan-dremio"]["host"]};PORT={cfg["ganesan-dremio"]["odbcjdbcport"]};AuthenticationType=Plain;\
    UID={cfg["ganesan-dremio"]["user"]};PWD={cfg["ganesan-dremio"]["password"]}'

    print (connstring)

    conn=pyodbc.connect(connstring,autocommit=True)

    return conn