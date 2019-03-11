# Ingestion-Pipeline

Log Event Data Format
--------------------
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
1. Run **docker-compose up** to run Kafka and Cassandra
3. Run the script **run-server.sh** to execute the **thrift-server**, **run-client.sh** for the **thrift-client** and **run-consumer.sh**  for the **kafka-consumer**
4. Navigate at *localhost:3030* in your browser. under the topic-ui you should see a topic named *log-events*
and inside you can find the ingested data. Data is stored as json.
5. Next open a terminal and enter *docker ps* and copy the cassandra container id
6. Run *docker exec -it [container-id] bash* to log into the cassandra container
7. Run *cqlsh* to get into cassandra terminal.
8. Run *Describe keyspaces;* - you should log-events keyspace and run *USE log-events;*
8. Run *SELECT * FROM logs;* to see the ingested data from kafka into cassandra.
