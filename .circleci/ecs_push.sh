#!/usr/bin/env bash
TAG=6
./gradlew bootRepackage buildDocker --no-daemon
aws ecr get-login --no-include-email --region us-east-1 --profile balanceposition | bash
docker build -t balanceposition .
docker tag balanceposition:latest 903639670790.dkr.ecr.us-east-1.amazonaws.com/balanceposition:${TAG}
docker push 903639670790.dkr.ecr.us-east-1.amazonaws.com/balanceposition:${TAG}
