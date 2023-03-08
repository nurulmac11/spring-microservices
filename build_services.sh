read -p "Build spring projects?: " -n 1 -r
echo    
if [[ $REPLY =~ ^[Yy]$ ]]
then
  cd auth
  ./mvnw clean package -DskipTests 
  cd ../gateway
  ./mvnw clean package -DskipTests 
  cd ../transactions
  ./mvnw clean package -DskipTests 
fi

read -p "Build docker?: " -n 1 -r
echo    
if [[ $REPLY =~ ^[Yy]$ ]]
then
  docker-compose up --build
fi

