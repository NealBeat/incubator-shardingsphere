package io.shardingjdbc.core.jdbc.registor.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * The column information.
 *
 * @author panjuan
 */
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public final class ColumnInformation {
    
    private final String columnName;
    
    private final String columnType;
    
    private final String keyType;
}
