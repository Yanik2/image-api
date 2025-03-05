#!/bin/bash
echo "Parsing envs..."

export $(jq -r 'to_entries | .[] | .key + "=" + .value' /var/secrets/image-api-secrets.json)

echo "Starting spring boot"

exec java -jar app.jar
