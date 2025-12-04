program:
	@echo "Building Spring Boot application and starting services..."
	cd VirtualScrollSys && gradle build
	docker-compose up --build 

clean:
	@echo "Cleaning build artifacts and stopping services..."
	cd VirtualScrollSys && gradle clean
	docker-compose down

test:
	@echo "Running Spring Boot tests..."
	cd VirtualScrollSys && ./gradlew test jacocoTestReport --console=plain
