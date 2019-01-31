/**
 * Copyright 2019 XEBIALABS
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package integration;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class SauceLabsIntegrationTest {

    private static final String BASE_URI = "http://localhost:15516/api/v1";
    
    private static final String TEST_TEMPLATE = "/templates/Applications/Releasedd7d46077d954b3ea30fedf4d468326d";
    private static final String START_RELEASE_ALL = "/templates/Applications/Releasedd7d46077d954b3ea30fedf4d468326d/start";
    private static final String START_RELEASE_TIME_AND_SIZE = "/templates/Applications/Releasebe3958660d1147a6b45b032c1f8a9291/start";
    private static final String START_RELEASE_BAD_CREDS = "/templates/Applications/Release6c9c6ed9a1a46878ca88a05367e2d42/start";
    private static final String START_RELEASE_BUILD_ID = "/templates/Applications/Releaseac54c03d35424ed8aa3e6932753697b3/start";
    private static final String START_RELEASE_EXACT_NAME = "/templates/Applications/Release22bce27b7f1241cc98bbcf6a34ae7a77/start";
    private static final String START_RELEASE_WILD_CARD_NAME = "/templates/Applications/Release44292616ee5b4c45b01366032f720f58/start";
    private static final String START_RELEASE_SIZE_RESTRICT = "/templates/Applications/Release18d36b058452406caf0e615fc3bff9de/start";
    private static final String START_RELEASE_SIZE_AND_WILD_CARD = "/templates/Applications/Release237b55521bea4f49bf3a0f4a4fa1f4d7/start";
    private static final String START_RELEASE_TIME_RESTRICT = "/templates/Applications/Release404153ff67ca428f9d8eb2ef14ac4c8f/start";
   
    private static final String GET_ARCHIVED_RELEASE_PREFIX = "/releases/archived/";
    private static final String GET_FAILED_RELEASE_PREFIX = "/releases/";

    private static RequestSpecification httpRequest = null;

    private static JsonPath archivedResponse_All = null;
    private static JsonPath archivedResponse_TimeAndSize = null;
    private static JsonPath failedResponse_BadCreds = null;
    private static JsonPath archivedResponse_BuildId = null;
    private static JsonPath archivedResponse_ExactName = null;
    private static JsonPath archivedResponse_WildCardName = null;
    private static JsonPath archivedResponse_TimeRestrict = null;
    private static JsonPath archivedResponse_SizeRestrict = null;
    private static JsonPath archivedResponse_SizeAndWildCard = null;

    static {
        baseURI = BASE_URI;
        // Configure authentication
        httpRequest = given().auth().preemptive().basic("admin", "admin");
    }

    @BeforeClass
    public static void setup() throws InterruptedException {

        String plannedRelId_All = "";
        String plannedRelId_TimeAndSize = "";
        String plannedRelId_BadCreds = "";
        String plannedRelId_BuildId = "";
        String plannedRelId_ExactName = "";
        String plannedRelId_WildCardName = "";
        String plannedRelId_TimeRestrict = "";
        String plannedRelId_SizeRestrict = "";
        String plannedRelId_SizeAndWildCard = "";

        // Make sure XLR has started. If not, sleep for 60
        Response response = httpRequest.request(Method.GET, TEST_TEMPLATE);
        int status = response.getStatusCode();
        if (status != 200) {
            System.out.println("Status code was " + status + ", Pausing for 60, waiting for XLR to start. ");
            Thread.sleep(60000);
        }
        // Prepare httpRequest
        JSONObject requestParams = getRequestParams();
        httpRequest.header("Content-Type", "application/json");
        httpRequest.header("Accept", "application/json");
        httpRequest.body(requestParams.toJSONString());

        ///////// Start releases and retrieve the planned release id.
        response = httpRequest.post(START_RELEASE_ALL);
        if (response.getStatusCode() != 200) {
            System.out.println("Status line, ALL was " + response.getStatusLine() + "");
        } else {
            plannedRelId_All = response.jsonPath().getString("id");
        }
        //////////
        response = httpRequest.post(START_RELEASE_TIME_AND_SIZE);
        if (response.getStatusCode() != 200) {
            System.out.println("Status line, Time and Size was " + response.getStatusLine() + "");
        } else {
            plannedRelId_TimeAndSize = response.jsonPath().getString("id");
        }
        //////////
        response = httpRequest.post(START_RELEASE_BAD_CREDS);
        if (response.getStatusCode() != 200) {
            System.out.println("Status line, bad creds was " + response.getStatusLine() + "");
        } else {
            plannedRelId_BadCreds = response.jsonPath().getString("id");
        }
        //////////
        response = httpRequest.post(START_RELEASE_BUILD_ID);
        if (response.getStatusCode() != 200) {
            System.out.println("Status line, build id was " + response.getStatusLine() + "");
        } else {
            plannedRelId_BuildId = response.jsonPath().getString("id");
        }
        //////////
        response = httpRequest.post(START_RELEASE_EXACT_NAME);
        if (response.getStatusCode() != 200) {
            System.out.println("Status line, exact name was " + response.getStatusLine() + "");
        } else {
            plannedRelId_ExactName = response.jsonPath().getString("id");
        }
        //////////
        response = httpRequest.post(START_RELEASE_WILD_CARD_NAME);
        if (response.getStatusCode() != 200) {
            System.out.println("Status line, wild card name was " + response.getStatusLine() + "");
        } else {
            plannedRelId_WildCardName = response.jsonPath().getString("id");
        }
        //////////
        response = httpRequest.post(START_RELEASE_TIME_RESTRICT);
        if (response.getStatusCode() != 200) {
            System.out.println("Status line, time restrict was " + response.getStatusLine() + "");
        } else {
            plannedRelId_TimeRestrict = response.jsonPath().getString("id");
        }
        //////////
        response = httpRequest.post(START_RELEASE_SIZE_RESTRICT);
        if (response.getStatusCode() != 200) {
            System.out.println("Status line, size restrict was " + response.getStatusLine() + "");
        } else {
            plannedRelId_SizeRestrict = response.jsonPath().getString("id");
        }
        //////////
        response = httpRequest.post(START_RELEASE_SIZE_AND_WILD_CARD);
        if (response.getStatusCode() != 200) {
            System.out.println("Status line, sizeAndWildCard was " + response.getStatusLine() + "");
        } else {
            plannedRelId_SizeAndWildCard = response.jsonPath().getString("id");
        }
        
        ///////// Get Archived or Failed responses
        // Sleep so XLR can finish processing releases
        System.out.println("Pausing for 120 seconds, waiting for release to archive. If most requests fail with 404, consider sleeping longer.");
        Thread.sleep(120000);
        //////////
        response = httpRequest.get(GET_ARCHIVED_RELEASE_PREFIX + plannedRelId_All);
        if (response.getStatusCode() != 200) {
            System.out.println("Status line for get archived ALL was " + response.getStatusLine() + "");
        } else {
            archivedResponse_All = response.jsonPath();
        }
        //////////
        response = httpRequest.get(GET_ARCHIVED_RELEASE_PREFIX + plannedRelId_TimeAndSize);
        if (response.getStatusCode() != 200) {
            System.out.println("Status for get archived TimeandSize was " + response.getStatusLine() + "");

        } else {
            archivedResponse_TimeAndSize = response.jsonPath();
        }
        //////////
        // We expect this to be a failed release we query for a release, not an archived release
        response = httpRequest.get(GET_FAILED_RELEASE_PREFIX + plannedRelId_BadCreds);
        if (response.getStatusCode() != 200) {
            System.out.println("Status for get failed relelase BadCreds was " + response.getStatusLine() + "");
        } else {
            failedResponse_BadCreds = response.jsonPath();
        }
        //////////
        response = httpRequest.get(GET_ARCHIVED_RELEASE_PREFIX + plannedRelId_BuildId);
        if (response.getStatusCode() != 200) {
            System.out.println("Status for get archived Build Id was " + response.getStatusLine() + "");

        } else {
            archivedResponse_BuildId = response.jsonPath();
        }
        //////////
        response = httpRequest.get(GET_ARCHIVED_RELEASE_PREFIX + plannedRelId_ExactName);
        if (response.getStatusCode() != 200) {
            System.out.println("Status for get archived Exact Name was " + response.getStatusLine() + "");

        } else {
            archivedResponse_ExactName = response.jsonPath();
        }
        //////////
        response = httpRequest.get(GET_ARCHIVED_RELEASE_PREFIX + plannedRelId_WildCardName);
        if (response.getStatusCode() != 200) {
            System.out.println("Status for get archived Wild Card Name was " + response.getStatusLine() + "");

        } else {
            archivedResponse_WildCardName = response.jsonPath();
        }
        //////////
        response = httpRequest.get(GET_ARCHIVED_RELEASE_PREFIX + plannedRelId_TimeRestrict);
        if (response.getStatusCode() != 200) {
            System.out.println("Status for get archived Time Restrict was " + response.getStatusLine() + "");

        } else {
            archivedResponse_TimeRestrict = response.jsonPath();
        }
        //////////
        response = httpRequest.get(GET_ARCHIVED_RELEASE_PREFIX + plannedRelId_SizeRestrict);
        if (response.getStatusCode() != 200) {
            System.out.println("Status for get archived Size Restrict was " + response.getStatusLine() + "");

        } else {
            archivedResponse_SizeRestrict = response.jsonPath();
        }
        //////////
        response = httpRequest.get(GET_ARCHIVED_RELEASE_PREFIX + plannedRelId_SizeAndWildCard);
        if (response.getStatusCode() != 200) {
            System.out.println("Status for get archived SizeAndWildCard was " + response.getStatusLine() + "");

        } else {
            archivedResponse_SizeAndWildCard = response.jsonPath();
        }
    }

    @Test
    public void confirmCommentAll() {
        assertTrue(archivedResponse_All != null);
        String comments = archivedResponse_All.get("phases[0].tasks[0].comments[0].text").toString();
        Assert.assertTrue(readFile("testExpected/getAll_Comments.txt").equals(comments));
        System.out.println("confirmCommentAll passed ");

    }


    @Test
    public void confirmTaskCredsOverrideServerCreds() {
        // This tests to see if task creds override server creds as designed
        // We put bad creds in the task and then expect an authentication failure
        // No need to compare to expected results, just make sure the response contains 401 code
        assertTrue(failedResponse_BadCreds != null);
        String comments = failedResponse_BadCreds.get("phases[0].tasks[0].comments[0].text").toString();
        Assert.assertTrue(comments.contains("401"));
        System.out.println("confirmTaskCredsOverrideServerCreds passed ");
    }

    @Test
    public void confirmCommentBuildId() {
        assertTrue(archivedResponse_BuildId != null);
        String comments = archivedResponse_BuildId.get("phases[0].tasks[0].comments[0].text").toString();
        Assert.assertTrue(readFile("testExpected/buildId_Comments.txt").equals(comments));
        System.out.println("confirmCommentBuildId passed ");
    }

    @Test
    public void confirmCommentExactName() {
        assertTrue(archivedResponse_ExactName != null);
        String comments = archivedResponse_ExactName.get("phases[0].tasks[0].comments[0].text").toString();
        Assert.assertTrue(readFile("testExpected/exactName_Comments.txt").equals(comments));
        System.out.println("confirmCommentExactName passed ");
    }

    @Test
    public void confirmCommentWildCardName() {
        assertTrue(archivedResponse_WildCardName != null);
        String comments = archivedResponse_WildCardName.get("phases[0].tasks[0].comments[0].text").toString();
        Assert.assertTrue(readFile("testExpected/wildCardName_Comments.txt").equals(comments));
        System.out.println("confirmCommentWildCardName passed ");
    }

    @Test
    public void confirmCommentTimeRestrict() {
        assertTrue(archivedResponse_TimeRestrict != null);
        String comments = archivedResponse_TimeRestrict.get("phases[0].tasks[0].comments[0].text").toString();
        Assert.assertTrue(readFile("testExpected/timeRestrict_Comments.txt").equals(comments));
        System.out.println("confirmCommentTimeRestrict passed ");
    }

    @Test
    public void confirmCommentSizeRestrict() {
        assertTrue(archivedResponse_SizeRestrict != null);
        String comments = archivedResponse_SizeRestrict.get("phases[0].tasks[0].comments[0].text").toString();
        //System.out.println("SizeRestrict comments - "+comments);
        Assert.assertTrue(readFile("testExpected/sizeRestrict_Comments.txt").equals(comments));
        System.out.println("confirmCommentSizeRestrict passed ");
    }

    @Test
    public void confirmCommentTimeAndSize() {
        assertTrue(archivedResponse_TimeAndSize != null);
        String comments = archivedResponse_TimeAndSize.get("phases[0].tasks[0].comments[0].text").toString();
        Assert.assertTrue(readFile("testExpected/timeAndSize_Comments.txt").equals(comments));
        System.out.println("confirmCommentTimeAndSize passed ");
    }

    @Test
    public void confirmCommentSizeAndWildCard() {
        assertTrue(archivedResponse_SizeAndWildCard != null);
        String comments = archivedResponse_SizeAndWildCard.get("phases[0].tasks[0].comments[0].text").toString();
        Assert.assertTrue(readFile("testExpected/sizeAndWildCard_Comments.txt").equals(comments));
        System.out.println("confirmCommentSizeAndWildCard passed ");
    }

    @AfterClass
    public static void tearDown() {
        System.out.println("Runs after all tests.");
    }

    // Utility Methods

    static String readFile(String path) {
        StringBuilder result = new StringBuilder("");

        // Get file from resources folder
        ClassLoader classLoader = SauceLabsIntegrationTest.class.getClassLoader();
        File file = new File(classLoader.getResource(path).getFile());
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append("\n");
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    static JSONObject getRequestParams() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("releaseTitle", "release from api");
        requestParams.put("folderId", "Applications/Folder01345a690c16b345168751d62934e912");
        requestParams.put("variables", new JSONObject());
        requestParams.put("releaseVariables", new JSONObject());
        requestParams.put("releasePasswordVariables", new JSONObject());
        requestParams.put("scheduledStartDate", null);
        requestParams.put("autoStart", false);
        return requestParams;
    }
}
