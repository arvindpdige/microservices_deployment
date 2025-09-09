pipeline {
    agent  any   
	
	tools {
		git 'Default'
		maven 'maven3.9.1'			
		jdk 'jdk17'
        nodejs 'nodejs18'
        gradle 'gradle8'
	}
	parameters {
		choice(name: 'BRANCH_NAME', choices:['dev', 'main'] )
		string(name: 'TAG', defaultValue: '')
	}
	options {
    buildDiscarder( logRotator( daysToKeepStr           : "30",
                                numToKeepStr            : "5",
                                artifactDaysToKeepStr   : "10",
                                artifactNumToKeepStr    : "30") )
    disableConcurrentBuilds()
    }
	environment {
	    SCANNER_HOME= tool 'sonar-scanner'
		BRANCH_URL = 'https://github.com/arvindpdige/E-Commerse-Project.git'
        REGISTRY_URL = 'https://hub.docker.com/repositories/arvindpdige'
	}
    stages {
        stage('Clean Workspace') {
            steps {
                cleanWs()
                }
            }			
		stage('Git Checkout') {			
			steps {
				script {
				git branch: "${params.BRANCH_NAME}", credentialsId: 'git-token', url: "${BRANCH_URL}"
				}
            }
        }
        stage('Sonar-Analysis'){
	        steps{
	            withSonarQubeEnv(credentialsId: 'Sq_Token', installationName: 'SonarQube') {
                    sh ''' $SCANNER_HOME/bin/sonar-scanner -Dsonar.projectName=e-commerce -Dsonar.projectKey=e-commerce-key \
                        -Dsonar.java.binaries=. '''
                }
	        }
	    }
	    stage('Sonar-QG'){
	        steps{
	            script {
	                waitForQualityGate abortPipeline: false, credentialsId: 'Sq_Token'
	           }
            }
	   }
        stage('Cart_Build') {
            steps {
                dir('cart-cna-microservice') {
                    script{
                    withDockerRegistry(credentialsId: 'dockerhub') {
                        sh '''
                            docker build -t arvindpdige/cart:"${TAG}" .
                            trivy image --format json -o cart_vul.json arvindpdige/cart:"${TAG}"                            
                            docker push arvindpdige/cart:"${TAG}"
                        '''
                        }
                        archiveArtifacts artifacts: 'cart_vul.json', fingerprint: true
                    }
                }
            }   
        }
        stage('Products_Build') {
            steps {
                dir('products-cna-microservice') {
                    script{
                    withDockerRegistry(credentialsId: 'dockerhub') {
                        sh '''
                            docker build -t arvindpdige/products:"${TAG}" .
                            trivy image --format json -o products_vul.json arvindpdige/products:"${TAG}" 
                            docker push arvindpdige/products:"${TAG}"
                        '''
                        }
                        archiveArtifacts artifacts: 'products_vul.json', fingerprint: true
                    }
                }   
            }
        }
        stage('Store_Build') {
            steps {
                dir('store-ui') {
                    script{
                    withDockerRegistry(credentialsId: 'dockerhub') {
                        sh '''
                            docker build -t arvindpdige/store:"${TAG}" .
                            trivy image --format json -o store_vul.json arvindpdige/store:"${TAG}" 
                            docker push arvindpdige/store:"${TAG}"
                        '''
                        }
                        archiveArtifacts artifacts: 'store_vul.json', fingerprint: true
                    }
                }   
            }
        }
        stage('Users_Build') {
            steps {
                dir('users-cna-microservice') {
                    script{
                    withDockerRegistry(credentialsId: 'dockerhub') {
                        sh '''
                            docker build -t arvindpdige/users:"${TAG}" .
                            trivy image --format json -o users_vul.json arvindpdige/users:"${TAG}" 
                            docker push arvindpdige/users:"${TAG}"
                        '''
                        }
                        archiveArtifacts artifacts: 'users_vul.json', fingerprint: true
                    }
                }   
            }
        }
        stage('Search_Build') {
            steps {
                dir('search-cna-microservice') {
                    script{
                    withDockerRegistry(credentialsId: 'dockerhub') {
                        sh '''
                            docker build -t arvindpdige/search:"${TAG}" .
                            trivy image --format json -o search_vul.json arvindpdige/search:"${TAG}" 
                            docker push arvindpdige/search:"${TAG}"
                        '''
                        }
                        archiveArtifacts artifacts: 'search_vul.json', fingerprint: true
                    }
                }   
            }
        }
		stage('Deploy_Environment') {			
			steps {
				script {
                    if (params.BRANCH_NAME == 'dev') { 
                        sh '''
                            echo "Stopping all containers"
                            docker-compose down -v || true
                            echo "Deploying to Development Environment"
                            # docker network create ecom-shop --driver bridge
                            TAG=${TAG} docker-compose up -d 
                        '''
                    }
                    else if (params.BRANCH_NAME == 'main') {
                        dir('k8s') {
                            withCredentials([file(credentialsId: 'kubeconfig', variable: 'KUBECONFIG')]) {
                                sh '''
                                    echo "Stopping all containers in K8s Production Environment"
                                    kubectl delete -k . || true
                                    echo "Deploying to K8s Production Environment"
                                    kustomize build . | sed "s|TAG|${TAG}|g" | kubectl apply -f -
                                '''
                            }
                        }
                    }
                    else {
                        error "Invalid branch name: ${params.BRANCH_NAME}"
                    }
                }
            }
        }
    }
	// post {
        // success {
        //     echo "success email sending disabled"
        //     //emailext to: "${EMAIL_RECIPIENT}", subject: "${EMAIL_SUBJECT_SUCCESS}", body: "${EMAIL_BODY_SUCCESS}"
        // }
        // failure {
        //     // emailext to: "${EMAIL_RECIPIENT}", subject: "${EMAIL_SUBJECT_FAILURE}", body: "${EMAIL_BODY_FAILURE}"
        // }
    // }
}
