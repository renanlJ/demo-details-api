APP := demo-details-api
IMAGE ?= $(APP):0.1.0
MVN := mvn

.PHONY: install lint format format-write test test-integration coverage build run docker-build docker-scan k8s-validate clean

install: ## Resolve Maven dependencies
	$(MVN) -q -B dependency:resolve

lint: ## Spotless check
	$(MVN) -q spotless:check

format: lint

format-write:
	$(MVN) -q spotless:apply

test: ## Unit + integration tests
	$(MVN) -q test

test-integration: test

coverage: ## Tests + JaCoCo gate (target/site/jacoco)
	$(MVN) -q verify

build: ## Package executable jar
	$(MVN) -q -DskipTests package

run: build
	java -jar target/$(APP)-0.1.0.jar

docker-build:
	docker build -t $(IMAGE) .

docker-scan:
	trivy image $(IMAGE)

k8s-validate:
	kubectl kustomize deploy/kubernetes | kubectl apply --dry-run=client -f -

clean:
	$(MVN) -q clean
