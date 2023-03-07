Build services;
```shell
cd auth
./mvnw clean package -DskipTests

cd ../gateway
./mvnw clean package -DskipTests
cd ..
```

Then build docker compose;
```shell
docker-compose up --build
```