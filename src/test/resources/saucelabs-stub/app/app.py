# Copyright (c) 2019 XebiaLabs

# This software is released under the MIT License.
# https://opensource.org/licenses/MIT

from flask import Flask
from flask import request, Response
from flask import make_response
from werkzeug.exceptions import HTTPException, BadRequest, NotFound
from functools import wraps
import os, io, json

app = Flask(__name__)

def getFile( fileName, status="200" ):
     filePath = "/saucelabs-stub/responses/%s" % fileName
     if not os.path.isfile(filePath):
        raise NotFound("Unable to load response file")

     f = io.open(filePath, "r", encoding="utf-8")

     resp = make_response( (f.read(), status) )
     resp.headers['Content-Type'] = 'application/json; charset=utf-8'

     return resp

def check_auth(username, password):
    """This function is called to check if a username /
    password combination is valid.
    """
    return username == 'xlr_test' and password == 'admin'

def authenticate():
    """Sends a 401 response that enables basic auth"""
    return Response(
    'Could not verify your access level for that URL.\n'
    'You have to login with proper credentials', 401,
    {'WWW-Authenticate': 'Basic realm="Login Required"'})


def requires_auth(f):
    """
    Determines if the basic auth is valid
    """
    @wraps(f)
    def decorated(*args, **kwargs):
        auth = request.authorization
        if not auth or not check_auth(auth.username, auth.password):
            return authenticate()
        return f(*args, **kwargs)
    return decorated


@app.route('/')
def index():
    return "Hello, from SauceLabs Stub, this is a test!"

@app.route('/rest/v1/<username>/jobs', methods=['GET'])
@requires_auth
def getJobs(username):
    if request.args:
        full = request.args.get('full')
        limit = request.args.get('limit')
        fromTime = request.args.get('from')
        toTime = request.args.get('to')
        # Case - request was for all
        if full and full=="true":
            if not limit and not fromTime:
                return getFile("allJobs.json")
            else:
                if not limit and fromTime and toTime:
                    return getFile("time.json")
                else:
                    if limit and fromTime and toTime:
                        return getFile("timeAndSize.json")
                    else:
                        if limit and not fromTime and not toTime:
                            return getFile("size.json")
                        else:
                            raise BadRequest('Request failed because request parameters were not set as expected')  
        else:
            raise BadRequest('Request failed because request parameter full=true was not found')
    else:
        raise BadRequest('Request failed because request parameter full=true was not found')

@app.route('/jobs/<jobid>', methods=['GET'])
def getJob(jobid):
    return "If this were a real SauceLabs Server, this page would present information about job "+jobid 

if __name__ == '__main__':
    app.run(debug=True)