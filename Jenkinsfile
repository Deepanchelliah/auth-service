pipeline {
  agent any

  stages {
    stage('Build') {
      agent {
        docker {
          image 'maven:3.9.6-eclipse-temurin-17'
          args '-v /var/run/docker.sock:/var/run/docker.sock'
        }
      }
      steps {
        sh 'mvn -v'
        sh 'mvn clean test'
      }
    }
  }
}
