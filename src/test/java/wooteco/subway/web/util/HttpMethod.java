package wooteco.subway.web.util;

import static io.restassured.RestAssured.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public enum HttpMethod {
	GET((requestSpecification, url) -> requestSpecification.get(url)),
	POST((requestSpecification, url) -> requestSpecification.post(url)),
	DELETE((requestSpecification, url) -> requestSpecification.delete(url)),
	PUT((requestSpecification, url) -> requestSpecification.put(url));

	private BiFunction<RequestSpecification, String, Response> function;

	HttpMethod(BiFunction<RequestSpecification, String, Response> function) {
		this.function = function;
	}

	public <R> R request(String url, Map<String, String> params, HttpStatus statusCode,
		Class<R> returnType) {
		RequestSpecification when = given().
			body(params).
			contentType(MediaType.APPLICATION_JSON_VALUE).
			accept(MediaType.APPLICATION_JSON_VALUE).
			when();

		Response apply = this.function.apply(when, url);

		return apply
			.then().
				log().all().
			// Todo status도 공통되지 않나..?
				statusCode(statusCode.value()).
				extract().as(returnType);
	}

	public <R> R request(String url, HttpStatus statusCode, Class<R> returnType) {
		return request(url, new HashMap<>(), statusCode, returnType);
	}
}