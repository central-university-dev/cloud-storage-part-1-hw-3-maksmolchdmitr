# Cloud Server and Client on Netty
## Example if using:
## Request command types
* Request Template
```json
{
  "commandType": "SOME_COMMAND_TYPE",
  "arguments": ["arg1", "arg2", "..."],
  "payload": "some payload bytes..."
}
```
* AUTHENTICATION
```json
{
  "commandType": "AUTHENTICATION",
  "arguments": ["login", "password"]
}
```
* LOAD_FILE
```json
{
  "commandType": "LOAD_FILE",
  "arguments": ["filePath", "fileName"],
  "payload": "fileBytes"
}
```