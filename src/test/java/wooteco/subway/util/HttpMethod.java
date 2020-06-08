package wooteco.subway.util;

import static io.restassured.RestAssured.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public enum HttpMethod {
	GET((requestSpecification, url) -> requestSpecification.get(url), HttpStatus.OK),
	POST((requestSpecification, url) -> requestSpecification.post(url), HttpStatus.CREATED),
	DELETE((requestSpecification, url) -> requestSpecification.delete(url), HttpStatus.OK),
	PUT((requestSpecification, url) -> requestSpecification.put(url), HttpStatus.OK);

	private BiFunction<RequestSpecification, String, Response> function;
	private HttpStatus httpStatus;

	HttpMethod(BiFunction<RequestSpecification, String, Response> function,
		HttpStatus httpStatus) {
		this.function = function;
		this.httpStatus = httpStatus;
	}

	public <R> R requestWithBody(String url, Class<R> returnType, Map<String, String> params) {
		return execute(url, params)
			.extract()
			.as(returnType);
	}

	public <R> R requestWithBody(String url, Class<R> returnType) {
		return requestWithBody(url, returnType, new HashMap<>());
	}

	public void request(String url, Map<String, String> params) {
		execute(url, params);
	}

	public void request(String url) {
		request(url, new HashMap<>());
	}

	private ValidatableResponse execute(String url, Map<String, String> params) {
		RequestSpecification when = given().
			body(params).
			contentType(MediaType.APPLICATION_JSON_VALUE).
			accept(MediaType.APPLICATION_JSON_VALUE).
			when();

		Response apply = this.function.apply(when, url);

		return apply
			.then()
			.log()
			.all()
			.statusCode(this.httpStatus.value());
	}
}