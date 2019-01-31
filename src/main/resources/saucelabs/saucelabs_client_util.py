#
# THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESSED OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS
# FOR A PARTICULAR PURPOSE. THIS CODE AND INFORMATION ARE NOT SUPPORTED BY XEBIALABS.
#

from saucelabs.saucelabs_client import SauceLabsClient


class SauceLabsClientUtil(object):

    @staticmethod
    def create_saucelabs_client(buildId, testName, testNameWildCard, hourRange, limit, container, 
        acctUsername, accessKey):
        client = SauceLabsClient.create_client(buildId, testName, testNameWildCard, hourRange, limit, container, 
            acctUsername, accessKey)
        return client