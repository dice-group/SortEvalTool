.PHONY: client

run:
	mvn spring-boot:run

client:
	cd client && python -m SimpleHTTPServer 8081
