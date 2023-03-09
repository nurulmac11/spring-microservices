read -p "Build spring projects?: " -n 1 -r spring
echo    
read -p "Build docker?: " -n 1 -r docker
echo    

if [[ $spring =~ ^[Yy]$ ]]
then
  cd auth
  ./mvnw clean package -DskipTests 
  cd ../gateway
  ./mvnw clean package -DskipTests 
  cd ../transactions
  ./mvnw clean package -DskipTests 
fi

if [[ $docker =~ ^[Yy]$ ]]
then
  docker-compose up --build
else
  docker-compose start && docker-compose logs
fi

