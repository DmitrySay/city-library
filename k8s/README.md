### K8s. Simple example how to deploy database  and backend

Before run city backend in kubernetes you should create image city_database and city_service and push to your public
repository [username]/city_database and  [username]/city_service. 

docker login
docker build -t dsayushau/city_service:latest . 
docker image push dsayushau/city_service:latest

docker build -t dsayushau/city_database:latest . 
docker tag city_database:latest dsayushau/city_database:latest docker
image push dsayushau/city_database:latest

#### How to create database
`kubectl apply -f city-database-secret.yaml`
`kubectl apply -f city-database-deployment.yaml`

#### How to create Back-app

`kubectl apply -f city-database-configmap.yaml`
`kubectl apply -f city-backend-deployment.yaml`

#### How to check

`kubectl get deployment`

`kubectl get service`

`kubectl describe service city-database-service`
`kubectl describe service city-backend-deployment`

`kubectl get pods`

`kubectl describe pod [pod-name]`

`kubectl logs [pod-name]`
   
#### How to delete
`kubectl delete -f city-database-deployment.yaml`
`kubectl delete -f city-backend-deployment.yaml`
                   
all configurations from folder:

`kubectl delete -f k8s/`         

##### Video and topics
https://www.baeldung.com/ops/docker-push-image-to-private-repository

https://stackoverflow.com/questions/32726923/pulling-images-from-private-registry-in-kubernetes
https://kubernetes.io/docs/tasks/configure-pod-container/pull-image-private-registry/

https://habr.com/ru/company/otus/blog/527394/

video https://www.youtube.com/watch?v=bhBSlnQcq2k&list=PLwvrYc43l1Mz_c-vV1yVyvFNFZPAleSNE

`echo -n "postgres_prod" | BASE64` cG9zdGdyZXNfcHJvZA== 
`echo -n "secret_prod" | BASE64` c2VjcmV0X3Byb2Q=
