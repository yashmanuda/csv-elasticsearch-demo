package com.yashubale.producer.jobs.abstractions;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public interface IFlinkJob {
    void init(StreamExecutionEnvironment env);

    void start(StreamExecutionEnvironment executionEnvironment) throws Exception;
}
