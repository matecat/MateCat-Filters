#!/bin/bash      
    
printf "\n"
echo "############################################################"
echo "####           MATECAT CONVERTER  BUILD SCRIPT         #####"  
echo "############################################################"
printf "\n"


printf "Executing maven... "
cd ..
mvn clean > /dev/null
mvn -DskipTests=true package > /dev/null
echo "done"

printf "Building scripts... "
cd target/jar/
FILENAME=$(find *.jar | head -1)
cat > server.sh << EOT
	#!/bin/bash
	java -jar $FILENAME 
EOT
echo "done"

printf "Making scripts executable... "
chmod 755 server.sh
echo "done"

printf "\nBuild completed at: "
echo $(pwd)

printf "\n"