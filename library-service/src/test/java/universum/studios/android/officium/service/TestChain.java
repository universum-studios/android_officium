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

	public TestChain(@NonNull Request request, @NonNull Response.Builder responseBuilder) {
		this.request = request;
		this.responseBuilder = responseBuilder;
	}

	@Override
	public Interceptor.Chain withConnectTimeout(int timeout, @NonNull TimeUnit unit) {
		return null;
	}

	@Override
	public int connectTimeoutMillis() {
		return 0;
	}

	@Override
	public Interceptor.Chain withWriteTimeout(int timeout, @NonNull TimeUnit unit) {
		return this;
	}

	@Override
	public int writeTimeoutMillis() {
		return 0;
	}

	@Override
	public Interceptor.Chain withReadTimeout(int timeout, @NonNull TimeUnit unit) {
		return this;
	}

	@Override
	public int readTimeoutMillis() {
		return 0;
	}

	@Override
	public Request request() {
		return request;
	}

	@Override
	public Response proceed(@NonNull Request request) throws IOException {
		return responseBuilder.request(request).build();
	}

	@Override
	public Connection connection() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Call call() {
		return null;
	}
}
