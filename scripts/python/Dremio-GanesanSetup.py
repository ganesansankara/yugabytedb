#!/usr/bin/env python

import requests
from requests.exceptions import HTTPError
import json
import config

cfg = config.config()
myglobal = {
    'dremioServer': f"http://{cfg['ganesan-dremio']['host']}:{cfg['ganesan-dremio']['port']}",
    'logintokenstring' : ''
}

#utility function
def apiPost(dremioServer,endpoint, headers, body=None, method='POST'):
    url= f"{myglobal['dremioServer']}/{endpoint}"
    print (f"****Calling={url}")

    text=''

    try:
        if ( method == 'PUT' ):
            text = requests.put(url, headers=headers, data=json.dumps(body)).text
        elif ( method == 'GET' ):
            text = requests.get(url, headers=headers, data=json.dumps(body)).text
        else:
            text = requests.post(url, headers=headers, data=json.dumps(body)).text
    except HTTPError as http_err:
        print(f'HTTP error occurred: {http_err}') 
    except Exception as err:
        print(f'Other error occurred: {err}') 
    else:
        print('Success!')

    if (text):
        print (f'**TEXT={text}')
        #return json.loads(text)
        return (text)
    else:
        return None


# Add Dremio Defualt User
def addDremioUser():

    bodyParam = { 
        'userName': cfg['ganesan-dremio']['user'], 'password': cfg['ganesan-dremio']['password'],
        'firstName': 'Gan', 'laststName': 'San', 'email': 'banana@banana.com', 'createdAt': '1526186430755'
    }
    headers = {
    'Authorization': '_dremionull', 'Content-Type':  'application/json'
    }
    postResp = apiPost(myglobal['dremioServer'], 'apiv2/bootstrap/firstuser',headers=headers, body=bodyParam, method='PUT')

#Login to Dremio
def loginDremio():
    print ("Logging into Dremio")
    # Add Dremio Data Source
    bodyParam = { 
        "userName": cfg['ganesan-dremio']['user'],  "password": cfg['ganesan-dremio']['password']
    }
    headers = {
        'Content-Type':  'application/json'
    }
   

    postResp = apiPost(myglobal['dremioServer'], 'apiv2/login',headers=headers, body=bodyParam, method='POST')
    j=json.loads(postResp)

    logintoken = j['token']
    print (f'logintoken={logintoken}')
    return logintoken


# Add Dremio Data Source
def addDremioDataSource (config='ganesan-db'):
    print ("Adding Dremio DataSource")

    bodyParam = { 
        "entityType": "source","type": "POSTGRES",
        "name": cfg[config]['dbname'],
        "config": {
            "hostname": cfg[config]['host'], "port": cfg[config]['port'],
            "databaseName": cfg[config]['dbname'],
            "username": cfg[config]['user'], "password": cfg[config]['password'],
            "authenticationType": "MASTER"
        }
    }
    headers = {
    'Authorization': f'_dremio{myglobal["logintokenstring"]}', 'Content-Type':  'application/json'
    }

    print ( f'bodyParam={bodyParam}')

    print ( f'headers={headers}')
     
    postResp = apiPost(myglobal['dremioServer'], 'api/v3/catalog',headers=headers, body=bodyParam, method='POST')

# Add Dremio Data Source
def addDremioSpace (spacename):
    print ("Adding Dremio Space")

    bodyParam = { 
        "entityType": "space", "name": spacename
    }
    headers = {
        'Authorization': f'_dremio{myglobal["logintokenstring"]}', 'Content-Type':  'application/json'
    }

    print ( f'bodyParam={bodyParam}')

    print ( f'headers={headers}')
     
    postResp = apiPost(myglobal['dremioServer'], 'api/v3/catalog',headers=headers, body=bodyParam, method='POST')

# Add Dremio Data Source
def getDremioSpaceId (spacename):
    print ("Getting Dremio Space")

    bodyParam = { 
        "entityType": "space", "name": spacename
    }
    headers = {
        'Authorization': f'_dremio{myglobal["logintokenstring"]}', 'Content-Type':  'application/json'
    }

    print ( f'bodyParam={bodyParam}')

    print ( f'headers={headers}')
     
    postResp = apiPost(myglobal['dremioServer'], f'api/v3/catalog', headers=headers, body=bodyParam, method='GET')
    j=json.loads(postResp)

    #spaceid = j['data']['id']
    spaceid='43b8f6a3-9586-47f7-92bf-ef9d053bc14c'
    print (f'spaceid={spaceid}')
    return spaceid

# Add Dremio Data Source
def addDremioFolder (spacename,foldername):
    print ("Adding Dremio Folder")

    #spaceid=getDremioSpaceId(spacename='ganesanspace')
    path = [spacename, foldername]
    bodyParam= { 
        "entityType": "folder", 
        #"id" : spaceid,
        "path": path
    }


    headers = {
        'Authorization': f'_dremio{myglobal["logintokenstring"]}', 'Content-Type':  'application/json'
    }

    print ( f'bodyParam={bodyParam}')

    print ( f'headers={headers}')
    postResp = apiPost(myglobal['dremioServer'], 'api/v3/catalog',headers=headers, body=bodyParam, method='POST')

# Add Dremio Data Source
def addDremioVirtualDataSet (spacename,foldername,vdsname,sql):
    print ("Adding Dremio VirtualDataSet")

    #spaceid=getDremioSpaceId(spacename='ganesanspace')
    path = [spacename, foldername, vdsname]
    bodyParam= { 
        "entityType": "dataset", 
        "type": "VIRTUAL_DATASET",
        "sql": sql,
        #"id" : spaceid,
        "path": path
    }


    headers = {
        'Authorization': f'_dremio{myglobal["logintokenstring"]}', 'Content-Type':  'application/json'
    }

    print ( f'bodyParam={bodyParam}')

    print ( f'headers={headers}')

     
    postResp = apiPost(myglobal['dremioServer'], f'api/v3/catalog',headers=headers, body=bodyParam, method='POST')


#main
def main():

    addDremioUser()

    myglobal['logintokenstring']=loginDremio()

    addDremioDataSource()

    addDremioSpace(spacename='s2b')

    addDremioFolder(spacename='s2b', foldername='vaaccounts')

    addDremioVirtualDataSet(spacename='s2b', foldername='vaaccounts', vdsname="vds_reports", \
        sql=f'select * from "{cfg["ganesan-db"]["dbname"]}"."public"."accounts"')

    #addDremioFolder(spacename='s2b', foldername='payments')


#end

if __name__ == '__main__':
    main()