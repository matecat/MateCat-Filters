![MateCat Filters Logo]("http://i.imgur.com/J8FSuWi.png")


# New hosted service, premier enterprise solutions, open-source no longer supported

**MateCat Filters** have been used by many corporations in mission-critical production environments. Unfortunately we can no longer provide this project for free given its maintenance costs. For this reason, as of **June 2020**, we decided to:
- put the repository in read only mode (archived)
- still offer the filters via the [hosted API](https://rapidapi.com/translated/api/matecat-filters/)
- support enterprises with custom solutions
- hire full time engineers dedicated to the project.

While we are sad of terminating the open source project, we believe this will offer a better service for all users.

The source herein corresponds to version _1.2.5_ based on Okapi version _M36_.

## Convert any file to XLIFF and back

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

#### Hosted API
- **Istantly ready to use**, zero installation / configuration.
- **Runs in MateCat's infrastructure**, with instances constantly monitored, optimized, and updated.
- **Transparent versioning**: automatically downgrades when you try to convert a XLIFF created with an older version of MateCat Filters.
- **Commercial dependencies included**: you don't need to buy licenses for the commercial software MateCat Filter uses to support OCR, PDF and legacy MS Office formats.


## Getting started

Navigate the [wiki](https://github.com/matecat/MateCat-Filters/wiki/) to learn how to [use the API](https://github.com/matecat/MateCat-Filters/wiki/API-documentation).
