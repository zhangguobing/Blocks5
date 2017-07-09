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
package com.zjonline.blocks5.album.impl;

import android.widget.CompoundButton;

/**
 * <p>Listens on the selected state of Item.</p>
 * Created by Yan Zhenjie on 2016/10/18.
 */
public interface OnCompoundItemCheckListener {

    /**
     * When the selected state of Item changes.
     *
     * @param compoundButton {@link CompoundButton}.
     * @param position       item position.
     * @param isChecked      checked state.
     */
    void onCheckedChanged(CompoundButton compoundButton, int position, boolean isChecked);

}
