# csv-elasticsearch-demo

Before running the code, please run `docker-compose.yml` to spawn the infra. Following things will be exposed once infra is spawned.
- Kafka server at `localhost:9092`
- Elasticsearch at `localhost:9200`
- Kibana at `localhost:5601`
- Zookeeper at `localhost:2181` (for Kafka)

### There are three modules in the repo : 
- Commons - contains common code 
- Producer - a Flink java application that reads data from a directory (as many csv files as possible), and pushes the data to Kafka 
- Consumer - a Kafka consumer that reads data from kafka topic and writes data to elasticsearch (also creates index if not present)

### Design 
- Flink application (producer) monitors the `producer/src/main/resources/csv-files` directory for any file additions, parses the CSV files and pushes kafka records to `sales-data-topic` topic correspondingly
- Consumer will read data from `sales-data-topic` in batch, so that we don't poll for every record
- Consumer will create an index `sales-index` if it doesn't exist, schema is present in `consumer/src/main/resources/index.json`
- Consumer will create a bulk request so that we don't bombard ES for every record
- Consumer and producer are decoupled via Kafka

### Notes
- Kibana can be accessed at `http://localhost:5601/app/dev_tools#/console`
- Topic, servers, index names (no alias as of now), timeouts are hard coded in the code 
- Have added producer and consumer run configurations which can be directly used
- Have added dummy csv file `sales-data-100.csv` that contains 100 records
- This is not a dropwizard or spring application so have not added any `.env` file support
- With given amount of time everything can be made configurable
- Keep adding CSV files (assuming with headers) in `csv-files` directory Flink application will read it periodically
