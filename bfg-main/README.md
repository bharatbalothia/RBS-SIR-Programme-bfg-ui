# BFG-MAIN

Root Maven project consists of 2 modules: bfg-ui and bfg-api.

## Build

Run `mvn clean install` to build bfg-ui and bfg-api modules.

OR

Navigate to bfg-ui or bfg-api folder and run `mvn clean install` to build one module at a time.

Navigate to bfg-api folder.

Run `mvn liberty:start`

Run `mvn liberty:deploy`


OR 

Navigate to bfg-api folder.

Run `.\build_and_deploy.bat`

## Build for RBS POC

Navigate to bfg-ui folder and run `mvn clean install -Ppoc` to build UI module. Repeat the same in bfg-api folder.

## Testing

Open this links to see that application is up and running

http://localhost:9080/bfgui/index.html

http://localhost:9080/bfgui/test


## API Docs

http://localhost:9080/bfgui/swagger-ui.html 