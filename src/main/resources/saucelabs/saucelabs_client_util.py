#
# THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESSED OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS
# FOR A PARTICULAR PURPOSE. THIS CODE AND INFORMATION ARE NOT SUPPORTED BY XEBIALABS.
#

from saucelabs.saucelabs_client import SauceLabsClient


class SauceLabsClientUtil(object):

    @staticmethod
    def create_saucelabs_client(container, username, password):
        client = SauceLabsClient.create_client(container, username, password)
        return client