#
# THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESSED OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS
# FOR A PARTICULAR PURPOSE. THIS CODE AND INFORMATION ARE NOT SUPPORTED BY XEBIALABS.
#

###################################################################################
#  Name: RTC Jazz Poll Trigger
#
#  Description: Trigger new release when new version of artifact is published to the Artifactory repository
#  
###################################################################################
from snippets import CustomSnippet

if server is None:
    print "No RTC server provided."
    sys.exit(1)

rtcUrl = server['url']

credentials = CredentialsFallback(server, username, password).getCredentials()
content = None    

versionValue = CustomSnippet.getLatestChangesetUUID(rtcUrl, credentials['username'], credentials['password'], entityName)
# populate output variables
commitUUID = versionValue
print "Last commitUUID: " + commitUUID
triggerState = versionValue
 