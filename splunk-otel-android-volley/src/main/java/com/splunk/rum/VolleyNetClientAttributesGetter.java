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

package com.splunk.rum;

import androidx.annotation.Nullable;
import com.android.volley.toolbox.HttpResponse;
import io.opentelemetry.instrumentation.api.instrumenter.net.NetClientAttributesGetter;
import io.opentelemetry.semconv.trace.attributes.SemanticAttributes;

enum VolleyNetClientAttributesGetter
        implements NetClientAttributesGetter<RequestWrapper, HttpResponse> {
    INSTANCE;

    @Override
    public String getTransport(RequestWrapper requestWrapper, @Nullable HttpResponse httpResponse) {
        return SemanticAttributes.NetTransportValues.IP_TCP;
    }

    @Nullable
    @Override
    public String getPeerName(RequestWrapper requestWrapper) {
        return UrlParser.getHost(requestWrapper.getRequest().getUrl());
    }

    @Nullable
    @Override
    public Integer getPeerPort(RequestWrapper requestWrapper) {
        return UrlParser.getPort(requestWrapper.getRequest().getUrl());
    }
}
