pipeline {
  agent any

  stages {
    stage('Build') {
      steps {
        sh 'mvn -v'
      }
    }
  }

  post {
    always {
      sh 'echo "post steps now have a workspace"'
    }
  }
}
