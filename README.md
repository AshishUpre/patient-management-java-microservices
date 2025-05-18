# note
Keep podman desktop open when running containers.

## GRPC inter service comm
When a service a is interacting with service b, the name of the corresponding .proto file will be after the service
that is being called. Hence here it will be called b.proto, b service will be server and a will be client. 


## Grpc server of billing service
In billing service we create a grpc server that starts whenever the application starts. For that first we generate
the stub codes and generated sources - using maven compile - gives target folder from which we can implement an 
interface and override its function - createBillingAccount (or any account we defined in .proto file). Note location
of proto should be inside main folder ---- is a convention used by plugins to look for proto files.

### testing
- grpc also uses HTTP (albeit http2) -- hence for that also we send http requests to test ----> ./grpc-requests
address -> first bit - name of server, second bit - name of service, third bit - name of rpc method
it matches the definition present in .proto file
 ------> GRPC localhost:9001/BillingService/CreateBillingAccount

# Kafka
grpc - 1 to 1 microservice communication - when you need immediate response (sync)
kafka - 1 to many microservice communication - dont need immediate response (async)

producer - patient-service - produces patient event, which is consumed by analytics-service

# API gateway
- Acts as single entrypoint for all clients to interact with multiple microservices.
- routes requests to microservices -> hides internal addresses
- also handles authentication, authorization, monitoring, rate lim, caching centrally that are common to all microservices

Useful as addresses / ports may change over time then need to manually make all changes in all microservices & clients
Scalability challenge as when multiple microservices added, client has to update their info about all of them. 
Security concern as we are exposing port of microservice to outside world.

### Note
after containerizing api gateway, we removed the external bind port of patient-service so that only way to reach it is
through api gateway

# AUTHentication
## process
- login by making auth request to auth service using api gateway
- it checks by comparing the username and pass with database
- if correct, it returns a jwt token to client
- the token given for subseq requests to prove who client is, api gateway checks validity with auth service

## Gateway involvement
- gateway will intercept the http requests using a filter and check if token is in proper format
- then it will call auth service using a webclient (http client) to validate the token
- dont want to apply token filter logic to auth service routes as they wont have token when the call these 
- if we apply the token filter to auth service routes, clients can never get token (never access the app)
- Hence we wil config(application.yml of api-gateway) such that filter is not applied to auth-service routes

## auth service docs using api-gateway
- at this point we already added swagger annotations, now we will be creating documentation, we just have to expose it to api gateway
- 