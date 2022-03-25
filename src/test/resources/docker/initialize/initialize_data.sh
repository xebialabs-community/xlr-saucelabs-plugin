#!/bin/sh
#
# Copyright 2022 XEBIALABS
#
# Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
#


SCRIPT=$(readlink -f "$0")
# Absolute path this script is in, thus /home/user/bin
SCRIPTPATH=$(dirname "$SCRIPT")

wget --http-user=admin --http-password=admin --auth-no-challenge \
     --header="Accept: application/json" \
     --header="Content-type: application/json" \
     --post-file=$SCRIPTPATH/data/server-configs.json \
    http://localhost:15516/api/v1/config -O /dev/null

####################### XLR server data - template release-template-All.json
echo "Load Template-All"

wget --http-user=admin --http-password=admin --auth-no-challenge \
     --header="Accept: application/json" \
     --header="Content-type: application/json" \
     --post-file=$SCRIPTPATH/data/release-template-All.json \
    http://localhost:15516/api/v1/templates/import -O /dev/null

####################### XLR server data - template  release-template-TimeAndSize.json
echo "Load Template-TimeAndSize"

wget --http-user=admin --http-password=admin --auth-no-challenge \
     --header="Accept: application/json" \
     --header="Content-type: application/json" \
     --post-file=$SCRIPTPATH/data/release-template-TimeAndSize.json \
    http://localhost:15516/api/v1/templates/import -O /dev/null

####################### XLR server data - template  release-template-BadCredentials.json
echo "Load Template-BadCredentials"

wget --http-user=admin --http-password=admin --auth-no-challenge \
     --header="Accept: application/json" \
     --header="Content-type: application/json" \
     --post-file=$SCRIPTPATH/data/release-template-BadCredentials.json \
    http://localhost:15516/api/v1/templates/import -O /dev/null

####################### XLR server data - template  release-template-BuildId.json
echo "Load Template-BuildId"

wget --http-user=admin --http-password=admin --auth-no-challenge \
     --header="Accept: application/json" \
     --header="Content-type: application/json" \
     --post-file=$SCRIPTPATH/data/release-template-BuildId.json \
    http://localhost:15516/api/v1/templates/import -O /dev/null

####################### XLR server data - template  release-template-ExactName.json
echo "Load Template-ExactName"

wget --http-user=admin --http-password=admin --auth-no-challenge \
     --header="Accept: application/json" \
     --header="Content-type: application/json" \
     --post-file=$SCRIPTPATH/data/release-template-ExactName.json \
    http://localhost:15516/api/v1/templates/import -O /dev/null

####################### XLR server data - template  release-template-WildCarcName.json
echo "Load Template-WildCardName"

wget --http-user=admin --http-password=admin --auth-no-challenge \
     --header="Accept: application/json" \
     --header="Content-type: application/json" \
     --post-file=$SCRIPTPATH/data/release-template-WildCardName.json \
    http://localhost:15516/api/v1/templates/import -O /dev/null

####################### XLR server data - template  release-template-TimeRestrict.json
echo "Load Template-TimeRestrict"

wget --http-user=admin --http-password=admin --auth-no-challenge \
     --header="Accept: application/json" \
     --header="Content-type: application/json" \
     --post-file=$SCRIPTPATH/data/release-template-TimeRestrict.json \
    http://localhost:15516/api/v1/templates/import -O /dev/null

####################### XLR server data - template  release-template-SizeRestrict.json
echo "Load Template-SizeRestrict"

wget --http-user=admin --http-password=admin --auth-no-challenge \
     --header="Accept: application/json" \
     --header="Content-type: application/json" \
     --post-file=$SCRIPTPATH/data/release-template-SizeRestrict.json \
    http://localhost:15516/api/v1/templates/import -O /dev/null

####################### XLR server data - template  release-template-SizeAndWildCard.json
echo "Load Template-SizeAndWildCard"

wget --http-user=admin --http-password=admin --auth-no-challenge \
     --header="Accept: application/json" \
     --header="Content-type: application/json" \
     --post-file=$SCRIPTPATH/data/release-template-SizeAndWildCard.json \
    http://localhost:15516/api/v1/templates/import -O /dev/null
