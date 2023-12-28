package com.example.expense_services.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public class Response {
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String id;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> categoryList;

    public Response() {}
    public Response(String status){ this.status = status; }
    public Response(String id , String status){
        this.id = id;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getCategoryList() { return categoryList; }

    public void setCategoryList(List<String> categoryList) { this.categoryList = categoryList; }
}
