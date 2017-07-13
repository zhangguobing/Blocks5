package com.bing.blocks5;

/**
 * author：zhangguobing on 2017/6/14 17:00
 * email：bing901222@qq.com
 */

public class Constants {

    public class Header {
        public static final String CONTENT_TYPE = "Content-Type";
        public static final String AUTHORIZATION = "Authorization";
        public static final String HTTP_TIMESTAMP = "Http-Timestamp";
        public static final String HTTP_APP_VERSION = "Http-App-Version";
        public static final String HTTP_APP_KEY = "Http-App-Key";
        public static final String HTTP_DEVICE_ID = "Http-Device-Id";
        public static final String HTTP_DEVICE_TYPE = "Http-Device-Type";
        public static final String HTTP_SIGNATURE = "Http-Signature";
        public static final String TOKEN = "token";
    }

    public class HttpCode {
        public static final int HTTP_UNAUTHORIZED = 401;
        public static final int HTTP_SERVER_ERROR = 500;
        public static final int HTTP_NOT_HAVE_NETWORK = 600;
        public static final int HTTP_NETWORK_ERROR = 700;
        public static final int HTTP_UNKNOWN_ERROR = 800;
    }

    public class CustomHttpCode {
        public static final int HTTP_SUCCESS = 0;
        public static final int HTTP_TOKEN_ERROR = 1;
        public static final int HTTP_NO_PERMISSION = 2;
        public static final int HTTP_NO_OBJECT= 3;
        public static final int HTTP_MISS_PARMAS = 4;
        public static final int HTTP_OLD_PWD_ERROR = 901;
    }

    public class Persistence {
        public static final String USER_INFO = "app.block5.userinfo";
        public static final String LAST_LOGIN_PHONE = "app.block5.phone";
        public static final String ACCESS_TOKEN = "app.block5.access_token";
        public static final String REFRESH_TOKEN = "app.block5.refresh_token";
        public static final String SHARE_FILE = "app.block5.share";
        public static final String TOKEN = "app.block5.token";
        public static final String UPLOAD_TOKEN = "app.block5.upload.token";
    }
}
