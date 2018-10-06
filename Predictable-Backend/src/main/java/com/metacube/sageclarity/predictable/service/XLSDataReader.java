package com.metacube.sageclarity.predictable.service;

import com.metacube.sageclarity.predictable.vo.DataUploadRowVO;

import java.io.InputStream;

public interface XLSDataReader {
    public DataUploadRowVO readData(InputStream inputStream);
}
