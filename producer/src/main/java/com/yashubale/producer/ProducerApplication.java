package com.yashubale.producer;

import com.yashubale.producer.configs.ProducerApplicationConfig;
import com.yashubale.producer.connectors.ProducerConnectors;
import com.yashubale.producer.jobs.CsvProducerJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.ConfigurationUtils;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Properties;

@Slf4j
public class ProducerApplication {
    public static void main(String[] args) throws Exception {
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        Properties props = new Properties();
        props.setProperty("taskmanager.memory.managed.size", "1024m");
        Configuration conf = ConfigurationUtils.createConfiguration(props);
        StreamExecutionEnvironment env = getStreamExecutionEnvironment(parameterTool, conf);
        CsvProducerJob csvProducerJob = getCsvProducerJob();
        csvProducerJob.init(env);
        csvProducerJob.start(env);
    }

    private static StreamExecutionEnvironment getStreamExecutionEnvironment(ParameterTool parameterTool, Configuration conf) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);
        env.getConfig().enableObjectReuse();
        env.setParallelism(1);
        env.setMaxParallelism(1);
        env.getCheckpointConfig().disableCheckpointing();
        env.getConfig().setGlobalJobParameters(parameterTool);
        return env;
    }

    private static CsvProducerJob getCsvProducerJob() {
        return new CsvProducerJob(getProducerConnectors());
    }

    private static ProducerConnectors getProducerConnectors() {
        ProducerApplicationConfig producerApplicationConfig = ProducerApplicationConfig
                .builder()
                .fileSourcePath("/Users/yashubale/personal-repos/csv-elasticsearch-demo/producer/src/main/resources/csv-files")
                .kafkaBootstrapServers("localhost:9092")
                .sinkTopic("sales-data-topic")
                .build();
        return new ProducerConnectors(producerApplicationConfig);
    }
}
