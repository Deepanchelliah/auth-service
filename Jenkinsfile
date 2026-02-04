pipeline {
  agent any

  environment {
    // Use Jenkins Credentials (Username/Password type)
    API_CREDS = credentials('my-api-creds-id')
    USERNAME = "${API_CREDS_USR}"
    PASSWORD = "${API_CREDS_PSW}"
  }

  stages {
    stage('Checkout automation') {
      steps {
        checkout scm
      }
    }

    stage('Checkout auth-service') {
      steps {
        dir('auth-service') {
          git url: 'https://github.com/Deepanchelliah/auth-service.git', branch: 'main'
        }
      }
    }

    stage('Build & run dependencies + tests') {
      steps {
        sh '''
          set -eux

          # Build and start auth-service
          docker compose -f docker-compose.ci.yml build auth-service
          docker compose -f docker-compose.ci.yml up -d auth-service

          # Run tests (one-shot)
          docker compose -f docker-compose.ci.yml run --rm \
            -e USERNAME="$USERNAME" -e PASSWORD="$PASSWORD" \
            automation-tests
        '''
      }
    }
  }

  post {
    always {
      sh '''
        docker compose -f docker-compose.ci.yml logs --no-color || true
        docker compose -f docker-compose.ci.yml down -v || true
      '''
    }
  }
}
