# Virtual Scroll System

http://ec2-3-107-185-2.ap-southeast-2.compute.amazonaws.com/


<h2>Full Stack webappp using Java and Python with implemented REST API with DJANGO and SPRINGBOOT framework </h2>
<h3>WebApp overview</h3>
Virtual Scroll Management System is a library to store Scroll in form of the digital text file. It allow users to 
access their accounts using their own identification signature with username and password to be able to upload their own
Scrolls and to view/download others' Scrolls.


This a free-style project is for learning purposes which requires to have Java for backend implementations

Implement a REST Api using Springboot to deal with data manipulations and Django framework to extract data and deal with Human Computer Interface.

The ```springboot.pid``` file to track local machine process which run the Java backend 

The ```django.pid``` file to track local machine process which run the Python HCI.


The database for this small scale project is db.sqlite3

<h3>Technology requirement</h3>

* Springboot : https://docs.spring.io/spring-boot/installing.html

 * make tools :

   ** (for Linux)
```sudo apt-get -y install make```

   **  (for Windows)
``` choco install make ```

Install require python packages with <code>pip install requirement.txt</code>


To test run the codebase, while at the root github directory use ```make run-all``` 

To stop the program, go to the terminal,  ```make clean```
