pipeline {
  agent {
    node {
      label 'maven'
    }
  }
  stages {
    stage('build ') {
      agent none
      steps {
        container('maven') {
          sh ''' mvn clean install
'''
        }

      }
    }

    stage('push') {
      agent none
      steps {
        container('maven') {
          withCredentials([usernamePassword(credentialsId : 'nexus-fanshuai' ,passwordVariable : 'PASSWORD' ,usernameVariable : 'USER' ,)]) {
            sh '''echo "$PASSWORD" | docker login --username $USER --password-stdin nexus-docker.galaksiodatanubo.work
# ruoyi-auth
 docker build -t $REGISTRY/uuc/uuc-auth:${TAG} -f docker/uuc/auth/dockerfile .
 docker push $REGISTRY/uuc/uuc-auth:${TAG}
 docker rmi $REGISTRY/uuc/uuc-auth:${TAG}
# ruoyi-gateway
 docker build -t ${REGISTRY}/uuc/uuc-gateway:${TAG} -f docker/uuc/gateway/dockerfile .
 docker push ${REGISTRY}/uuc/uuc-gateway:${TAG}
 docker rmi ${REGISTRY}/uuc/uuc-gateway:${TAG}
# ruoyi-system
 docker build -t ${REGISTRY}/uuc/uuc-system:${TAG} -f docker/uuc/modules/system/dockerfile .
 docker push ${REGISTRY}/uuc/uuc-system:${TAG}
 docker rmi ${REGISTRY}/uuc/uuc-system:${TAG}
# ruoyi-xxljob
 docker build -t ${REGISTRY}/uuc/uuc-xxljob:${TAG} -f docker/uuc/modules/xxljob/dockerfile .
 docker push ${REGISTRY}/uuc/uuc-xxljob:${TAG}
 docker rmi ${REGISTRY}/uuc/uuc-xxljob:${TAG}
# ruoyi-file
 docker build -t ${REGISTRY}/uuc/uuc-file:${TAG} -f docker/uuc/modules/file/dockerfile .
 docker push ${REGISTRY}/uuc/uuc-file:${TAG}
 docker rmi ${REGISTRY}/uuc/uuc-file:${TAG}
# ruoyi-resource-provider
 docker build -t ${REGISTRY}/uuc/uuc-resource-provider:${TAG} -f docker/uuc/modules/resource-provider/dockerfile .
 docker push ${REGISTRY}/uuc/uuc-resource-provider:${TAG}
 docker rmi ${REGISTRY}/uuc/uuc-resource-provider:${TAG}
'''
          }

        }
      }
    }

    stage('deploy') {
      agent none
      steps {
        container('maven') {
          withCredentials([kubeconfigFile(credentialsId : 'kubeconfig' ,variable : 'KUBECONFIG' ,)]) {
            sh '''helm3 upgrade uuc-system --set namespace=$NAMESPACE -n $NAMESPACE --install --set image.repository=${REGISTRY}/uuc/uuc-system,image.tag=${TAG},containerPort=9201,service.port=9201,fullnameOverride=uuc-system ./charts
helm3 upgrade uuc-auth --set namespace=$NAMESPACE  -n $NAMESPACE --install --set image.repository=${REGISTRY}/uuc/uuc-auth,image.tag=${TAG},containerPort=8200,service.port=8200,fullnameOverride=uuc-auth ./charts
helm3 upgrade uuc-gateway --set namespace=$NAMESPACE -n $NAMESPACE --install --set image.repository=${REGISTRY}/uuc/uuc-gateway,image.tag=${TAG},containerPort=8080,service.port=8080,fullnameOverride=uuc-gateway ./charts
helm3 upgrade uuc-xxljob --set namespace=$NAMESPACE -n $NAMESPACE --install --set image.repository=${REGISTRY}/uuc/uuc-xxljob,image.tag=${TAG},containerPort=9511,service.port=9511,fullnameOverride=uuc-xxljob ./charts
helm3 upgrade uuc-file --set namespace=$NAMESPACE -n $NAMESPACE --install --set image.repository=${REGISTRY}/uuc/uuc-file,image.tag=${TAG},containerPort=9300,service.port=9300,fullnameOverride=uuc-file ./charts
helm3 upgrade uuc-resource-provider --set namespace=$NAMESPACE -n $NAMESPACE --install --set image.repository=${REGISTRY}/uuc/uuc-resource-provider,image.tag=${TAG},containerPort=9333,service.port=9333,fullnameOverride=uuc-resource-provider ./charts
envsubst < externalName.yaml | kubectl apply -f -
'''
          }

        }

      }
    }

  }
  environment {
    REGISTRY = 'nexus-docker.galaksiodatanubo.work/repository/galaksio'
    NAMESPACE = 'dev-ump'
    TAG = createVersion()
  }
}
def createVersion() {
    return "${env.BRANCH_NAME}_" + new Date().format('yyyyMMddHHmmss') + "_${env.BUILD_ID}"
}
