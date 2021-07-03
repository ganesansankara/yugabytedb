#!/bin/sh

#(cd ../docker; sh killcontainer.sh && sh startcontainer.sh )

python3 deletecreateschema.py
sh insertaccounts.sh init1 1000
python3 Dremio-GanesanSetup.py
python3 Dremio-GanesanTestODBC.py
