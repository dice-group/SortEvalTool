# SortEvalTool
Evaluation Tool for HARE

### Installing

Run the server using 
```
mvn spring-boot:run
```

Once the server is running make the client 

```
make client
```

The client can access the server at the following uri 

```
http://localhost:8080/next?username=<USERNAME>
```
The final saving of the dataset can be done in the uri 
```
http://localhost:8080/save?username=<USERNAME>
```
The save method expects a POST as in a JSON input which it writes directly to the corresponding file. 
All control in the server is in the controller package and is EvalController.class. 
Once a successful save is done it redirects to the next URI (if any). 

Currently the server maintains a Hashmap as a database (for ease of implementation and temporary use) for the user and his last selected category but writes the result file with the given username. 
This means if the server goes down then user might have to reevaluate all the categories.
