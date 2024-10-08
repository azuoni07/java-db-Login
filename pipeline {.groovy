pipeline {
    agent any

    tools {
        // Install the Maven version configured as "M3" and add it to the path.
        maven "Maven"
    }

    stages {
        stage('1. Fetch the code') {
            steps {
                // Get some code from a GitHub repository
                git branch: 'release', credentialsId: 'github-cred', url: 'https://github.com/azuoni07/java-db-Login.git'

            }
        }
        stage('2. Build the code') {
            steps {

                // Run Maven on a Unix agent
                sh 'mv clean package'
            
            }
            post {
            //If Maven was able to run the tests, even if some of the test
            //failed, record the test results and archive the jar file
            success {
                deploy adapters: [tomcat9(credentialsId: 'tomcat-admin-user', path: '', url: 'http://172.31.88.90:8080')], contextPath: '/stage-login', onFailure: false, war: '"**/*.war"'
            }
        }
    }
}
}
