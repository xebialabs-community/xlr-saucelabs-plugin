# Copyright (c) 2019 XebiaLabs

# This software is released under the MIT License.
# https://opensource.org/licenses/MIT

from saucelabs.saucelabs_client import SauceLabsClient


class SauceLabsClientUtil(object):

    @staticmethod
    def create_saucelabs_client(buildId, testName, testNameWildCard, hourRange, limit, container, 
        acctUsername, accessKey):
        client = SauceLabsClient.create_client(buildId, testName, testNameWildCard, hourRange, limit, container, 
            acctUsername, accessKey)
        return client