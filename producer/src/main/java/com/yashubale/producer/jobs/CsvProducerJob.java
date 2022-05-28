package com.yashubale.producer.jobs;

import com.yashubale.producer.connectors.ProducerConnectors;
import com.yashubale.producer.jobs.abstractions.IFlinkJob;
import com.yashubale.commons.models.SalesEntity;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.file.src.FileSource;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class CsvProducerJob implements IFlinkJob {
    private final ProducerConnectors producerConnectors;

    public CsvProducerJob(ProducerConnectors producerConnectors) {
        this.producerConnectors = producerConnectors;
    }

    public void init(StreamExecutionEnvironment env) {
        FileSource<SalesEntity> salesEntityFileSource = producerConnectors.getSalesEntityFileSource();

        String fetchStreamName = "fetch-source-stream";
        String sourceName = "csv-files-source";
        DataStream<SalesEntity> salesEntityDataStreamSource = env.fromSource(
                        salesEntityFileSource,
                        WatermarkStrategy.noWatermarks(),
                        sourceName)
                .name(fetchStreamName)
                .uid(fetchStreamName);

        String sinkName = "csv-files-sink";
        salesEntityDataStreamSource.sinkTo(producerConnectors.getSalesEntitySink())
                .uid(sinkName)
                .name(sinkName)
                .setParallelism(1);

    }

    public void start(StreamExecutionEnvironment executionEnvironment) throws Exception {
        executionEnvironment.execute(getClass().getSimpleName());
    }
}
