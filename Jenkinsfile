def version = "${env.BUILD_NUMBER}"
def REPONAME = "${scm.getUserRemoteConfigs()[0].getUrl()}"

node {
    
    stage('Clone sources') {
      git(
           url: "${REPONAME}",
           credentialsId: 'username***REMOVED***-github-automation-uk-gov-dft',
           branch: "${BRANCH_NAME}"
        )
     }

    stage ('Gradle build') {
        // Set Environment Vairable if the CI env variable is set.
        script {
            env.SPRING_APPLICATION_JSON = '{"spring":{"datasource":{"url":"jdbc:postgresql://postgresql:5432/bb_dev?currentSchema=applicationmanagement"}}}'
        }
        try {
            sh './gradlew clean build bootJar createDatabaseSchemaZip artifactoryPublish artifactoryDeploy --refresh-dependencies'
        }
        finally {
            junit '**/TEST*.xml'
        }
    }

    stage('SonarQube analysis') {
        withSonarQubeEnv('sonarqube') {
            def ver = readFile('VERSION').trim()
            echo "Version: " + ver
            // requires SonarQube Scanner for Gradle 2.1+
            // It's important to add --info because of SONARJNKNS-281
            sh "./gradlew --info sonarqube -Dsonar.projectName=applicationmanagement-service -Dsonar.projectVersion=${ver} -Dsonar.branch=${BRANCH_NAME}"
        }
    }

    stage("Quality Gate") {
        timeout(time: 5, unit: 'MINUTES') {
            def qg = waitForQualityGate()
            if (qg.status != 'OK') {
                error "Pipeline aborted due to quality gate failure: ${qg.status}"
            }
        }
    }
}
