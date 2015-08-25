# Matecat Converter

## Interaction with the server
The server provides a simple REST API to execute to interact with it. 

#### Conversion of a file into a Xliff
* `[POST]` request to: `http://{SERVER IP ADDRESS}:{SERVER PORT}/convert/{source-language}/{target-language}`, sending the contents of the file as post parameter called `file` with `multipart/form-data` encoding.

* cURL call:
```
curl --form "file=@file.format" \ 
     "http://{SERVER-IP-ADDRESS}:{SERVER-PORT}/convert/{source-language}/{target-language}"
```

#### Extraction of the original file from a Xliff
* `[POST]` request to: `http://{SERVER IP ADDRESS}:{SERVER PORT}/original`, sending the contents of the xliff file as post parameter called `file` with `multipart/form-data` encoding.

* cURL call:
```
curl --form "file=@file.format" \ 
     "http://{SERVER-IP-ADDRESS}:{SERVER-PORT}/original"
```

#### Generation of the translated file from a Xliff
* `[POST]` request to: `http://{SERVER IP ADDRESS}:{SERVER PORT}/derived`, sending the contents of the xliff file as post parameter called `file` with `multipart/form-data` encoding.

* cURL call:
```
curl --form "file=@file.format" \ 
     "http://{SERVER-IP-ADDRESS}:{SERVER-PORT}/derived"
```

## Server Deployment
To build a runnable jar go to the `/deployment` folder, and follow the instructions correspondig to your operating system. The deployment requires Maven.

#### UNIX 
First, check that the building script is executable, by running the following command in the Terminal:
```
chmod +rx build
```
Then, you can execute the script doing double click on it, or by typing:
```
./build
```
#### WINDOWS
Execute the following commands from the CMD:

```
mvn -DskipTests=true clean compile package
```

## Server Execution
To run the server go to the folder where the building has taken place, by default `/target/package`, and follow the instructions correspondig to your operating system. The execution of the server requires Java 8.

_Note. If you build the server manually in Windows, you will not have a `server` nor a `server.bat` file. If this is the case, execute the following command in the Terminal / CMD:_

```
java -cp "resources;[NAME OF THE JAR INCLUDING EXTENSION]" com.matecat.converter.Main
```

#### UNIX
Double click on the file `server` (without extension)

#### Windows
Double click on the file `server.bat`.
