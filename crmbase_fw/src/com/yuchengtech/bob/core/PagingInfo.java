package com.yuchengtech.bob.core;

/**
 * 查询分页类
 */
public class PagingInfo {
    
    /** 每页记录数 */
    private int pageSize;    
    /** 当前请求页 */
    private int currentPage;
    /** 总记录条数 */
    private int totalCount;

    public PagingInfo(){
        pageSize = 20;
        currentPage = -1;
        totalCount = -1;
    }
    
    public PagingInfo(int pageSize){
        this.pageSize = pageSize;
        currentPage = -1;
        totalCount = -1;
    }
    
    public PagingInfo(int pageSize, int currentPage){
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        totalCount = -1;
    }

    public int getBeginRowNumber(){
        if (currentPage > 1) {
            return (currentPage - 1) * pageSize;
        } else {
            return 0;
        }
    }
    
    public int getEndRowNumber(){
        if(this.currentPage > 1){
            return currentPage * pageSize;
        } else {
            return pageSize;
        }
    }
    
    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }    
    
    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
    
}
