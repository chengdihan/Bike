pipeline {
    agent {
        dockerfile {
            filename './scripts/Dockerfile'
            label 'asylum-worker' // Which jenkins workers to run this on (workers labelled with docker)
            args '--shm-size 1G  -v /var/lib/jenkins/.gitconfig:/home/jenkins/.gitconfig -v /var/lib/jenkins/.gradle:/home/jenkins/.gradle' // Set shm high due to chromium needing a lot
        }
    }

    environment {
        SLACK_CHANNEL = "#asylum-alert-qa"
    }

    stages {
        stage('Build/Test') {
            steps {
                gradlew('clean', 'build', 'jacocoTestReport')
            }
        }
    }

    post {
        always {
            junit 'build/test-results/test/*.xml'
            publishTestHtmlReport(".")
            publishJacocoHtmlReport(".")
            deleteDir() // Cleanup old gradlew cruft
        }

        failure {
            slackSendSummary channel: env.SLACK_CHANNEL
        }
    }
}
