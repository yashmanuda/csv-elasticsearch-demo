package com.yashubale.commons.filesource.csv;

import com.yashubale.commons.models.FileSourceParams;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.connector.file.src.FileSource;
import org.apache.flink.connector.file.src.FileSource.FileSourceBuilder;
import org.apache.flink.connector.file.src.reader.StreamFormat;
import org.apache.flink.core.fs.Path;
import org.apache.flink.formats.csv.CsvReaderFormat;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.dataformat.csv.CsvMapper;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.File;
import java.time.Duration;


public class CsvReader {

    public static <T> FileSource<T> getCsvFileSourceFromPath(FileSourceParams fileSourceParams, Class<T> tClass) {
        File file = new File(fileSourceParams.getPath());
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(tClass).withoutQuoteChar().withColumnSeparator(',').withHeader();
        StreamFormat<T> csvFormat = CsvReaderFormat.forSchema(mapper, schema, TypeInformation.of(tClass));
        return getFileSource(file, csvFormat, fileSourceParams);
    }

    private static <T> FileSource<T> getFileSource(File file, StreamFormat<T> streamFormat, FileSourceParams fileSourceParams) {
        FileSourceBuilder<T> tFileSourceBuilder = FileSource.forRecordStreamFormat(streamFormat, Path.fromLocalFile(file));
        if (fileSourceParams.getSourceType().equals(FileSourceParams.SourceType.UNBOUNDED)) {
            return tFileSourceBuilder.monitorContinuously(Duration.ofSeconds(fileSourceParams.getSeconds())).build();
        }
        return tFileSourceBuilder.build();
    }
}
