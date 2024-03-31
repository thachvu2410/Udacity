# Overview

This project deploys a Business Analysts API as a microservice to Kubernetes using AWS.

## Steps

### Repository and Docker Image Setup:

- Set up an AWS Elastic Container Registry (ECR) for Docker image storage.
- Configure AWS CodeBuild for automated image builds triggered by GitHub "push" events.

### AWS CodeBuild Pipeline:

- Establish a CodeBuild pipeline for continuous integration with necessary ECR roles.

### Amazon EKS Cluster Setup:

- Create an Amazon EKS cluster with a node group.
- Utilize the `config-eks.sh` script for local configuration updates.

### AWS EKS and VS Studio Integration:

- Initialize communication between AWS EKS service and VS Studio terminal.

### Helm Charts for Database Configuration:

- Configure the database using Helm Charts, seeding sample data with the `seed_data.sh` script.

### Kubernetes Deployment Configuration:

- Create deployment configurations (`deployment.yaml`, `service.yaml`, `configmap.yaml`, `secrets.yaml`) using the `deploy.sh` script.

### Database Service Configuration:

- Ensure the database service is accessible to the microservice.
- Set up directed logs to AWS CloudWatch for troubleshooting using the `cloudwatch.sh` script.

## Conclusion

These steps ensure automatic deployment and scalability of the microservice on Kubernetes using AWS services.