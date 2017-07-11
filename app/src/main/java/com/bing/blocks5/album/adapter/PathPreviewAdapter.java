/*
 * Copyright © Yan Zhenjie. All Rights Reserved
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
package com.bing.blocks5.album.adapter;

import java.util.List;

/**
 * <p>Preview local image.</p>
 * Created by yanzhenjie on 17-3-29.
 */
public class PathPreviewAdapter extends BasicPreviewAdapter<String> {

    public PathPreviewAdapter(List<String> previewList) {
        super(previewList);
    }

    @Override
    protected String getImagePath(String s) {
        return s;
    }
}
