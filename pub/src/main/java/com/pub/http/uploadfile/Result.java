package com.pub.http.uploadfile;


import com.pub.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 返回结果
 *
 * @author Lenovo
 */
public class Result {
    private String Success;//错误代码 0-成功 其他失败
    private String Msg;//错误信息
    private String Data;//返回结果  error=0时，返回jason形式的结果
    private int total;//查询到数据总数

    public String getSuccess() {
        return Success;
    }

    public void setSuccess(String success) {
        this.Success = success;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        this.Msg = msg;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        this.Data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public static Result parse(String json) {
        System.out.println("json:" + json);
        if (StringUtils.isEmpty(json))
            return null;

        Result result = new Result();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
            if (!jsonObject.isNull("Success")) {
                result.setSuccess(jsonObject.getString("Success"));
            }
            if (!jsonObject.isNull("Msg")) {
                result.setMsg(jsonObject.getString("Msg"));
            }
            if (!jsonObject.isNull("Data")) {
                result.setData(jsonObject.getString("Data"));
            }
            if (!jsonObject.isNull("total")) {
                result.setTotal(jsonObject.getInt("total"));
            }
        } catch (JSONException e) {
            System.out.println("Result解析错误：" + e.toString());
        }

        return result;
    }
}
