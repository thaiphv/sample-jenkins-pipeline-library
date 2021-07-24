def call(body) {
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    pipeline {
        stages {
            stage ('Init') {
                when {
                    expression { isReleasePipeline(config) }
                }
                agent { label config.node_label }
                steps {
                    ansiColor('xterm') {
                        initiate(config)
                    }
                }
            }
            stage ('Deploy') {
                when {
                    expression { isReleasePipeline(config) }
                }
                agent { label config.node_label }
                steps {
                    ansiColor('xterm') {
                        deploy(config)
                    }
                }
            }
            stage ('Run integration tests') {
                when {
                    expression { isReleasePipeline(config) }
                }
                agent { label config.node_label }
                steps {
                    ansiColor('xterm') {
                        runIntegrationTests(config)
                    }
                }
            }
            stage ('Run extra pipelines') {
                when {
                    expression { isReleasePipeline(config) }
                }
                agent any
                steps {
                    ansiColor('xterm') {
                        runExtraPipelines(config)
                    }
                }
            }
            stage ('Release') {
                when {
                    expression { isReleasePipeline(config) }
                }
                agent { label config.node_label }
                steps {
                    ansiColor('xterm') {
                        release(config)
                    }
                }
            }
        }
    }
}

def isReleasePipeline(config) {}

def initiate(config) {}

def deploy(config) {}

def runIntegrationTests(config) {}

def runExtraPipelines(config) {}

def release(config) {}
