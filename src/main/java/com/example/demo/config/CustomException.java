package com.example.demo.config;

import java.util.*;

public class CustomException {

    public static Map<String, Object> errorMsg(Integer errCd, String errMsg, String msg)
    {
        Map<String, Object> error = new HashMap<>();
        error.put("error_code", errCd);
        error.put("error_message", errMsg);

        Map<String, Object> response = new HashMap<>();
        response.put("message", msg);
        response.put("error", error);

        return response;
    }

    public static Map<String, Object> succRsp(String status, String msg, Object data)
    {
        Map<String, Object> ret = new HashMap<>();
        ret.put("status", status);
        ret.put("message", msg);
        ret.put("response_data", data);

        return ret;
    }
}
