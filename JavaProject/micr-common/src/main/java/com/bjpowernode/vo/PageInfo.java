package com.bjpowernode.vo;

public class PageInfo {

    private Integer pageNo;//第几页
    private Integer pageSize;//每页记录数量
    private Integer totalRecord;//总记录数量
    private Integer totalPage;//总页数

    public PageInfo() {
        this.pageNo= 1;
        this.pageSize = 9;
        this.totalRecord = 0;
        this.totalPage = 0;

    }

    public PageInfo(Integer pageNo, Integer pageSize, Integer totalRecord) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totalRecord = totalRecord;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(Integer totalRecord) {
        this.totalRecord = totalRecord;
    }

    public Integer getTotalPage() {
        //算总页数
        if( totalRecord % pageSize == 0){
             totalPage =  totalRecord / pageSize;
        } else {
             totalPage = totalRecord / pageSize + 1;
        }

        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    @Override
    public String toString() {
        return "PageInfo{" +
                "pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                ", totalRecord=" + totalRecord +
                ", totalPage=" + totalPage +
                '}';
    }
}
