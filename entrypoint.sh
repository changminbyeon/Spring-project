#!/bin/bash

chown -R devuser:devgroup /app

exec gosu devuser "$@"