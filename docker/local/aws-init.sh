#!/bin/bash
set -x

echo "Starting S3 Bucket Creation..."

docker-compose exec s3-demo-localstack awslocal s3api create-bucket --bucket s3-demo-bucket

echo "Listing Bucket:"

docker-compose exec s3-demo-localstack awslocal s3api list-buckets

echo "S3 Bucket Creation Completed."

set +x