cd auth
./mvnw clean package -DskipTests 
cd ../gateway
./mvnw clean package -DskipTests 
cd ../transactions
./mvnw clean package -DskipTests 
read -p "Build docker? " -n 1 -r
echo    
if [[ $REPLY =~ ^[Yy]$ ]]
then
  docker-compose up --build
fi

