# How to use
1. Start a server:
`./gradlew :server:run --console plain`
2. Start a client or many clients with command:
`./gradlew :client:run --console plain`
3. Answer to question 'You want to try log in? Y/n
   ' - **Y**
4. Enter username
5. Enter password 
6. Server must send response as:  
"isSuccess" - true or false,  
"message" - string with message from server,  
"countAttempts" - count of auth attempts  
7. Looping from step 3 to 6...