pipeline {
  agent any
  stages {
    stage('Gitクローン') {
      steps {
        git branch: 'master',
          credentialsId: 'github',
          url: 'https://github.com/ihcomega56/pingpong-server.git'
      }
    }

    stage('ビルド, テスト, アーティファクトのプッシュ') {

      steps {
        rtGradleResolver(
          id: 'pingpong-resolver',
          serverId: 'yokota',
          repo: 'jcenter'
        )
        rtGradleDeployer(
          id: 'pingpong-deployer',
          serverId: 'yokota',
          repo: 'pingpong-local'
        )
        rtBuildInfo(
          captureEnv: true,
          buildName: 'pingpong-server'
        )
        rtGradleRun(
          useWrapper: true,
          buildFile: 'build.gradle',
          tasks: 'clean test artifactoryPublish',
          resolverId: 'pingpong-resolver',
          deployerId: 'pingpong-deployer',
          // If the build name and build number are not set here, the current job name and number will be used:
          buildName: 'pingpong-server'
        )
      }
    }

    stage('ビルドインフォのプッシュ') {
      steps {
        rtPublishBuildInfo(
          serverId: 'yokota',
          buildName: 'pingpong-server'
        )
      }
    }

    stage('Xrayスキャン') {
      steps {
        xrayScan(
          serverId: 'yokota',
          buildName: 'pingpong-server',
          failBuild: true
        )
      }
    }

    stage('リリース') {
      steps {
        rtDownload(
          serverId: 'yokota',
          buildName: 'pingpong-server',
          failNoOp: true,
          spec: '''{
          "files": [
            {
              "pattern": "pingpong-local/com/example/pingpong-server/0.0.1-SNAPSHOT/pingpong-server-0.0.1-SNAPSHOT.jar"
            }
          ]
          }''',
        )
        pushToCloudFoundry(
          target: 'https://api.us-south.cf.cloud.ibm.com',
          organization: "$email",
          cloudSpace: 'dev',
          credentialsId: 'ibmcloud',
          manifestChoice: [appName: 'pingpong-server',
                           appPath: "com/example/pingpong-server/0.0.1-SNAPSHOT/pingpong-server-0.0.1-SNAPSHOT.jar",
                           value: "jenkinsConfig"],
          pluginTimeout: "600"
        )
      }
    }
  }
}