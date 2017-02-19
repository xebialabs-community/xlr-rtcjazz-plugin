
# xlr-rtcjazz-plugin

This document describes the functionality provided by the XL Release RTC Jazz Commit Trigger plugin.

See the **[XL Release Documentation](https://docs.xebialabs.com/xl-release/how-to/create-a-release-trigger.html)** for background information on XL Release concepts.

# CI status #

[![Build Status][xlr-rtcjazz-plugin-travis-image]][xlr-rtcjazz-plugin-travis-url]
[![Codacy Badge][xlr-rtcjazz-plugin-codacy-image]][xlr-rtcjazz-plugin-codacy-url]
[![Code Climate][xlr-rtcjazz-plugin-code-climate-image]][xlr-rtcjazz-plugin-code-climate-url]

[xlr-rtcjazz-plugin-travis-image]: https://travis-ci.org/xebialabs-community/xlr-rtcjazz-plugin.svg?branch=master
[xlr-rtcjazz-plugin-travis-url]: https://travis-ci.org/xebialabs-community/xlr-rtcjazz-plugin
[xlr-rtcjazz-plugin-codacy-image]: https://api.codacy.com/project/badge/Grade/b4db63bddfd54a14b167d346584cef76
[xlr-rtcjazz-plugin-codacy-url]: https://www.codacy.com/app/amitmohleji/xlr-rtcjazz-plugin
[xlr-rtcjazz-plugin-code-climate-image]: https://codeclimate.com/github/xebialabs-community/xlr-rtcjazz-plugin/badges/gpa.svg
[xlr-rtcjazz-plugin-code-climate-url]: https://codeclimate.com/github/xebialabs-community/xlr-rtcjazz-plugin


# Overview #

The XL Release RTC Jazz Commit trigger plugin provides a trigger which automatically generates new release when a new commit appears in the specified workspace or stream.

# Requirements #

* **Requirements**
	* **XL Release** 4.x+

# Installation #

* Place the plugin JAR file into your `SERVER_HOME/plugins` directory.
* Also take the rtc6.0.1jars.zip file , unzip it and place the content under `SERVER_HOME/lib` directory

* Restart the server  

# Usage #

First, you need to add an entry in the [Configuration](https://docs.xebialabs.com/xl-release/how-to/create-custom-configuration-types-in-xl-release.html#configuration-page) section with information on how to connect to your RTC Jazz Commit repository:

![Trigger Configuration](triggerConfig.png)

The next step is to configure a new [trigger](https://docs.xebialabs.com/xl-release/how-to/create-a-release-trigger.html) for your XL Release [template](https://docs.xebialabs.com/xl-release/how-to/create-a-release-template.html):

![Configuration ](triggerTemplate1.png)
![Configuration ](triggerTemplate2.png)