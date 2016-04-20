#
# THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESSED OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS
# FOR A PARTICULAR PURPOSE. THIS CODE AND INFORMATION ARE NOT SUPPORTED BY XEBIALABS.
#

import sys, time, ast, re
import com.xhaus.jyson.JysonCodec as json
from xlrelease.HttpRequest import HttpRequest

class SauceLabsClient(object):
    def __init__(self, http_connection, username=None, password=None):
        self.http_request = HttpRequest(http_connection, username, password)

    @staticmethod
    def create_client(http_connection, username=None, password=None):
        return SauceLabsClient(http_connection, username, password)


    def get_jobs(self,):
        saucelabs_api_url = "/rest/v1/%s/jobs?full=true" % self.http_request.params.getUsername()
        jobs_response = self.http_request.get(saucelabs_api_url, contentType='application/json')
        if not jobs_response.isSuccessful():
            raise Exception("Failed to get jobs. Server return [%s], with content [%s] when calling [%s]" % (jobs_response.status, jobs_response.response, saucelabs_api_url))
        data = json.loads(jobs_response.getResponse())
        jobs = {}
        for job in data:
            jobs[job["id"]] = "Running %s on %s" % (job["name"], job["os"])
        return jobs
