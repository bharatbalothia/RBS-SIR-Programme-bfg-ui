call mvn liberty:stop
if not "%ERRORLEVEL%" == "0" exit /b
call mvn clean install
if not "%ERRORLEVEL%" == "0" exit /b
call mvn liberty:deploy
if not "%ERRORLEVEL%" == "0" exit /b
call mvn liberty:start
