#
# THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESSED OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS
# FOR A PARTICULAR PURPOSE. THIS CODE AND INFORMATION ARE NOT SUPPORTED BY XEBIALABS.
#

import sys
from saucelabs.saucelabs_client_util import SauceLabsClientUtil

if saucelabsServer is None:
    print "No server provided."
    sys.exit(1)

saucelabs_client = SauceLabsClientUtil.create_saucelabs_client(buildId, testName, testNameWildCard, hourRange, 
    limit, saucelabsServer, acctUsername, accessKey)

jobs_output, jobs = saucelabs_client.get_jobs()

