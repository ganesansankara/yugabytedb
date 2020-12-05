#!/usr/bin/env python

from dremio_client import init

query = 'select * from sys.options'
client = init(simple_client=True)
results = client.query(query)

print('eturned =rows' + results )

client = init() # initialise connectivity to Dremio via config file
catalog = client.data # fetch catalog
vds = catalog.space.vds.get() # fetch a specific dataset
df = vds.query() # query the first 1000 rows of the dataset and return as a DataFrame
pds = catalog.source.pds.get() # fetch a physical dataset
pds.metadata_refresh() # refresh metadata on that dataset


#CLI interface for integration with scripts

#dremio_client query --sql 'select * from sys.options'

#dremio_client refresh-metadata --table 'my.vds.name'
