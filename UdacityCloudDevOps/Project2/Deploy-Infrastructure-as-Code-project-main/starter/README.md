# CD12352 - Infrastructure as Code Project Solution

# UdagramProject

## Spin up instructions

###To spin up network infra
./deploy.sh deploy udagram-network network.yml network-parameters.json
###To spin up application infra
./deploy.sh deploy udagram-app udagram.yml udagram-parameters.json

## Tear down instructions

###To tear down network infra
./deploy.sh delete udagram-network
###To tear down application infra
./deploy.sh delete udagram-app

## Other considerations

WEBSITE URL: udagra-LoadB-eVDjG9Lwz7nW-62031107.us-east-1.elb.amazonaws.com
