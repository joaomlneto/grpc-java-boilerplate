[![Build Status](https://travis-ci.com/joaomlneto/grpc-java-boilerplate.svg?token=QsVQsaqyQNrgjyTyzV4W&branch=master)](https://travis-ci.com/joaomlneto/grpc-java-boilerplate)

# grpc-java-boilerplate
Proper template with GRPC integration on Maven

- `service-interface` contains the GRPC specification file, which is
automatically compiled into Java files
- `service-handler` contains an implementation of the service interface
- `server` starts a simple server on port 9090 backed by the `service-handler` interface
- `client` is a simple Java client that can connect to `server`
