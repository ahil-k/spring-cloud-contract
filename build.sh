#!/bin/bash
# pushd specs/spring-cloud-contract-spec-java
# mvn clean install -DskipTests -Dmaven.javadoc.skip=true
# popd

# pushd spring-cloud-contract-verifier
# mvn clean install -DskipTests -Dmaven.javadoc.skip=true
# popd

# pushd spring-cloud-contract-tools/spring-cloud-contract-maven-plugin
# mvn clean install -DskipTests -Dmaven.javadoc.skip=true
# popd

mvn versions:set -DnewVersion=3.1.0-tweak -DprocessAllModules -DgenerateBackupPoms=false
mvn clean install -DskipTests -Dmaven.javadoc.skip=true

# mvn  deploy -DaltDeploymentRepository=spring::default::https://build-artifactory.eng.vmware.com/artifactory/vcps-maven-local -DskipTests

# mvn  deploy -Durl=https://build-artifactory.eng.vmware.com/artifactory/iris-maven-local -DskipTests -Dmaven.install.skip=true -DrepositoryId=jfrog -Pjfrog -DprofileIdEnabled=true


# curl -ukahil:${JFROG_API_KEY} http://build-artifactory.eng.vmware.com/router/api/v1/system/ping

# ```
# <?xml version="1.0" encoding="UTF-8"?>
# <settings>
#     <servers>
#         <server>
#             <id>spring</id>
#             <username>kahil</username>
#             <password>${JFROG_API_KEY}</password>
#         </server>
#     </servers>
# </settings> 
# ``` 

# <distributionManagement>
# 		<repository>
# 			<id>jfrog</id>
# 			<name>build-artifactory-releases</name>
# 			<url>https://build-artifactory.eng.vmware.com/artifactory/iris-maven-local</url>
# 		</repository>
# </distributionManagement>