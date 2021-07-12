package com.sdydj.common.constant;

public class Productconstant {

    public enum  AttrEnum{
        ATTR_TYPE_BASE(1,"基本属性"),ATTR_TYPE_SALE(0,"销售属性");
        private int code;
        private String msg;
        AttrEnum(int code,String msg){
            this.code=code;
            this.msg=msg;
        }

        public String getMsg() {
            return msg;
        }

        public int getCode() {
            return code;
        }
    }
    public  enum UpStatusEnum{
        SPU_NEW(0,"新建"),SPU_UP(1,"上架"),SPU_DOWN(2,"下架");
        private int code;
        private String msg;
        UpStatusEnum(int code,String msg){
            this.code=code;
            this.msg=msg;
        }

        public String getMsg(){
            return  this.msg;
        }

        public int getCode(){
            return  this.code;
        }

    }
}
