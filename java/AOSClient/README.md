# AOSClient

## Building
Build without unit tests:

`./gradlew clean build -x test`

## Unit Tests
Run all unit tests:

`./gradlew test`

## Test Server
The AOSClient library includes a CoAP server for testing.

### Configuration
The server code is located in the server package.

### Creating resources
Resources can be added to the server using the resource class.


## Usage

 Use the AOSClient to access a CSE using an AE.
 
`AOSClient client = new AOSClient();`


`CSE mncse = client.getCSE(HOST, PORT);`

Register your AE with the CSE.  Another instance of AE will be retuned that will be used to access protected resources on the CSE.

`AE ae = mncse.register(new AE(APP_ID, APP_NAME, POINT_OF_ACCESS));`

Use the AE returned from the registration process to retrive a Node resource.

`Node node = mncse.retrieveNode(ae);`