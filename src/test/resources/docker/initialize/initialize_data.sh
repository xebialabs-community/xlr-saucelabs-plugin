#!/bin/sh

# Copyright (c) 2019 XebiaLabs

# This software is released under the MIT License.
# https://opensource.org/licenses/MIT

SCRIPT=$(readlink -f "$0")
# Absolute path this script is in, thus /home/user/bin
SCRIPTPATH=$(dirname "$SCRIPT")

wget --http-user=admin --http-password=admin --auth-no-challenge \
     --header="Accept: application/json" \
     --header="Content-type: application/json" \
     --post-file=$SCRIPTPATH/data/server-configs.json \
    http://localhost:5516/api/v1/config -O /dev/null

####################### XLR server data - template release-template-All.json
echo "Load Template-All"

wget --http-user=admin --http-password=admin --auth-no-challenge \
     --header="Accept: application/json" \
     --header="Content-type: application/json" \
     --post-file=$SCRIPTPATH/data/release-template-All.json \
    http://localhost:5516/api/v1/templates/import -O /dev/null

####################### XLR server data - template  release-template-TimeAndSize.json
echo "Load Template-TimeAndSize"

wget --http-user=admin --http-password=admin --auth-no-challenge \
     --header="Accept: application/json" \
     --header="Content-type: application/json" \
     --post-file=$SCRIPTPATH/data/release-template-TimeAndSize.json \
    http://localhost:5516/api/v1/templates/import -O /dev/null

####################### XLR server data - template  release-template-BadCredentials.json
echo "Load Template-BadCredentials"

wget --http-user=admin --http-password=admin --auth-no-challenge \
     --header="Accept: application/json" \
     --header="Content-type: application/json" \
     --post-file=$SCRIPTPATH/data/release-template-BadCredentials.json \
    http://localhost:5516/api/v1/templates/import -O /dev/null

####################### XLR server data - template  release-template-BuildId.json
echo "Load Template-BuildId"

wget --http-user=admin --http-password=admin --auth-no-challenge \
     --header="Accept: application/json" \
     --header="Content-type: application/json" \
     --post-file=$SCRIPTPATH/data/release-template-BuildId.json \
    http://localhost:5516/api/v1/templates/import -O /dev/null

####################### XLR server data - template  release-template-ExactName.json
echo "Load Template-ExactName"

wget --http-user=admin --http-password=admin --auth-no-challenge \
     --header="Accept: application/json" \
     --header="Content-type: application/json" \
     --post-file=$SCRIPTPATH/data/release-template-ExactName.json \
    http://localhost:5516/api/v1/templates/import -O /dev/null

####################### XLR server data - template  release-template-WildCarcName.json
echo "Load Template-WildCardName"

wget --http-user=admin --http-password=admin --auth-no-challenge \
     --header="Accept: application/json" \
     --header="Content-type: application/json" \
     --post-file=$SCRIPTPATH/data/release-template-WildCardName.json \
    http://localhost:5516/api/v1/templates/import -O /dev/null

####################### XLR server data - template  release-template-TimeRestrict.json
echo "Load Template-TimeRestrict"

wget --http-user=admin --http-password=admin --auth-no-challenge \
     --header="Accept: application/json" \
     --header="Content-type: application/json" \
     --post-file=$SCRIPTPATH/data/release-template-TimeRestrict.json \
    http://localhost:5516/api/v1/templates/import -O /dev/null

####################### XLR server data - template  release-template-SizeRestrict.json
echo "Load Template-SizeRestrict"

wget --http-user=admin --http-password=admin --auth-no-challenge \
     --header="Accept: application/json" \
     --header="Content-type: application/json" \
     --post-file=$SCRIPTPATH/data/release-template-SizeRestrict.json \
    http://localhost:5516/api/v1/templates/import -O /dev/null

####################### XLR server data - template  release-template-SizeAndWildCard.json
echo "Load Template-SizeAndWildCard"

wget --http-user=admin --http-password=admin --auth-no-challenge \
     --header="Accept: application/json" \
     --header="Content-type: application/json" \
     --post-file=$SCRIPTPATH/data/release-template-SizeAndWildCard.json \
    http://localhost:5516/api/v1/templates/import -O /dev/null
