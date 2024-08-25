package com.exe.sharkauction.components.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DataNotFound {
    private String resourceName;

    private String fieldName;

    private Object fieldValue;
}
