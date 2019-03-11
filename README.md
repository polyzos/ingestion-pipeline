# Ingestion-Pipeline

Log Event Data Format
--------------------

The Log Event describes data from poll campaigns

| Field  | Description|
| ------------- | ------------- |
| version  | The current Schema version |
| currentDate | A timestamp with the ingestion time|
| startDate | The date the poll started |
| endDate | The date the poll ended|
| pollster | Who created the poll|
| favor | Number of voters in favor|
| oppose | Number of voters that are against  |
| total | Total number of voters |
| url | The pollster's url |
| device | Where the vote came from - can be [WEBAPP, ANDROID, IOS OR OTHER]|
| message  | Type of log message - can be [LOG, DEBUG, ERROR, WARNING]|

Running The App
---------------
1. At the root folder run **docker-compose up** to run Kafka and Cassandra (wait for 30 seconds)
2. Run the script **run-server.sh** to execute the **thrift-server**, **run-client.sh** for the **thrift-client** (After the server logs indicate the server is running) and **run-consumer.sh**  for the **kafka-consumer**
3. Navigate at *localhost:3030* in your browser. under the topics-ui you should see a topic named *log-events* and inside you can find the ingested data. Data is stored as json.
4. Next open a terminal and enter ***docker ps*** and copy the cassandra container id
5. Run ***docker exec -it [container-id] bash*** to log into the cassandra container
7. Run ***cqlsh*** to get into cassandra terminal.
8. Run ***Describe keyspaces;*** - you should see **log-events** keyspace and run ***USE log-events;***
8. Run ***SELECT * FROM logeventtable;*** to see the ingested data from kafka into cassandra.
