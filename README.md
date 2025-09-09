# E-Commerse-Project
**Reference Repo**: 
https://github.com/venkataravuri/e-commerce-microservices-sample/tree/master


#### JAVA INSTALL
java --version 
sudo apt install openjdk-17-jre-headless

#### JENKINS SETUP
sudo wget -O /etc/apt/keyrings/jenkins-keyring.asc   

https://pkg.jenkins.io/debian-stable/jenkins.io-2023.key  

echo "deb [signed-by=/etc/apt/keyrings/jenkins-keyring.asc]"   

https://pkg.jenkins.io/debian-stable binary/ | sudo tee   /etc/apt/sources.list.d/jenkins.list > /dev/null 

sudo apt-get update 

sudo apt-get install jenkins 

systemctl cat jenkins 

sudo apt update 

sudo systemctl status jenkins 

**Open ports 8080**

sudo ufw allow 8080 

sudo ufw allow OpenSSH 

sudo ufw enable 

sudo ufw status 

#To get jenkins initial password 

sudo cat /var/lib/jenkins/secrets/initialAdminPassword

**Plugins to install** 

Eclipse Temurin installer	-> Java 

Config file provider, Pipeline maven integration  -> Maven 

SonarQube scanner 

Docker, Docker Pipeline,  

Kubernetes, Kubernetes cli, Kubernetes credentials, Kubernetes client api 


#### DOCKER SETUP
**To remove packages which can cause issue:** 

for pkg in docker.io docker-doc docker-compose docker-compose-v2 podman-docker containerd runc; do sudo apt-get remove $pkg; done

**Add Docker's official GPG key:** 

sudo apt-get update 

sudo apt-get install ca-certificates curl 

sudo install -m 0755 -d /etc/apt/keyrings 

sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc 

sudo chmod a+r /etc/apt/keyrings/docker.asc 

**Add the repository to Apt sources:** 

echo   "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu  \ 
$(. /etc/os-release && echo "${UBUNTU_CODENAME:-$VERSION_CODENAME}") stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null 

sudo apt-get update 

sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin 

systemctl status docker 

**Add users into docker group** 

sudo usermod -aG docker $USER 

sudo usermod -aG docker jenkins 

sudo systemctl restart docker 

**Install DOCKER-COMPOSE** 

sudo apt install docker-compose 

#### TRIVY SETUP

sudo apt-get update 

sudo apt-get install wget apt-transport-https gnupg lsb-release 

wget -qO - https://aquasecurity.github.io/trivy-repo/deb/public.key | sudo apt-key add - 
echo deb https://aquasecurity.github.io/trivy-repo/deb $(lsb_release -sc) main | sudo tee -a /etc/apt/sources.list.d/trivy.list 

sudo apt-get update 

sudo apt-get install trivy 

trivy -v 

#### SonarQube Setup

docker run -d --name sonarqube \
    -p 9000:9000 \
    -v sonarqube_data:/opt/sonarqube/data \
    -v sonarqube_extensions:/opt/sonarqube/extensions \
    -v sonarqube_logs:/opt/sonarqube/logs \
    sonarqube:lts-community

**To connect to SonarQube**

http://VM_Public_IP:9000

admin:admin

#### Nexus SETUP

docker run -d –name nexus -p 8081:8081 -v nexus-data:/nexus-data sonatype/nexus:3

**To connect to Nexus**

http://VM_Public_IP:8081

admin:Password_from_below_step

**To avail login password**

docker exec -it nexus /bin/bash

cat sonatype-work/nexus3/admin.password 

#### PROMETHEUS

wget https://github.com/prometheus/prometheus/releases/download/v3.5.0/prometheus-3.5.0.linux-amd64.tar.gz

wget https://github.com/prometheus/blackbox_exporter/releases/download/v0.27.0/blackbox_exporter-0.27.0.linux-amd64.tar.gz

nohup ./blackbox_exporter > blackbox.log 2>&1 &

nohup ./prometheus > prometheus.log 2>&1 &

**Add in prometheus.yaml **
  - job_name: 'blackbox'
    metrics_path: /probe
    params:
      module: [http_2xx]  # Look for a HTTP 200 response.
    static_configs:
      - targets:
        - http://prometheus.io    # Target to probe with http.
        - https://prometheus.io   # Target to probe with https.
        - http://example.com:8080 # Target to probe with http on port 8080.
    relabel_configs:
      - source_labels: [__address__]
        target_label: __param_target
      - source_labels: [__param_target]
        target_label: instance
      - target_label: __address__
        replacement: 127.0.0.1:9115     # IP or Hostname of instance running prometheus

Restart prometheus

#### Grafana
sudo apt-get install -y adduser libfontconfig1 musl

wget https://dl.grafana.com/enterprise/release/grafana-enterprise_8.2.3_amd64.deb

sudo dpkg -i grafana-enterprise_8.2.3_amd64.deb

sudo /bin/systemctl daemon-reload

sudo /bin/systemctl enable grafana-server

sudo /bin/systemctl start grafana-server

Login as admin admin

**Import Dashboard**

Add Data Sources -> Select prometheus -> Enter URL -> Save & Test

Google -> Search "blackbox dashboard id" -> Click 1st result -> Import the dashboard template -> Copy ID to clipboard

+ -> Import -> Enter ID -> Load 


#### TERRAFORM 
Setup AKS Cluster using Terraform 


#### KUBERNETES
Download Kubectl (Check version)

curl -LO https://dl.k8s.io/release/v1.32.5/bin/linux/amd64/kubectl

chmod +x kubectl

sudo mv kubectl /usr/bin/local/

Download kustomize

curl -s "https://raw.githubusercontent.com/kubernetes-sigs/kustomize/master/hack/install_kustomize.sh" | bash

chmod +x kustomize

sudo mv kustomize /usr/bin/local/

**Generate token using service account in the namespace** 

https://kubernetes.io/docs/reference/access-authn-authz/service-accounts-admin/#:~:text=To%20create%20a%20non%2Dexpiring,with%20that%20generated%20token%20data.



#### DEMO1
https://www.youtube.com/watch?v=lOvsdjb501U