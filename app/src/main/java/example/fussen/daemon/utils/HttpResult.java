package example.fussen.daemon.utils;

/**
 * 类HttpResult.java的实现描述：HTTP响应结果
 * 
 * @author J 2016-1-12 上午12:04:51
 */
public class HttpResult {

    public static final String  CONTENT_TYPE_HTML = "text/html";
    public static final String  CONTENT_TYPE_HTML_JSON = "application/json";
    private int                 statusCode;
    private String              content;
    private byte[]              stream;

    public HttpResult(){
        super();
    }

    /**
     * @param statusCode
     * @param content
     */
    public HttpResult(int statusCode, String content){
        super();
        this.statusCode = statusCode;
        this.content = content;
    }

    /**
     * @param statusCode
     * @param stream
     */
    public HttpResult(int statusCode, byte[] stream){
        super();
        this.statusCode = statusCode;
        this.stream = stream;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public byte[] getStream() {
        return stream;
    }

    public void setStream(byte[] stream) {
        this.stream = stream;
    }

    @Override
    public String toString() {
        return "HttpResult [statusCode=" + statusCode + ", content=" + content + "]";
    }

}