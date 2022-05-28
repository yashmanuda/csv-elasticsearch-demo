package com.yashubale.commons.models;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
public class FileSourceParams {
    private String path;
    private SourceType sourceType;
    private Long seconds;

    @Getter
    public enum SourceType {
        BOUNDED, UNBOUNDED;
    }
}
