#!/usr/bin/env python
import sys
import json
import os,binascii
import asyncio
import aiocoap
import aiocoap.resource
import logging
from aiocoap import *
from aiocoap.numbers import Code
from aiocoap.optiontypes import StringOption
from aiocoap.optiontypes import UintOption


#set up logger
#logging.basicConfig(level=logging.DEBUG)
log = logging.getLogger(__name__)
log.setLevel(logging.INFO)

CONTENT_FORMATS = {
    "application/json": 50
}

# options.
ONEM2M_FR_OPTION = 256
ONEM2M_RQI_OPTION = 257
ONEM2M_TY_OPTION = 267
ONEM2M_OP_OPTION = 256
ONEM2M_TO_OPTION = 257
ONEM2M_RQI = 257

HOST = '192.168.225.1'
POA = 'coap://192.168.225.34:5683'
PORT = 8100
AE_APP_ID = 'Nra1.com.aos.iot'
AE_APP_NAME = 'metersvc-smpl'
AE_CREDENTIAL = ''
REG_PATH = '.'
MS_POLICY_PATH = './metersvc/policies'
MS_READS_PATH = './metersvc/reads'
MS_NODE_PATH = 'node?rcn=4'

CONTENT_FORMAT = CONTENT_FORMATS['application/json'] 

#one global
aeId = None

async def register():
    log.debug("register()")
    rqi = binascii.b2a_hex(os.urandom(5)).decode('utf-8')
    request = Message(code=Code.POST, mtype=CON, uri = f'coap://{HOST}:{PORT}/{REG_PATH}')
    request.opt.add_option(StringOption(OptionNumber.URI_HOST, HOST))
    request.opt.add_option(UintOption(OptionNumber.URI_PORT, PORT))
    request.opt.add_option(UintOption(OptionNumber.CONTENT_FORMAT, CONTENT_FORMAT))
    request.opt.add_option(UintOption(OptionNumber.ACCEPT, CONTENT_FORMAT))
    request.opt.add_option(StringOption(ONEM2M_FR_OPTION, AE_CREDENTIAL))
    request.opt.add_option(StringOption(ONEM2M_RQI_OPTION, rqi))
    request.opt.add_option(UintOption(ONEM2M_TY_OPTION, 2))
    request.payload = str.encode(json.dumps({
        "ae": {
            "api": AE_APP_ID,
            "apn": AE_APP_NAME,
            "poa": [POA]
           }
        }))
    log.info("Invoking AE Register API")
    await submit_request(request)

async def deregister():
    log.debug("deregister()")
    rqi = binascii.b2a_hex(os.urandom(5)).decode('utf-8')
    request = Message(code=Code.DELETE, mtype=CON, uri = f'coap://{HOST}:{PORT}/{REG_PATH}/{aeId}')
    request.opt.add_option(StringOption(OptionNumber.URI_HOST, HOST))
    request.opt.add_option(UintOption(OptionNumber.URI_PORT, PORT))
    request.opt.add_option(StringOption(ONEM2M_FR_OPTION, AE_CREDENTIAL))
    request.opt.add_option(StringOption(ONEM2M_RQI_OPTION, rqi))
    request.opt.add_option(UintOption(ONEM2M_TY_OPTION, 2))
    log.info("Invoking AE Deregister API")
    await submit_request(request)

async def create_meter_read_policy():
    log.debug("create_meter_read_policy()")
    rqi = binascii.b2a_hex(os.urandom(5)).decode('utf-8')
    request = Message(code=Code.POST, mtype=CON, uri = f'coap://{HOST}:{PORT}/{MS_POLICY_PATH}')
    request.opt.add_option(StringOption(OptionNumber.URI_HOST, HOST))
    request.opt.add_option(UintOption(OptionNumber.URI_PORT, PORT))
    request.opt.add_option(UintOption(OptionNumber.CONTENT_FORMAT, CONTENT_FORMAT))
    request.opt.add_option(UintOption(OptionNumber.ACCEPT, CONTENT_FORMAT))
    request.opt.add_option(StringOption(ONEM2M_FR_OPTION, aeId))
    request.opt.add_option(StringOption(ONEM2M_RQI_OPTION, rqi))
    request.opt.add_option(UintOption(ONEM2M_TY_OPTION, 4))
    request.payload = str.encode(json.dumps({
        "cin": {
            "rn": "metersvc-sampl-pol-01",
            "con": { 
                "read": {
                    "rtype": "powerQuality",
                    "tsched": {
                        "recper": 120,
                        "sched": {
                            "end": None,
                            "start": "2020-01-27T00:00:00"
                        }
                    }
            }
            }
    }}))
    log.info("Invoking Create Meter Read Policy API")
    await submit_request(request)

