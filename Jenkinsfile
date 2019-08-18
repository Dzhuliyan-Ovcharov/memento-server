pipeline {
    agent any

        stages {
            stage ('Compile Stage') {
                steps {
                    withMaven(maven : 'maven_3_6_1') {
                        bat 'mvn clean compile'
                    }
                }
            }

            stage ('Test Stage') {
                steps {
                    withMaven(maven : 'maven_3_6_1') {
                        bat 'mvn test'
                    }
                }
            }

            stage ('Deployment Stage') {
                steps {
                    withMaven(maven : 'maven_3_6_1') {
                        bat 'mvn deploy'
                    }
                }
            }
        }
}