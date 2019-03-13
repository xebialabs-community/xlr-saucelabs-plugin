# Copyright (c) 2019 XebiaLabs

# This software is released under the MIT License.
# https://opensource.org/licenses/MIT

import sys
from saucelabs.saucelabs_client_util import SauceLabsClientUtil

if saucelabsServer is None:
    print "No server provided."
    sys.exit(1)

saucelabs_client = SauceLabsClientUtil.create_saucelabs_client(buildId, testName, testNameWildCard, hourRange, 
    limit, saucelabsServer, acctUsername, accessKey)

jobs_output, jobs = saucelabs_client.get_jobs()

