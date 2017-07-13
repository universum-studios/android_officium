package universum.studios.android.officium.service;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Connection;
import okhttp3.Interceptor;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Martin Albedinsky
 */
public class TestChain implements Interceptor.Chain {

	@SuppressWarnings("unused")
	private static final String TAG = "TestChain";

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
}
