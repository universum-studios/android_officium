/*
 * *************************************************************************************************
 *                                 Copyright 2017 Universum Studios
 * *************************************************************************************************
 *                  Licensed under the Apache License, Version 2.0 (the "License")
 * -------------------------------------------------------------------------------------------------
 * You may not use this file except in compliance with the License. You may obtain a copy of the
 * License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied.
 *
 * See the License for the specific language governing permissions and limitations under the License.
 * *************************************************************************************************
 */
package universum.studios.android.officium.service;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Connection;
import okhttp3.Interceptor;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Martin Albedinsky
 */
public class TestChain implements Interceptor.Chain {

	private final Request request;
	private final Response.Builder responseBuilder;

	public TestChain(@NonNull Request request) {
		this(request, new Response.Builder().protocol(Protocol.HTTP_1_1).code(200).message("Response message."));
	}

	private TestChain(@NonNull final Request request, @NonNull final Response.Builder responseBuilder) {
		this.request = request;
		this.responseBuilder = responseBuilder;
	}

	@Override public Interceptor.Chain withConnectTimeout(final int timeout, @NonNull final TimeUnit unit) {
		return null;
	}

	@Override public int connectTimeoutMillis() {
		return 0;
	}

	@Override public Interceptor.Chain withWriteTimeout(final int timeout, @NonNull final TimeUnit unit) {
		return this;
	}

	@Override public int writeTimeoutMillis() {
		return 0;
	}

	@Override public Interceptor.Chain withReadTimeout(final int timeout, @NonNull final TimeUnit unit) {
		return this;
	}

	@Override public int readTimeoutMillis() {
		return 0;
	}

	@Override public Request request() {
		return request;
	}

	@Override public Response proceed(@NonNull final Request request) throws IOException {
		return responseBuilder.request(request).build();
	}

	@Override public Connection connection() {
		throw new UnsupportedOperationException();
	}

	@Override public Call call() {
		return null;
	}
}