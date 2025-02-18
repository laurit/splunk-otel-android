/*
 * Copyright Splunk Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.opentelemetry.rum.internal;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.sdk.trace.data.DelegatingSpanData;
import io.opentelemetry.sdk.trace.data.SpanData;

final class ModifiedSpanData extends DelegatingSpanData {

    private final Attributes modifiedAttributes;

    ModifiedSpanData(SpanData original, Attributes modifiedAttributes) {
        super(original);
        this.modifiedAttributes = modifiedAttributes;
    }

    @Override
    public Attributes getAttributes() {
        return modifiedAttributes;
    }

    @Override
    public int getTotalAttributeCount() {
        return modifiedAttributes.size();
    }
}
