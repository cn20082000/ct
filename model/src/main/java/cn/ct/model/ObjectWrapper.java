/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.ct.model;

import java.io.Serializable;

/**
 *
 * @author cn200
 */
public class ObjectWrapper implements Serializable {
    public static final int MES_SUCCESS = 1;
    public static final int MES_FAILED = 2;
    
    public static final int GET_SIGNIN = 1001;
    public static final int REPLY_SIGNIN = 1002;
    public static final int GET_SIGNUP = 1003;
    public static final int REPLY_SIGNUP = 1004;
    public static final int GET_UPDATE = 1005;
    public static final int REPLY_UPDATE = 1006;
    
    public static final int GET_CREATE_PROJECT = 1101;
    public static final int REPLY_CREATE_PROJECT = 1102;
    public static final int GET_LIST_PROJECT = 1103;
    public static final int REPLY_LIST_PROJECT = 1104;
    public static final int GET_OPEN_PROJECT = 1105;
    public static final int REPLY_OPEN_PROJECT = 1106;
    public static final int GET_CLOSE_PROJECT = 1107;
    public static final int REPLY_CLOSE_PROJECT = 1108;
    public static final int GET_COLLAB_PROJECT = 1109;
    public static final int REPLY_COLLAB_PROJECT = 1110;
    
    public static final int GET_SEND_COLLAB = 1201;
    public static final int REPLY_SEND_COLLAB = 1202;
    public static final int GET_LIST_INVITE = 1203;
    public static final int REPLY_LIST_INVITE = 1204;
    public static final int GET_ACCEPT_COLLAB = 1205;
    public static final int REPLY_ACCEPT_COLLAB = 1206;
    public static final int GET_REJECT_COLLAB = 1207;
    public static final int REPLY_REJECT_COLLAB = 1208;
    public static final int ONL_RECEIVE_COLLAB = 12001;
    public static final int ONL_LIST_COLLAB = 12002;
    public static final int ONL_LIST_ONLINE = 12003;
    
    private int performative;
    private int message;
    private Object data;
    
    public ObjectWrapper() {
        super();
    }
    
    public ObjectWrapper(int performative, int message, Object data) {
        super();
        this.performative = performative;
        this.message = message;
        this.data = data;
    }

    public ObjectWrapper(int performative, Object data) {
        super();
        this.performative = performative;
        this.data = data;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }
    
    public int getPerformative() {
        return performative;
    }
    
    public void setPerformative(int performative) {
        this.performative = performative;
    }
    
    public Object getData() {
        return data;
    }
    
    public void setData(Object data) {
        this.data = data;
    }   
}