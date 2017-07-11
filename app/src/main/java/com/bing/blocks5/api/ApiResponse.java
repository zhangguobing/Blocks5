/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zjonline.blocks5.api;

import android.support.annotation.Nullable;

import com.google.gson.JsonParseException;
import com.orhanobut.logger.Logger;
import com.zjonline.blocks5.Constants;
import com.zjonline.blocks5.R;
import com.zjonline.blocks5.util.GsonHelper;
import com.zjonline.blocks5.util.StringFetcher;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;
import retrofit2.Response;

/**
 * Common class used by API responses.
 * @param <T>
 */
public class ApiResponse<T> {
    public int code;
    @Nullable
    public T data;
    @Nullable
    public String message;

    /**
     * API是否请求失败
     * @return 失败返回true, 成功返回false
     */
    public boolean isSuccessful() {
        return Constants.CustomHttpCode.HTTP_SUCCESS  == code;
    }
}
