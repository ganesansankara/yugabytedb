#!/usr/bin/env python

from pyarrow import flight
import pyarrow as pa
import config

cfg = config.config()

class HttpDremioClientAuthHandler(flight.ClientAuthHandler):

    def __init__(self, username, password):
        super(flight.ClientAuthHandler, self).__init__()
        self.basic_auth = flight.BasicAuth(username, password)
        self.token = None

    def authenticate(self, outgoing, incoming):
        auth = self.basic_auth.serialize()
        outgoing.write(auth)
        self.token = incoming.read()

    def get_token(self):
        return self.token

host = cfg['ganesan-dremio']['host'] 
username = cfg['ganesan-dremio']['user']
password = cfg['ganesan-dremio']['password']
username ='dremio'
password = 'dremio123'

print (username,password)

sql = 'select count(*) from ganspace.pay.vdaccounts'
connstring='grpc+tcp://'+ host+ ':47470'
print (connstring)

client = flight.FlightClient(connstring)
client.authenticate(HttpDremioClientAuthHandler(username, password)) 
info = client.get_flight_info(flight.FlightDescriptor.for_command(sql))
reader = client.do_get(info.endpoints[0].ticket)
batches = []
while True:
    try:
        batch, metadata = reader.read_chunk()
        batches.append(batch)
    except StopIteration:
        break
data = pa.Table.from_batches(batches)
df = data.to_pandas()