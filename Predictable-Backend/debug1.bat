cd src\main\predictable-client
if "%1" == "prod" (
   call ng build --prod
)else (
	call ng build
)
cd ..\..\..
call mvn clean
call mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5002"