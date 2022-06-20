#!/bin/bash
# Get API key from https://build-artifactory.eng.vmware.com/ui/admin/artifactory/user_profile
JFROG_API_KEY=
JFROG_USER=
JFROG_ARTIFACTORY_URL=
VERSION="tweak-0.13"

curl -u${JFROG_API_KEY}:${JFROG_API_KEY} http://build-artifactory.eng.vmware.com/router/api/v1/system/ping

upload_artifact() {
    RELATIVE_PATH=$1
    ARTIFACT_NAME=$(basename $RELATIVE_PATH)
    cp ${RELATIVE_PATH}/pom.xml ${RELATIVE_PATH}/target/${ARTIFACT_NAME}-${VERSION}.pom
    curl -X PUT -u${JFROG_USER}:${JFROG_API_KEY} -T ${RELATIVE_PATH}/target/${ARTIFACT_NAME}-${VERSION}.jar ${JFROG_ARTIFACTORY_URL}/org/springframework/cloud/${ARTIFACT_NAME}/${VERSION}/
    curl -X PUT -u${JFROG_USER}:${JFROG_API_KEY} -T ${RELATIVE_PATH}/target/${ARTIFACT_NAME}-${VERSION}.pom ${JFROG_ARTIFACTORY_URL}/org/springframework/cloud/${ARTIFACT_NAME}/${VERSION}/
}

upload_pom() {
    RELATIVE_PATH=$1
    ARTIFACT_NAME=$(basename $RELATIVE_PATH)
    mkdir -p ${RELATIVE_PATH}/target
    cp ${RELATIVE_PATH}/pom.xml ${RELATIVE_PATH}/target/${ARTIFACT_NAME}-${VERSION}.pom
    curl -X PUT -u${JFROG_USER}:${JFROG_API_KEY} -T ${RELATIVE_PATH}/target/${ARTIFACT_NAME}-${VERSION}.pom ${JFROG_ARTIFACTORY_URL}/org/springframework/cloud/${ARTIFACT_NAME}/${VERSION}/
}

upload_artifact ./specs/spring-cloud-contract-spec
upload_artifact ./specs/spring-cloud-contract-spec-java
upload_artifact ./specs/spring-cloud-contract-spec-groovy
upload_artifact ./spring-cloud-contract-stub-runner
upload_artifact ./spring-cloud-contract-tools/spring-cloud-contract-converters
upload_artifact ./spring-cloud-contract-tools/spring-cloud-contract-maven-plugin
upload_artifact ./spring-cloud-contract-shade
upload_artifact ./spring-cloud-contract-verifier
upload_artifact ./spring-cloud-contract-wiremock

upload_pom ./spring-cloud-contract-tools
upload_pom ./spring-cloud-contract-dependencies
mkdir -p ./target/spring-cloud-contract-parent && cp pom.xml ./target/spring-cloud-contract-parent
upload_pom ./target/spring-cloud-contract-parent


# ===================#
# ALTERNATIVE APPROACH
# ===================#

# mvn  deploy -DaltDeploymentRepository=spring::default::https://build-artifactory.eng.vmware.com/artifactory/vcps-maven-local -DskipTests
# mvn  deploy -Durl=https://build-artifactory.eng.vmware.com/artifactory/iris-maven-local -DskipTests -Dmaven.install.skip=true -DrepositoryId=jfrog -Pjfrog -DprofileIdEnabled=true

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

#```
# <distributionManagement>
# 		<repository>
# 			<id>jfrog</id>
# 			<name>build-artifactory-releases</name>
# 			<url>https://build-artifactory.eng.vmware.com/artifactory/iris-maven-local</url>
# 		</repository>
# </distributionManagement>
#```
