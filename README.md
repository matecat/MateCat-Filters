<p align="center"><img src="http://i.imgur.com/kCb5hS1.png"></p>

# Convert any file to XLIFF and back

With **MateCat Filters** you can easily extract all the translatable contents from any file format into a convenient XLIFF file.

After you have translated your XLIFF, use Filters again to get back a completely translated file in the original format, with perfectly preserved formatting.

Fast, reliable and scalable, running everyday inside [MateCat](https://www.matecat.com/), the popular open-source CAT tool.

Test it right now on [matecat.com](https://www.matecat.com/): when you upload your file and later download it translated, 
you are using Filters.

## Main features

#### Plenty of supported formats

Among others, MateCat Filters fully supports Microsoft Office formats (legacy ones too), Open Office, PDF, hypertext, and even images of scanned documents thanks to automatic OCR (using the proper external library). See the [full list](https://github.com/matecat/MateCat-Filters/wiki/Supported-file-formats) in the Wiki.

#### Advanced segmentation

Filters uses segmentation rules defined by the Unicode consortium, plus another set of rules specifically designed for CAT Tools. This is why Filters can properly split sentences even in uncommon languages like Mongolian.

#### Easy integration, fast and scalable

Written in Java using Jetty and Okapi Framework. Efficient, multi-threaded and stateless.
The interface is super simple, with only 2 REST endpoints to use! See the [API documentation](https://github.com/matecat/MateCat-Filters/wiki/API-documentation) in the wiki.

## Getting started

Navigate the [wiki](https://github.com/matecat/MateCat-Filters/wiki/) to learn how to [build your instance](https://github.com/matecat/MateCat-Filters/wiki/Build-and-run) and [use the API](https://github.com/matecat/MateCat-Filters/wiki/API-documentation).

Consider using the [hosted API](https://market.mashape.com/translated/MateCat-filters) for both testing and production purposes. 
You can use it for free or pay a monthly fee for big conversion volumes. 
Main advantages:

* **Istantly ready to use**, zero installation / configuration.
* **Runs in MateCat's infrastructure**, with instances constantly monitored, optimized, and updated.
* **Transparent versioning**: automatically downgrades when you try to convert a XLIFF created with an older version of MateCat Filters.
* **Commercial dependencies included**: you don't need to buy licenses for the commercial software MateCat Filter uses to support OCR, PDF and legacy MS Office formats.
