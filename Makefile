.PHONY: run-django run-springboot run-all clean

# File to store the PIDs of running processes
PID_FILE=server.pids

#  run the Django app on port 8000
run-django:
	@echo "Starting Django server on port 8000..."
	@if lsof -i :8000; then \
		echo "Port 8000 is already in use. Please free it or use a different port."; \
	else \
		(cd VirtualScrollSys && python3 auth/manage.py runserver 8000 & echo $$! >> $(PID_FILE)); \
	fi

#  run the Spring Boot app on port 8081
run-springboot:
	@echo "Starting Spring Boot server on port 8081..."
	@if lsof -i :8081; then \
		echo "Port 8081 is already in use. Please free it or use a different port."; \
	else \
		(cd VirtualScrollSys && ./gradlew run --console=plain & echo $$! >> $(PID_FILE)); \
	fi

# Target to run both Django and Spring Boot services in parallel
run-all: clean
	@echo "Starting both Django and Spring Boot servers..."
	$(MAKE) run-django &
	$(MAKE) run-springboot &

# Target to kill running servers
clean:
	@echo "Stopping servers..."
	@if [ -f $(PID_FILE) ]; then \
		while read -r pid; do \
			kill $$pid 2>/dev/null && echo "Killed process $$pid"; \
		done < $(PID_FILE); \
		rm -f $(PID_FILE); \
	else \
		echo "No servers to stop."; \
	fi
