/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.r2dbc.postgresql.codec;

import io.r2dbc.postgresql.client.EncodedParameter;
import io.r2dbc.postgresql.message.Format;
import io.r2dbc.postgresql.util.Assert;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

import static io.r2dbc.postgresql.codec.PostgresqlObjectId.JSON;
import static io.r2dbc.postgresql.codec.PostgresqlObjectId.JSONB;
import static io.r2dbc.postgresql.message.Format.FORMAT_BINARY;

abstract class AbstractJsonCodec<T> extends AbstractCodec<T> {

    private static final Set<PostgresqlObjectId> SUPPORTED_TYPES = EnumSet.of(JSON, JSONB);

    AbstractJsonCodec(Class<T> type) {
        super(type);
    }

    @Override
    EncodedParameter doEncode(T value) {
        return doEncode(value, JSON);
    }

    @Override
    public EncodedParameter encodeNull() {
        return createNull(FORMAT_BINARY, JSONB);
    }

    @Override
    boolean doCanDecode(PostgresqlObjectId type, Format format) {
        Assert.requireNonNull(format, "format must not be null");
        Assert.requireNonNull(type, "type must not be null");

        return SUPPORTED_TYPES.contains(type);
    }

    @Override
    public Iterable<PostgresTypeIdentifier> getDataTypes() {
        return SUPPORTED_TYPES.stream().map(PostgresTypeIdentifier.class::cast).collect(Collectors.toList());
    }

}
