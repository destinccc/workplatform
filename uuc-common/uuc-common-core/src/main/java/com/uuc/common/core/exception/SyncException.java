package com.uuc.common.core.exception;

/**
 * 业务异常
 * 
 * @author uuc
 */
public final class SyncException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误提示
     */
    private String message;

    /**
     * 错误明细，内部调试错误
     *
     * 和 {@link CommonResult#getDetailMessage()} 一致的设计
     */
    private String detailMessage;

    /**
     * 空构造方法，避免反序列化问题
     */
    public SyncException()
    {
    }

    public SyncException(String message)
    {
        this.message = message;
    }

    public SyncException(String message, Integer code)
    {
        this.message = message;
        this.code = code;
    }

    public String getDetailMessage()
    {
        return detailMessage;
    }

    public String getMessage()
    {
        return message;
    }

    public Integer getCode()
    {
        return code;
    }

    public SyncException setMessage(String message)
    {
        this.message = message;
        return this;
    }

    public SyncException setDetailMessage(String detailMessage)
    {
        this.detailMessage = detailMessage;
        return this;
    }
}