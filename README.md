# Cloud Server and Client on Netty

[Youtube link](https://youtu.be/J_yAZTvAVTY?si=EmJVONONuh9G-VEE)

## How to use:

1. Run server with:

```
export SERVER_PORT="8080"
export SERVER_WORK_DIRECTORY=$(realpath ServerDirectory)
./gradlew :server:run --console plain
```

2. Run client with:

```
export SERVER_HOST="localhost"
export SERVER_PORT="8080"
export CLIENT_WORK_DIRECTORY=$(realpath ClientDirectory)
./gradlew :client:run --console plain
```

## Available accounts

| Login | Password |
|-------|----------|
| Maks  | 1234     |
| Alice | pass     |

## Example of using:

```
Введите 'exit' для выхода
All possible command types: [authentication, load_file, get_info, download_file, move, copy]
Enter command type > authentication
Print login > Maks
Print password > 1234
Encoding request...
Decoding response...
Your command was successfully completed!

All possible command types: [authentication, load_file, get_info, download_file, move, copy]
Enter command type > get_info
Encoding request...
Decoding response...
Server Files Directory Tree:
----------------------------
Directory 'Maks'
---File 'README.md'
----------------------------

All possible command types: [authentication, load_file, get_info, download_file, move, copy]
Enter command type > load_file
Enter client filePath > README.md
Enter server filePath > newDirectory/someFolder
Enter server fileName > some.md
Encoding request...
Decoding response...
Your command was successfully completed!

All possible command types: [authentication, load_file, get_info, download_file, move, copy]
Enter command type > get_info
Encoding request...
Decoding response...
Server Files Directory Tree:
----------------------------
Directory 'Maks'
---File 'README.md'
---Directory 'newDirectory'
------Directory 'someFolder'
---------File 'some.md'
----------------------------

All possible command types: [authentication, load_file, get_info, download_file, move, copy]
Enter command type > copy
Enter source filePath > newDirectory/someFolder
Enter source fileName > some.md
Enter destination filePath > newDirectory
Enter destination fileName > COPIED_FILE.md
Encoding request...
Decoding response...
Your command was successfully completed!

All possible command types: [authentication, load_file, get_info, download_file, move, copy]
Enter command type > get_info
Encoding request...
Decoding response...
Server Files Directory Tree:
----------------------------
Directory 'Maks'
---File 'README.md'
---Directory 'newDirectory'
------Directory 'someFolder'
---------File 'some.md'
------File 'COPIED_FILE.md'
----------------------------
exit
```

Server logs:
```
Request from client: RequestData{commandType=AUTHENTICATION, arguments=[Maks, 1234], payload=Empty}
Response for client: ResponseData{status=SUCCESS, arguments=[], payload=Empty}
Request from client: RequestData{commandType=GET_INFO, arguments=[], payload=Empty}
Response for client: ResponseData{status=INFO, arguments=[], payload=Non-empty}
Request from client: RequestData{commandType=LOAD_FILE, arguments=[newDirectory/someFolder, some.md], payload=Non-empty}
Response for client: ResponseData{status=SUCCESS, arguments=[], payload=Empty}
Request from client: RequestData{commandType=GET_INFO, arguments=[], payload=Empty}
Response for client: ResponseData{status=INFO, arguments=[], payload=Non-empty}
Request from client: RequestData{commandType=COPY, arguments=[newDirectory/someFolder, some.md, newDirectory, COPIED_FILE.md], payload=Empty}
Response for client: ResponseData{status=SUCCESS, arguments=[], payload=Empty}
Request from client: RequestData{commandType=GET_INFO, arguments=[], payload=Empty}
Response for client: ResponseData{status=INFO, arguments=[], payload=Non-empty}
```

## Request and his Response accordingly by command types

### Short command list

**[AUTHENTICATION](https://github.com/central-university-dev/cloud-storage-part-1-hw-3-maksmolchdmitr/tree/develop#authentication) +
[LOAD_FILE](https://github.com/central-university-dev/cloud-storage-part-1-hw-3-maksmolchdmitr/tree/develop#load_file) +
[GET_INFO](https://github.com/central-university-dev/cloud-storage-part-1-hw-3-maksmolchdmitr/tree/develop#get_info) +
[DOWNLOAD_FILE](https://github.com/central-university-dev/cloud-storage-part-1-hw-3-maksmolchdmitr/tree/develop#download_file) +
[MOVE](https://github.com/central-university-dev/cloud-storage-part-1-hw-3-maksmolchdmitr/tree/develop#move) +
[COPY](https://github.com/central-university-dev/cloud-storage-part-1-hw-3-maksmolchdmitr/tree/develop#copy)**

* **Request Template**

```json
{
  "commandType": "SOME_COMMAND_TYPE",
  "arguments": [
    "arg1",
    "arg2",
    "..."
  ],
  "payload": "some payload bytes..."
}
```

* **Response Template**

```json
{
  "status": "SOME_RESPONSE_STATUS",
  "arguments": [
    "arg1",
    "arg2",
    "..."
  ],
  "payload": "some payload bytes..."
}
```

---

#### AUTHENTICATION

* AUTHENTICATION request

```json
{
  "commandType": "AUTHENTICATION",
  "arguments": [
    "login",
    "password"
  ]
}
```

* Response
    * SUCCESS - You was successfully authenticated
    * FAILED - You send illegal login and password

---

#### LOAD_FILE

* LOAD_FILE request

```json
{
  "commandType": "LOAD_FILE",
  "arguments": [
    "filePath",
    "fileName"
  ],
  "payload": "fileBytes"
}
```

* Response
    * SUCCESS - Your file was successfully loaded
    * SERVER_ERROR - Went something wrong with server

---

#### GET_INFO

* GET_INFO request

```json
{
  "commandType": "GET_INFO"
}
```

* Response

```json
{
  "status": "SOME_STATUS",
  "payload": "FileObject as bytes"
}
```

---

#### DOWNLOAD_FILE

* DOWNLOAD_FILE request

```json
{
  "commandType": "DOWNLOAD_FILE",
  "arguments": [
    "filePath",
    "fileName"
  ]
}
```

* Response
    * FILE - Server send and download file from cloud!
    * FILE_NOT_FOUND - Illegal filePath or fileName!

```json
{
  "status": "SOME_STATUS",
  "payload": "File as bytes"
}
```

---

#### MOVE

* MOVE request

```json
{
  "commandType": "MOVE",
  "arguments": [
    "sourceFilePath",
    "sourceFileName",
    "destinationFilePath",
    "destinationFileName"
  ]
}
```

* Response
    * SUCCESS - Your command was successfully completed!
    * FILE_NOT_FOUND - Illegal filePath or fileName!

---

#### COPY

* COPY request

```json
{
  "commandType": "COPY",
  "arguments": [
    "sourceFilePath",
    "sourceFileName",
    "destinationFilePath",
    "destinationFileName"
  ]
}
```

* Response
    * SUCCESS - Your command was successfully completed!
    * FILE_NOT_FOUND - Illegal filePath or fileName!

---

## Response status types

* SUCCESS - If command was successfully completed
* FAILED - You send illegal login and password
* ACCESS_DENIED - You was banned by Server
* ALREADY_AUTHENTICATED - You was already authenticated
* SERVER_ERROR - If went something wrong with server
* INVALID_REQUEST - If went something wrong with client request
* INFO - send you information about files on server
* FILE - Server send you file
* FILE_NOT_FOUND - Illegal filePath or fileName!