#Commands to generate and deploy endpoints, then deploy code:
	mvn endpoints-framework:openApiDocs -e
	gcloud endpoints services deploy target/openapi-docs/openapi.json
	mvn appengine:update

#Command to upload, no-cache and make publicly readable
	gsutil -h "Cache-Control:no-cache" cp bucket/index.html gs://list-demo/index.html

#Bring front end up locally
	sudo live-server --port=80