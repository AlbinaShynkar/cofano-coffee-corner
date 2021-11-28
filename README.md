# Cofano Coffee Corner

The Cofano Coffee Corner is a web application made exclusively for [Cofano](https://www.cofano.nl/nl/) employees to be able to communicate with each other from anywhere in the world and simulate a real-life coffee corner. Employees are able to view, create, and take part in company-related events as well as view and add to a digital bulletin board, visible to all other employees.

## Prerequisites

In order to be able to run the application, you will need to have an application server such as [Apache Tomcat](https://tomcat.apache.org/download-80.cgi) installed on your machine. Along with an application server, you will need a web browser supporting Server-Sent Events, click [here](https://developer.mozilla.org/en-US/docs/Web/API/Server-sent_events/Using_server-sent_events#Browser_compatibility) for a complete list of supported browsers.

## Installation

Clone the project directly from Git to install the web-app. Navigate to your desired folder, and apply:

```bash
git clone https://git.snt.utwente.nl/s2305399/cofanocoffeecorner.git
```
Add the project to your application server and start the server using your preferred Integrated Development Environment (IDE). Then click [here](http://localhost:8080/coffeecorner/) to use the installed Maven project or simply copy the following URL to your chosen web browser: `http://localhost:8080/coffeecorner/`

## Usage

As soon as you access the aforementioned URL, you will be prompted to log in. Follow the login process and use your account to log in. Once you are logged in, you should be able to see the dashboard of the system and be able to access any of its functionalities using the navigation menu on the left.

![Cofano Dashboard](https://git.snt.utwente.nl/s2305399/cofanocoffeecorner/-/raw/master/design/Dashboard.png)

## Testing

All code testing has been implemented using JUnit testing (integration testing for REST end points). The test classes can be found under `src/test` ([here](/src/test)). Each test class tests one of two things. A Database Access Object (DAO) class since these are the classes which are used by the controllers and which in turn use the models within the system to interpret/save the data retrieved/input, as well as controller test classes which test the REST end points of the system. In order to use the latter, the [Database](/src/main/java/com/cofano/coffeecorner/data/Database.java) class needs its final boolean **TESTING** (line 13) to be set to *true*, so that requests are not filtered and do not require the tests to run only if the user is logged in, since it is impossible to do so in the testing environment. By setting this global variable to *true*, it is also assured that every test will only modify and add data to the testing Database, and not affect the live one.

#### Live Database Credentials

Username: `dab_di19202b_237`  
Password: `AKo06Bs9uQ5rG7xg`

#### Testing Database Credentials 

Username: `dab_di19202b_353`  
Password: `NZIPoHqipf74izA7`

## System Design

Before sending data to the server, the front-end parses user inputs and produces a JSON object using JavaScript (alongside AJAX). The JSON object is then sent to the server using the appropriate URI for the desired request, defined using the Jersey RESTful Web Services framework, and following conventions. 

When the controller receives the clients request, it makes use of the Jackson API to parse the JSON data received into the appropriate Java model. The Java object produced is then sanitized (user input is filtered) to avoid any security issues, and it is then passed to the DAO class which makes use of SQL prepared statements to save/retrieve data. Database transactions are not part of the system since no simultaneous requests to update records can be made, and they are thereafter unnecessary.

Once the model has been added to the database, the [Broadcaster class](/src/main/java/com/cofano/coffeecorner/controller/Broadcaster.java) is called, so that the model can be sent to every user online using server-sent events. Should the server have any issues while parsing the JSON data, validating a certain user or model, a 515 error response code is sent back along with a custom exception describing what problem the server encountered. Custom exceptions can be found [here](/src/main/java/com/cofano/coffeecorner/exceptions).

When JavaScript detects an incoming server-sent event, it maps the received data to the correct model depending on the type of event received. If the user is not found in the same page corresponding to the event type received (e.g. If the user is not on the chat page when receiving a message), a notification will be sent instead (unless the user is in Do Not Disturb mode), and the number of unread notifications on the top right will increase. After 10 unread notifications, the number will stop to increase and the user will be regarded as not interested for the moment.

## Support

For any inquiries or troubleshooting, please contact any of the [project authors](#authors-and-acknowledgements).

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## Authors and Acknowledgements

### Jasper van Amerongen
- Project Leader  
- Project Coordinator
- Front-End Coordinator
- Shaper
> Email: `j.vanamerongen-1@student.utwente.nl`  

### Nidanur Gunay
- Back-End Developer
- Monitor Evaluator
- Project Tester
- Project Specialist
> Email: `n.gunay@student.utwente.nl`

### Adamo Mariani
- Back-End Coordinator
- Security Monitor
- Code Quality Monitor
> Email: `a.mariani@student.utwente.nl`

### Albina Shynkar
- Back-End Developer
- Database Designer
- Project Creative
- Progress Monitor
> Email: `a.shynkar@student.utwente.nl`

### Lola Solovyeva
- Front-End Developer
- Project Specialist
- Mock-Ups Designer
> Email: `o.solovyeva@student.utwente.nl`

### Eda Yardim
- Project Tester
- Back-End Developer
- Complete Finisher
> Email: `e.yardim@student.utwente.nl`