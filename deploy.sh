#!/usr/bin/env bash

cd web
ncftpput -R -u $WEDDING_USERNAME -p $WEDDING_PASSWORD $WEDDING_HOST "/" "."