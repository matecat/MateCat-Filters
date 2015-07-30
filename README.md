# Matecat Converter

Matecat Converter is a free open-source [Matecat](https://www.matecat.com) module which offers file conversions from multiple formats to Xliff (.xlf). Even though its usage is not mandatory, it substancially improves the user experience of the site. 

_Note: if your purpose is just to use this module within Matecat, and not to modify it, you can directly connect to our server (it's free):_ __http://[TODO SERVER ADDRESS: TODO SERVER PORT]__

## Main features

Matecat Converter is a server that offers a REST interface for the tasks of:

* Conversion of a file into a Xliff
* Extraction of the original file from a Xliff
* Generation of the translated (derived) file from a Xliff

In addition to that, it also frees Matecat of the following responsabilities:

* Encoding detection / handling
* Intermediate file conversions
* Interaction with the [Okapi Framework](http://www.opentag.com/okapi/wiki/)



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
Execute the following commands from the terminal:

```
chmod 755 build.sh
./build.sh
```
#### WINDOWS
Execute the following commands from the CMD:

```
mvn clean
mvn -DskipTests=true package
```

## Server Execution
To run the server go to the folder where the building has taken place, by default `/target/jar`, and follow the instructions correspondig to your operating system. The execution of the server requires Java 8.

#### UNIX
Execute the following command from the terminal:

```
./server.sh
```

#### Windows
Execute the following command from the CMD:

```
java -jar [NAME OF THE JAR INCLUDING EXTENSION]
```

