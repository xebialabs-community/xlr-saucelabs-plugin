
# Developer Information - End to End Integration Testing #

# Overview #

This xlr-saucelabs-plugin project includes a customized gradle task that performs end to end integration testing. The task, named integrationTesting, starts up a version of XLR and a mock SauceLabs server in a docker container. The task imports into XLR a number of templates to support testing and then the task runs JUnit tests. 

## How to run the integrationTesting task: ##
At the command line or terminal: 
`./gradlew clean integrationTesting`

# Details #
This section describes the moving parts that make integration tesing work.


**XL Docker Gradle Plugin**

The [XL Docker Gradle Plugin](https://github.com/xebialabs-community/xl-docker-gradle-plugin) compiles and runs the SauceLabs plugin within a containerized instance of XL Release. It is also configured to spin up a mock SauceLabs Server
1. The xl-docker-gradle-plugin is configured in the build.gradle file - see the plugin and xlDocker sections
2. The xl-docker-gradle-plugin uses these docker files to create XL Release and the mock SauceLabs Server
    1. `<project folder>/src/test/resources/saucelabs-stub/Dockerfile` This file configures the mock SauceLabs Server image 
    2. `<project folder>/src/test/resources/docker/docker-compose.yml` This file defines and runs the XL Release service and the mock SauceLabs service. NOTE: This is also where you configure the path that points to your XLR license
3. Once the containers are started, the [dockerized XL Release](https://hub.docker.com/r/xebialabsunsupported/xld_dev_run) initializes itself by running the script `<project folder>/src/test/resources/docker/initialize/initialize_data.sh`
    1.  This initialization script imports into XLR a server configuration that points to the SauceLabs Mock Server. 
    2. The script then imports the templates that support integration testing. 
    3. The template files are stored in `<project folder>/src/test/resources/docker/initialize/data`

**SauceLabs Mock Server**

The SauceLabs Mock Server is simply a Flask app that responds to http requests. The app returns various canned responses to pattern matched get and post requests. The patterns are based upon the SauceLabs REST Api and the canned responses mimic the actual SauceLabs Server responses gathered during development of the xlr-saucelabs-plugin. In this way, integration testing can verify that the xlr-saucelabs-plugin makes valid SauceLab requests and properly processes SauceLabs responses. 
1. The canned responses are stored here: `<project folder>/src/test/resources/saucelabs-stub/app/responses`
2. The SauceLabs Mock Server file: `<project folder>/src/test/resources/saucelabs-stub/app/app.py`

**JUnit Integration Tests**

The JUnit integrations tests are here: `<project folder>/src/test/java/SauceLabsIntegrationTest.java`. Most tests follow this pattern:

1. Before any tests are run, the test code uses the XL Release api to create and start releases based upon the previously imported templates. The result of each release run is then acquired via the XLR REST api.
2. Each test compares the archived release output to expected results to determine if a test has passed or failed.
    1. The expected results for each test are stored in txt files here: `<project folder>/src/test/resources/testExpected`
3. Currently there are nine tests, testing the following xlr-saucelabs-plugin features:
    1. Retrieve all jobs
    2. Confirm that task credentials override credentials configured in the SauceLabs server configuration
    3. Retrieve jobs filtered by Build Id
    4. Retrieve jobs filtered by Exact Job Name
    5. Retrieve jobs filtered by Wild Card Name
    6. Retrieve jobs filtered by Time Restriction
    7. Retrieve jobs filtered by Number of Jobs (Size) Restriction
    8. Retrieve jobs filtered by Time and Size
    9. Retrieve jobs filtered by Size and Wild Card Name

**Gradle Task integrationTesting**

In the build.gradle file a custom task named `integrationTesting` has been added to the `afterEvaluate` gradle phase. The custom task calls, in order:
1. `runDockerCompose` (customized task provided by the XL Docker Gradle Plugin)
2. a sleep task (to allow XLR time to spin up)
3. a custom `subIntegrationTest` configure to run only the integration test. The task has been configured to print out test println statements. This can be commented out, if desired.  
4. And finally, `stopDockerCompose`, a task that will stop and remove the test container

# How to Add Tests #

During development of a new feature you will be interacting with an actual SauceLabs Server and at that time you should collect some artifacts to facilitate testing.

***From SauceLabs***
1. HTTP Request Pattern - The pattern of the http request the new plugin feature will make to the actual SauceLabs Server
2. SauceLabs Response File - The JSON response returned SauceLabs Server 
    1. you can gather this information by using curl and the 
   [SauceLabs Server REST API](https://wiki.saucelabs.com/display/DOCS/The+Sauce+Labs+REST+API).  See example in tips below.
   2. Format the response (see tips below) and save it to a file. It may be necessary to edit some values in the JSON - change user names etc. to make test data consistent. 
   3. Copy the response file to `<project folder>/src/test/resources/saucelabs-stub/app/responses`

***From XL Release***

1. Template File
    1. Set up a template with task configuration that exercises the new feature
    2. Export the template (IMPORTANT: See tip below about exporting templates)
    3. Edit the template so that the attribute “saucelabsServer” has the identifier that matches the name of the server configuration imported during initialization (take a look at existing templates)
    4. Rename the template file appropriately and move this template into `<project folder>/src/test/resources/docker/initialize/data`
    5. Edit `<project folder>/src/test/resources/docker/initialize/initialize_data.sh` to import this new template
2. Expected Result File
    1. Within XLR, run a release that exercises the new feature against an actual SauceLabs server
    2. Using the XL Release REST API and curl, retrieve the JSON from the archived successful release (see example of a curl command below). For some tests, it may be possible to simply download the release attachment. In any case, your objective is to collect data that can be used for comparison in an assert test to verify successful test completion. [XL Release REST API](https://docs.xebialabs.com/xl-release/8.2.x/rest-docs/)
    3. Save the string to be used for comparison to a file, name it appropriately and save to: `<project folder>/src/test/resources/testExpected`

***Code The Test***

1. `test/resources/saucelabs-stub/app/app.py`
    1. Following previously coded examples, use the new request pattern mentioned previously and code the mock Server to respond with the saucelabs response file you created
2. `test/java/integration/SauceLabsIntegrationTest.java`
    1. Following previously coded examples, add the test code. We are using Rest Assured JsonPath to retrieve values from the archived release response to compare to the expected response file. [JsonPath resource](https://www.testingexcellence.com/parse-json-response-rest-assured/)

**Tips**
1. This site is very useful for verifying and formatting JSON - [JSON Formatter](https://jsonformatter.org/)
2. Example SauceLabs REST api curl command: `curl -u <put username here>:<put access code here> "http://saucelabs.com/rest/v1/<put username here>/jobs?full=true"`
3. Export templates as xlr files and then unarchive. You can then format the JSON files and edit them.  In order to reimport the un-archived JSON files, you must edit the files and surround the JSON content with square brackets `[  ]`. 
4. Example of XLR REST api curl command that retrieves a successful archive release: `curl -u admin:admin "http://localhost:15516/api/v1/releases/archived/Applications/Release6b18eaeaaeb143d5ba1840f3653037da"`



