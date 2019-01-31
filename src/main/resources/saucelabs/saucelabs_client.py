#
# THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESSED OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS
# FOR A PARTICULAR PURPOSE. THIS CODE AND INFORMATION ARE NOT SUPPORTED BY XEBIALABS.
#

import sys, time, ast, re
import com.xhaus.jyson.JysonCodec as json
from xlrelease.HttpRequest import HttpRequest
from datetime import datetime

class SauceLabsClient(object):
    def __init__(self, buildId, testName, testNameWildCard, hourRange, limit, http_connection, 
        acctUsername, accessKey):
        self.configUsername = acctUsername
        self.configAccessKey = accessKey
        self.http_connection = http_connection
        # If not username and accessKey not configured in the getJobs task, use the info from the
        #    SauceLabs server configuration
        if not self.configUsername:
            self.configUsername = http_connection.get("username")
        if not self.configAccessKey:
            self.configAccessKey = http_connection.get("password")
        self.http_request = HttpRequest(http_connection, self.configUsername, self.configAccessKey)
        self.acctUsername = acctUsername
        self.buildId = buildId
        self.testName = testName
        self.testNameWildCard = testNameWildCard
        self.beginRange = None
        self.endRange = None
        self.limit = limit
        if hourRange and hourRange > 0:
            self.endRange = str(int(time.time()))
            self.beginRange = str(int(time.time()) - (int(hourRange)*3600))
            

    @staticmethod
    def create_client(buildId, testName, testNameWildCard, hourRange, limit, http_connection, 
        acctUsername, accessKey):
        return SauceLabsClient(buildId, testName, testNameWildCard, hourRange, limit, http_connection, 
             acctUsername, accessKey)

    def filterList(self, data):
        filteredJobs = []
        for testJob in data:
            passedName = True
            passedBuild = True
            testJobName = testJob["name"] if testJob["name"] else ""
            testJobBuild = testJob["build"] if testJob['build'] else ""
            # Note: hour and limit are filtered in the intial http request
            if self.testName:
                if self.testNameWildCard:
                    if self.testName not in testJobName:
                        passedName = False
                else: 
                    if self.testName != testJobName:
                        passedName = False
            if self.buildId:
                if self.buildId != testJobBuild:
                    passedBuild = False 
            if passedName and passedBuild:
                filteredJobs.append(testJob)
        return filteredJobs

    def getAdditionalParams(self):
        theLimit = ""
        theHourRange = ""
        if self.limit:
            theLimit = "&limit="+str(self.limit)
        if self.beginRange and self.endRange:
            theHourRange = "&from="+self.beginRange+"&to="+self.endRange
        paramsString = theLimit+theHourRange
        return paramsString

    def get_jobs(self,):
        addParams = self.getAdditionalParams()
        saucelabs_api_url = "/rest/v1/%s/jobs?full=true%s" % (self.configUsername, addParams)

        jobs_response = self.http_request.get(saucelabs_api_url, contentType='application/json')
        if not jobs_response.isSuccessful():
            raise Exception("Failed to get jobs. Server return [%s], with content [%s] when calling [%s]" % (jobs_response.status, jobs_response.response, saucelabs_api_url))
        data = json.loads(jobs_response.getResponse())
        filteredJobs = self.filterList(data)
        jobs = {}
        nothingFound = "No jobs matching the search criteria were found. Search URL - "+saucelabs_api_url
        if len(filteredJobs) > 0:
            print 'Description | Start Time | Build Id | Name | Passed | Link'
            print ':---: | :---: | :---: | :---: | :---: | :---:'
        else:
            print nothingFound
            jobs["0"] = nothingFound
        counter = 0
        for job in filteredJobs: 
            counter += 1
            jobName = job["name"] if job["name"] else " "
            jobBuildId = job["build"] if job["build"] else " " 
            description = job["os"]+"/"+job["browser"]+"-"+job["browser_short_version"]
            jobPassed = job["passed"] if job["passed"] else " "
            
            jobPassedConverted =" "
            if jobPassed != " ":
                if job["passed"]:
                    jobPassedConverted = "Passed"
                else:
                    jobPassedConverted = "Failed"
                
            ts = int(job["start_time"])
            dateAndTime = datetime.utcfromtimestamp(ts).strftime('%Y-%m-%d %H:%M:%S')
            link = "[View]("+self.http_connection.get("url")+"/jobs/"+job["id"]+")"
            print '%s|%s|%s|%s|%s|%s' % (description, dateAndTime, jobBuildId, jobName, jobPassed,link)
            jobs[str(job["id"])] = jobPassedConverted

        filteredJobsStr = json.dumps(filteredJobs)
        return filteredJobsStr, jobs
