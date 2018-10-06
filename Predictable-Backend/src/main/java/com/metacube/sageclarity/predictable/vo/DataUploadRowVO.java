package com.metacube.sageclarity.predictable.vo;

import java.util.List;

public class DataUploadRowVO {
    private List<List<Object>> rowOfDataList;
    private List<String> listOfHeaders;


    public List<List<Object>> getRowOfDataList() {
        return rowOfDataList;
    }
    public void setRowOfDataList(List<List<Object>> rowOfDataList) {
        this.rowOfDataList = rowOfDataList;
    }
    public List<String> getListOfHeaders() {
        return listOfHeaders;
    }
    public void setListOfHeaders(List<String> listOfHeaders) {
        this.listOfHeaders = listOfHeaders;
    }

}