async def delete_meter_read_policy():
    log.debug("delete_meter_read_policy()")
    rqi = binascii.b2a_hex(os.urandom(5)).decode('utf-8')
    request = Message(code=Code.DELETE, mtype=CON, uri = f'coap://{HOST}:{PORT}/{MS_POLICY_PATH}/metersvc-sampl-pol-01')
    request.opt.add_option(StringOption(OptionNumber.URI_HOST, HOST))
    request.opt.add_option(UintOption(OptionNumber.URI_PORT, PORT))
    request.opt.add_option(StringOption(ONEM2M_FR_OPTION, aeId))
    request.opt.add_option(StringOption(ONEM2M_RQI_OPTION, rqi))
    log.info("Invoking Delete Meter Read Policy API")
    await submit_request(request)

async def create_subscription():
    log.debug("create_subscription()")
    rqi = binascii.b2a_hex(os.urandom(5)).decode('utf-8')
    request = Message(code=Code.POST, mtype=CON, uri = f'coap://{HOST}:{PORT}/{MS_READS_PATH}')
    request.opt.add_option(StringOption(OptionNumber.URI_HOST, HOST))
    request.opt.add_option(UintOption(OptionNumber.URI_PORT, PORT))
    request.opt.add_option(UintOption(OptionNumber.CONTENT_FORMAT, CONTENT_FORMAT))
    request.opt.add_option(UintOption(OptionNumber.ACCEPT, CONTENT_FORMAT))
    request.opt.add_option(StringOption(ONEM2M_FR_OPTION, aeId))
    request.opt.add_option(StringOption(ONEM2M_RQI_OPTION, rqi))
    request.opt.add_option(UintOption(ONEM2M_TY_OPTION, 23))
    request.payload = str.encode(json.dumps({
        "sub": {
            "rn": "metersvc-sampl-sub-01",
            "enc": {
                "net": [3]
            },
            "nct": 1,
            "nu": [aeId]
        } 

    }))
    log.info("Invoking Create Subscription API")
    await submit_request(request)

async def delete_subscription():
    log.debug("delete_subscription()")
    rqi = binascii.b2a_hex(os.urandom(5)).decode('utf-8')
    request = Message(code=Code.DELETE, mtype=CON, uri = f'coap://{HOST}:{PORT}/{MS_READS_PATH}/metersvc-sampl-sub-01')
    request.opt.add_option(StringOption(OptionNumber.URI_HOST, HOST))
    request.opt.add_option(UintOption(OptionNumber.URI_PORT, PORT))
    request.opt.add_option(StringOption(ONEM2M_FR_OPTION, aeId))
    request.opt.add_option(StringOption(ONEM2M_RQI_OPTION, rqi))
    log.info("Invoking Delete Subscription API")
    await submit_request(request)

async def retrieve_node():
    log.debug("retrieve_node()")
    rqi = binascii.b2a_hex(os.urandom(5)).decode('utf-8')
    request = Message(code=Code.GET, mtype=CON, uri = f'coap://{HOST}:{PORT}/{MS_NODE_PATH}')
    request.opt.add_option(StringOption(ONEM2M_RQI_OPTION, rqi))
    request.opt.add_option(StringOption(ONEM2M_FR_OPTION, aeId))
    request.opt.add_option(UintOption(OptionNumber.ACCEPT, CONTENT_FORMAT))
    log.info("Invoking Node Retrieve API")
    await submit_request(request)

async def submit_request(request):
    global aeId

    protocol = await Context.create_client_context()
    response = await protocol.request(request).response

    if response.code is Code.NOT_ACCEPTABLE or response.code is Code.NOT_FOUND:
        log.info(response)
    else:
        log.debug("Type: %s", response.mtype)
        log.debug("Code: %s", response.code)
        if response.opt.get_option(257):
            log.debug("Request Identifier: %s", response.opt.get_option(257)[0].value.decode("utf-8"))

        # Invalidate the aeid on deregistration.
        if request.code is Code.DELETE and REG_PATH in request.get_request_uri():
            aeId = None

        if response.payload:
            log.info("Payload: %s", response.payload.decode())

            #check for create AE response
            response_payload = response.payload.decode()

            try:
                response_payload_obj = json.loads(response_payload)
                aei = response_payload_obj.get("ae").get("aei")
                if aei:
                    aeId = aei
                    log.debug("AE Identifier %s", aeId)
            except ValueError as e:
                log.debug("Not an AE registration response")
            except AttributeError as e:
                log.debug("Not an AE registration response")

