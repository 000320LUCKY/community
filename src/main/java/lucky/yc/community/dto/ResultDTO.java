package lucky.yc.community.dto;

import lombok.Data;
import lucky.yc.community.exception.CustomizeErrorCode;
import lucky.yc.community.exception.CustomizeException;

@Data
public class ResultDTO<T> {
    private Integer code;
    private String message;
    private T data;

    /**
     * 自定义异常信息
     * @param code 状态码
     * @param message 错误信息
     * @return 返回异常
     */
    public static ResultDTO errorOf(Integer code, String message) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage(message);
        return resultDTO;
    }

    /**
     * 其他异常信息
     * @param errorCode 错误码
     * @return 返回异常
     */
    public static ResultDTO errorOf(CustomizeErrorCode errorCode) {
        return errorOf(errorCode.getCode(), errorCode.getMessage());
    }

    public static ResultDTO okOf() {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功！");
        return resultDTO;
    }

    public static ResultDTO errorOf(CustomizeException ex) {
        return errorOf(ex.getCode(), ex.getMessage());
    }

    public static <T> ResultDTO okOf(T t) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功！");
        resultDTO.setData(t);
        return resultDTO;
    }
}
