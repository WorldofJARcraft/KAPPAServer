#!/usr/bin/env bash
docker run --name kappa_db --restart=always -p 3306:3306 --mount type=bind,src=/etc/my.cnf,dst=src=/etc/my.cnf --mount type=bind,src=/var/lib/mysql,dst=/var/lib/mysql kappa