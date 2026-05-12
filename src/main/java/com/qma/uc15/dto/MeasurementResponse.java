package com.qma.uc15.dto;

/** DTO for outgoing measurement responses. Error Handling as Data. */
public class MeasurementResponse {
    private final boolean success;
    private final String message;
    private final Object data;

    private MeasurementResponse(boolean success, String message, Object data) {
        this.success = success; this.message = message; this.data = data;
    }

    public static MeasurementResponse ok(Object data)       { return new MeasurementResponse(true, "OK", data); }
    public static MeasurementResponse error(String message) { return new MeasurementResponse(false, message, null); }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public Object getData()    { return data; }

    @Override public String toString() {
        return String.format("Response{success=%s, message='%s', data=%s}", success, message, data);
    }
}