class AeResource(aiocoap.resource.Resource):
    @asyncio.coroutine
    def render_post(self, request):
        log.info('Received Notification')
        rqi = request.opt.get_option(257)[0].value.decode("utf-8")
        log.debug("Request Identifier: %s", rqi)
        payload = request.payload.decode()
        log.info("Payload: %s",  payload)

        #assemble and send response
        response = aiocoap.Message(code=aiocoap.CONTENT)
        response.opt.add_option(StringOption(ONEM2M_RQI_OPTION, rqi))
        return response;

async def notification_server():
    log.debug("notification_server()")
    s = aiocoap.resource.Site()
    s.add_resource([aeId], AeResource())
    log.info("Notification Server listening on %s/%s", POA, aeId)
    future = asyncio.ensure_future(aiocoap.Context.create_server_context(s))

async def exit():
    if aeId is not None:
        await loop.create_task(delete_subscription())
        await loop.create_task(delete_meter_read_policy())
        await loop.create_task(deregister())
    sys.exit(0)

async def main():
    # APIs
    REGISTER_AE = "Register AE"
    NODE_RETRIEVE = "Node Retrieve"
    CREATE_MTR_READ_POL = "Create Meter Read Policy"
    DELETE_MTR_READ_POL = "Delete Meter Read Policy"
    CREATE_SUB = "Create Subscription"
    DELETE_SUB  = "Delete Subscription"
    DEREGISTER = "De-Register AE"
    EXIT = "Exit"

    availableAPIs = (
        REGISTER_AE,
        NODE_RETRIEVE,
        CREATE_MTR_READ_POL,
        DELETE_MTR_READ_POL,
        CREATE_SUB,
        DELETE_SUB,
        DEREGISTER,
        EXIT
    )

    print("{}Please selected an API.  Ctrl-c to stop.{}".format(os.linesep, os.linesep))

    for i,v in enumerate(availableAPIs):
        print("{}: {}".format(i+1,v))

    api = int(input("\n  Select an API: ")) -1

    # API task to be awaited.
    task = None

    if (api == availableAPIs.index(EXIT)):
        task = loop.create_task(exit())
    else:
        if aeId is not None:
            # APIs that require registration.
            if api == availableAPIs.index(REGISTER_AE):
                print("AE already registered.")
            elif api == availableAPIs.index(NODE_RETRIEVE):
                task = loop.create_task(retrieve_node())
            elif api == availableAPIs.index(CREATE_MTR_READ_POL):
                task = loop.create_task(create_meter_read_policy())
            elif api == availableAPIs.index(DELETE_MTR_READ_POL):
                task = loop.create_task(delete_meter_read_policy())
            elif api == availableAPIs.index(CREATE_SUB):
                task = loop.create_task(create_subscription())
            elif api == availableAPIs.index(DELETE_SUB):
                task = loop.create_task(delete_subscription())
            elif (api == availableAPIs.index(DEREGISTER)):
                task = loop.create_task(deregister())
        else:
            # APIs that do not require registration.
            if(api == availableAPIs.index(REGISTER_AE)):
                task = loop.create_task(register())
            else:
                print("{}AE not registered.  Call register.".format(os.linesep))

    # Wait for task to execute.
    if task:
        await task

    # Loop prompt.
    await main()

if __name__ == "__main__":
    logging.debug("__main__")
    loop = asyncio.get_event_loop()
    # asyncio.ensure_future(notification_server())
    try:
        loop.run_until_complete(main())
        loop.run_forever()
    except KeyboardInterrupt:
        log.debug('Received Keyboard Interrupt.  Cleaning up')

        # Clean up device.
        try:
            loop.run_until_complete(delete_subscription())
        except Exception as e:
            log.debug("No subscription found")
        try:
            loop.run_until_complete(delete_meter_read_policy())
        except Exception as e:
            pass
            log.debug("No meter read policy found")
        try:
            loop.run_until_complete(deregister())
        except Exception as e:
            log.debug("AE not registered")