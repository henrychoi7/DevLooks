package gachon.mobile.programming.android.finalproject.utils;

import org.json.JSONException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class ExceptionHelper {
    private ExceptionHelper() {}

    public static String getApplicationExceptionMessage(Exception e) {
        if (e instanceof JSONException) {
            return "데이터 읽는 도중 오류가 발생하였습니다. (JSON 관련 오류).";
        }
        else if (e instanceof UnknownHostException) {
            return "서버에 접속을 실패하였습니다. 네트워크가 켜져있는지 확인해 주십시오.";
        }
        else if (e instanceof SocketTimeoutException) {
            return "네트워크 요청 시간이 만료되었습니다. 네트워크 상태를 점검해 주십시오.";
        }
        else if (e instanceof ConnectException) {
            return "서버 접속 도중 오류가 발생하였습니다. 네트워크 상태를 점검해 주십시오.";
        }
        else if (e instanceof SocketException) {
            return  "데이터 송수신중 오류가 발생하였습니다. 네트워크 상태를 점검해 주십시오.";
        }
        else {
            return "알 수 없는 에러가 발생하였습니다. [에러 내역: " + e.toString() + "]";
        }
    }
}
