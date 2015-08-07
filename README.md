# Matecat Converter

Matecat Converter is a free open-source [Matecat](https://www.matecat.com) module which offers file conversions from multiple formats to Xliff (.xlf). Even though its usage is not mandatory, it substancially improves the user experience of the site. 

## Main features

Matecat Converter is a server that offers a REST interface for the tasks of:

* Conversion of a file into a Xliff
* Extraction of the original file from a Xliff
* Generation of the translated (derived) file from a Xliff

In addition to that, it also frees Matecat of the following responsabilities:

* Encoding detection / handling
* Intermediate file conversions
* Interaction with the [Okapi Framework](http://www.opentag.com/okapi/wiki/)


## Test client
![Client preview](http://i59.tinypic.com/2nba9l0.png)
Once the server is running, the easiest way to test it is using the embedded client. It can be accessed at `http://localhost:{SERVER-PORT}`. By default, the address is `http://localhost:8082`. 

_Note: whereas the client it has been proven in several browser, we do recommend the usage of Google Chrome._

## Changelog

__Version 0.2.2:__
* HTML and XML trimming prevented
* Improved logging
* A bug affecting XML conversions has been fixed
* New client within the server

__Version 0.2.1:__
* Testing endpoint added, accessible at /test. It can be used to test if the server is working.
* Even if the server is running on Windows, the Converter reads the .xlf's as UTF-8 (fixing some encoding bugs).

__Version 0.2:__
* Logging messages improved. In addition to that, a more extensive log is stored in 'server.log' file.
* PHP test client added.
* Minor changes preventing memory leaks.
* Better handling of .xlf files with un-normalized structure.
* Okapi framework updated to its latest 0.28 snapshot.
* Improved HTML charset detection.
* Storage configuration added: now it's possible to specify the temporal folder, as well as if the folders should be deleted after its competition or not.
