pipeline {
    agent {
        // Equivalent to "docker build -f Dockerfile".
        dockerfile {
            filename './scripts/Dockerfile'
            label 'asylum-worker' // Which jenkins workers to run this on (workers labelled with docker)
            args '--shm-size 1G  -v /var/lib/jenkins/.gitconfig:/home/jenkins/.gitconfig -v /var/lib/jenkins/.gradle:/home/jenkins/.gradle' // Set shm high due to chromium needing a lot
        }
    }

    environment {
        SLACK_CHANNEL = "#asylum-alert-qa"
    }

    parameters {
        gitParameter branchFilter: 'origin/(.*)', defaultValue: 'master', name: 'BRANCH', type: 'PT_BRANCH'
    }

    stages {
        stage('Checkout branch') {
            steps {
                script{
                    try {
                        sh "git checkout --track origin/${params.BRANCH}"
                    } catch (err) {
                        sh "git checkout ${params.BRANCH}"
                    } 
                }
            }
        }
        stage('Build/Test') {
            steps {
                gradlew('clean', 'build', 'publish')
                script {
                    currentBuild.displayName = currentBuild.displayName+" "+getCurrentVersion();
                }
            }
        }
        stage('Deploy in QA') {
            steps {
               script {
                    opsworks.deployPedroApp(
                        java_application_name: 'starter-service',
                        java_build: getCurrentVersion(),
                        stackName: 'stack-name',
                        layerShortName: 'starter-service',
                        appName: 'starter-service'
                    )
                }
            }
        }
        stage('QA Tests') {
            steps {
                dir('starter-service-qatests') {
                    git credentialsId: 'Github_jenkins', url: 'git@github.com:NewEngen/starter-service-qatests.git'
                    gradlew('clean', 'test', '--continue')
                    junit 'build/test-results/test/*.xml'
                }
            }
        }
    }
    post {
        always {
            dir('starter-service-qatests') {
                junit 'build/test-results/test/*.xml'
            }
            publishTestHtmlReport("starter-service-qatests")
            deleteDir() // Cleanup old gradlew cruft
        }
        failure {
            slackSendSummary channel: env.SLACK_CHANNEL
        }
    }
}

def getCurrentVersion() {
    return sh(returnStdout: true, script: './gradlew currentVersion | grep Project | cut -d: -f2').trim()
}
