# BFG-MAIN

Root Maven project consists of 2 modeules: bfg-ui and bfg-api


## Build

Run `mvn clean install` to build bfg-ui and bfg-api modules 
OR
Navigate bfg-ui or bfg-api folder and `mvn clean install` to build one module at a time.

Navigate to bfg-api folder.

Run `mvn liberty:start`

Run `mvn liberty:deploy`


## Testing

Open this links to see that application is up and running

http://localhost:9080/index.html
http://localhost:9080/test
