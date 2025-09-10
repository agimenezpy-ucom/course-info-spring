pipeline {
    agent {
        docker {
            image 'maven:3.9.9-eclipse-temurin-21'
            args '-v $HOME/.m2:/root/.m2'
        }
    }

    stages {

        stage('Build') {
            steps {
                
                // Run Maven on a Unix agent.
                sh "mvn -Dmaven.test.failure.ignore=true clean package"
                
                docker.build('.')
            }

            post {
                // If Maven was able to run the tests, even if some of the test
                // failed, record the test results and archive the jar file.
                success {
                    junit '**/target/surefire-reports/TEST-*.xml'
                    jacoco()
                    archiveArtifacts '**/target/*.jar'
                }

                always {
                    emailext body: "${BUILD_URL}\n${currentBuild.absoluteUrl}",
                    to: "demo@example.com",
                    recipientProviders: [contributor()],
                    subject: "${currentBuild.currentResult}: Job ${JOB_NAME} ${BUILD_NUMBER}"
                } 
            }
        }

        stage('Publish') {
            steps {
                docker.build('.')
            }
        }    
    }
}
