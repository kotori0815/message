package com.msg.util;

/**
 * Created by wudi on 2017/4/23.
 */
public class Page {
    private Integer pageIndex;
    private Integer pageSize;
    private Integer totalRows;
    private Integer totalPages;
    private Integer firstRow;
    private Integer lastRow;



    public Page(Integer pageIndex, Integer pageSize) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    public Page(){
        this(1,3);
    }
    public Page(Integer pageIndex){
        this(pageIndex,5);
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(Integer totalRows) {
        this.totalRows = totalRows;
        this.totalPages=this.totalRows%this.pageSize==0?
                this.totalRows/this.pageSize:
                this.totalRows/this.pageSize+1;
    }

    public Integer getFirstRow(){
        return (this.pageIndex-1)*pageSize;
    }
    public Integer getLastRow(){
        return this.pageIndex*pageSize;
    }
    public Integer getTotalPages() {
        return totalPages;
    }

    public boolean getHasProviousPage(){
        return this.pageIndex>1;
    }
    public  boolean getHasNextPage(){
        return this.pageIndex<this.totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }
}
