package com.bjpowernode.vo;

//ajax请求返回的结果对象
public class ResultObject {
    //请求的应答结果码， -1：默认错误， 0：成功， 其他还有状态码
    private Integer code;
    //错误提示消息， 就是code的解释
    private String msg;
    //ajax请求服务器端返回的数据
    private Object data;


    public ResultObject() {
    }

    public ResultObject(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    //创建一个是成功的ResultObject
    public static ResultObject success(String msg){
        ResultObject ro = new ResultObject();
        ro.setCode(0);
        ro.setMsg(msg);
        ro.setData("");
        return ro;
    }

    //创建一个默认错误的ResultObject
    public static ResultObject fail(String msg){
        ResultObject ro = new ResultObject();
        ro.setCode(-1);
        ro.setMsg(msg);
        ro.setData("");
        return ro;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
