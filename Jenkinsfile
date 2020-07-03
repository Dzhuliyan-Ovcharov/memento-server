pipeline {
    agent any

    properties([
      parameters([
        string(name: 'Branch:', defaultValue: 'master', description: 'The target environment', )
       ])
    ])

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
        }
}
