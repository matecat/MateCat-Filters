# MateCat Filters Changelog



## 1.2.1 (2016-10-20)

* HTML subfiltering in XML now uses the same rules of the regular HTML filter
* Fixed: conversion of CSV and TSV files was not working properly

Changes we developed in Okapi and tested in this MateCat Filters version:

* Fixed: revisions detection in DOCX files sometimes produced false positives
* Fixed: some tags where not extracted from MIF files
* Fixed: default thresholds for IDML files where too conservative
* Fixed: some special inline tags in TTX where causing broken converted files



## 1.2.0 (2016-08-10)

* Improved Filters architecture to allow more customization; develop and plug your own filters to support files with particular features

Changes we developed in Okapi and tested in this MateCat Filters version:

* Excel files fix: visible cells merged with hidden ones were not extracted
* Proper kuten (asian period character with embedded trailing space) support



## 1.1.4 (2016-05-30)

* Win Converter failover: Filters can now use the Consul service discovery to get a list of available Win Converters
* Improved Win Converter communication protocol for speed and robustness
* Workaround for filenames charset bug: due to a bug in MIMEPull library, filenames are always read as ISO-8859-1. To send a UTF-8 filename you can now use the "fileName" POST parameter.
* Updated ICU4J library for better segmentation. Noticed improvements in the break behavior on periods not followed by space.

Changes we developed in Okapi and tested in this MateCat Filters version:

* Word, Excel and Powerpoint: improved the hyphens support
* Word, Excel and Powerpoint: improved &lt;mc:AlternateContent> support
* Excel: fix some corruptions in merge step
* Powerpoint: ignore some useless attributes producing tags
* Adobe FrameMaker: fix bug processing header of newer versions



## 1.1.3 (2016-03-14)

* Added ability to provide custom segmentation rules.
* Fixed "Get Original" button not working in GUI.
* Fixed URL segmentation rules causing near-infinite loop.
* Added segmentation rule for footnotes references.

Changes we developed in Okapi and tested in this MateCat Filters version:

* MS Office: 100% RTL support
* MS Office: improved handling of &lt;w:smartTag>
* Word: fixed buggy handling for &lt;w:fldChar> (long urls)
* Word: gracefully fail on revisions (tracked changes)
* Word: improved handling of &lt;w:SpecVanish>
* Word: fix error on double &lt;w:hyperlinks>
* Excel: hidden table headers no more extracted
* HTML: added RTL support
* Open/LibreOffice: doc properties no more extracted
* Open/LibreOffice Calc: cached formula results no more extracted



## 1.1.2 (2015-12-21)

* Big improvements to documentation, code structure and robustness

Changes we developed in Okapi and tested in this MateCat Filters version:

* MS Office: 80% RTL support
* MS Office: fixed issues on whitespaces handling
* MS Office: aggressive cleanup now strips away &lt;w:bCs> and &lt;w:szCs>
* Word: fixed some corruptions handling diagrams
* MS Office: fixed corruptions on tags with too many attributes



## 1.1.1 (2015-11-19)

First public release.