# Python SDK Sample Apps

## Python Supported Versions
Python Sample Apps have been verified with Python Version 3.6.9 on Ubuntu Linux.

## Python Module Supported Versions
libcoap : 0.4b2.post0 (beta pulled from github)

## Installation
### Virtual Environment
```
$ pip install virtualenv &&
$ virtualenv -p /usr/bin/python3.6 virtenv &&
$ source virtenv/bin/activate
```

### Dependencies
```
$ pip install --upgrade "git+https://github.com/chrysn/aiocoap#egg=aiocoap[all]"
```

## Usage
### Run the script and select the API to exercise.
```
$ python aos-ms-client.py

Please selected an API.  Ctrl-c to stop.

1: Register AE
2: Node Retrieve
3: Create Meter Read Policy
4: Delete Meter Read Policy
5: Create Subscription
6: Delete Subscription
7: De-Register AE
8: Exit

  Select an API: 1
INFO:__main__:Invoking AE Register API
INFO:__main__:Payload: {"ae":{"aa":["apn","api","aei"],"aei":"C000432","api":"Nra1.com.aos.iot","apn":"metersvc-smpl","at":["/PN_CSE/aeA58bd97cb00272a"],"ct":"20200720T013258","lt":"20200610T190201","pi":"cseBase","poa":["coap://192.168.225.34:5683"],"ri":"C000432","rn":"C000432","ty":2}}
```