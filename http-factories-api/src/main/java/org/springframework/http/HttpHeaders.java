/*
 * Copyright 2002-2016 the original author or authors.
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

package org.springframework.http;

import org.springframework.util.MultiValueMap;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Pattern;

/**
 * Represents HTTP request and response headers, mapping string header names to a list of string values.
 *
 * <p>In addition to the normal methods defined by {@link Map}, this class offers the following
 * convenience methods:
 * <ul>
 * <li>{@link #getFirst(String)} returns the first value associated with a given header name</li>
 * <li>{@link #add(String, String)} adds a header value to the list of values for a header name</li>
 * <li>{@link #set(String, String)} sets the header value to a single string value</li>
 * </ul>
 *
 * <p>Inspired by {@code com.sun.net.httpserver.Headers}.
 *
 * @author Arjen Poutsma
 * @author Sebastien Deleuze
 * @author Brian Clozel
 * @author Juergen Hoeller
 * @author Josh Long
 * @since 3.0
 */
public class HttpHeaders implements MultiValueMap<String, String>, Serializable {

	private static final long serialVersionUID = -8578554704772377436L;

	/**
	 * The HTTP {@code Accept} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7231#section-5.3.2">Section 5.3.2 of RFC 7231</a>
	 */
	public static final String ACCEPT = "Accept";
	/**
	 * The HTTP {@code Accept-Charset} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7231#section-5.3.3">Section 5.3.3 of RFC 7231</a>
	 */
	public static final String ACCEPT_CHARSET = "Accept-Charset";
	/**
	 * The HTTP {@code Accept-Encoding} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7231#section-5.3.4">Section 5.3.4 of RFC 7231</a>
	 */
	public static final String ACCEPT_ENCODING = "Accept-Encoding";
	/**
	 * The HTTP {@code Accept-Language} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7231#section-5.3.5">Section 5.3.5 of RFC 7231</a>
	 */
	public static final String ACCEPT_LANGUAGE = "Accept-Language";
	/**
	 * The HTTP {@code Accept-Ranges} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7233#section-2.3">Section 5.3.5 of RFC 7233</a>
	 */
	public static final String ACCEPT_RANGES = "Accept-Ranges";
	/**
	 * The CORS {@code Access-Control-Allow-Credentials} response header field name.
	 * @see <a href="http://www.w3.org/TR/cors/">CORS W3C recommendation</a>
	 */
	public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
	/**
	 * The CORS {@code Access-Control-Allow-Headers} response header field name.
	 * @see <a href="http://www.w3.org/TR/cors/">CORS W3C recommendation</a>
	 */
	public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
	/**
	 * The CORS {@code Access-Control-Allow-Methods} response header field name.
	 * @see <a href="http://www.w3.org/TR/cors/">CORS W3C recommendation</a>
	 */
	public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
	/**
	 * The CORS {@code Access-Control-Allow-Origin} response header field name.
	 * @see <a href="http://www.w3.org/TR/cors/">CORS W3C recommendation</a>
	 */
	public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
	/**
	 * The CORS {@code Access-Control-Expose-Headers} response header field name.
	 * @see <a href="http://www.w3.org/TR/cors/">CORS W3C recommendation</a>
	 */
	public static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
	/**
	 * The CORS {@code Access-Control-Max-Age} response header field name.
	 * @see <a href="http://www.w3.org/TR/cors/">CORS W3C recommendation</a>
	 */
	public static final String ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";
	/**
	 * The CORS {@code Access-Control-Request-Headers} request header field name.
	 * @see <a href="http://www.w3.org/TR/cors/">CORS W3C recommendation</a>
	 */
	public static final String ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";
	/**
	 * The CORS {@code Access-Control-Request-Method} request header field name.
	 * @see <a href="http://www.w3.org/TR/cors/">CORS W3C recommendation</a>
	 */
	public static final String ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";
	/**
	 * The HTTP {@code Age} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7234#section-5.1">Section 5.1 of RFC 7234</a>
	 */
	public static final String AGE = "Age";
	/**
	 * The HTTP {@code Allow} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7231#section-7.4.1">Section 7.4.1 of RFC 7231</a>
	 */
	public static final String ALLOW = "Allow";
	/**
	 * The HTTP {@code Authorization} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7235#section-4.2">Section 4.2 of RFC 7235</a>
	 */
	public static final String AUTHORIZATION = "Authorization";
	/**
	 * The HTTP {@code Cache-Control} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7234#section-5.2">Section 5.2 of RFC 7234</a>
	 */
	public static final String CACHE_CONTROL = "Cache-Control";
	/**
	 * The HTTP {@code Connection} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7230#section-6.1">Section 6.1 of RFC 7230</a>
	 */
	public static final String CONNECTION = "Connection";
	/**
	 * The HTTP {@code Content-Encoding} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7231#section-3.1.2.2">Section 3.1.2.2 of RFC 7231</a>
	 */
	public static final String CONTENT_ENCODING = "Content-Encoding";
	/**
	 * The HTTP {@code Content-Disposition} header field name
	 * @see <a href="http://tools.ietf.org/html/rfc6266">RFC 6266</a>
	 */
	public static final String CONTENT_DISPOSITION = "Content-Disposition";
	/**
	 * The HTTP {@code Content-Language} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7231#section-3.1.3.2">Section 3.1.3.2 of RFC 7231</a>
	 */
	public static final String CONTENT_LANGUAGE = "Content-Language";
	/**
	 * The HTTP {@code Content-Length} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7230#section-3.3.2">Section 3.3.2 of RFC 7230</a>
	 */
	public static final String CONTENT_LENGTH = "Content-Length";
	/**
	 * The HTTP {@code Content-Location} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7231#section-3.1.4.2">Section 3.1.4.2 of RFC 7231</a>
	 */
	public static final String CONTENT_LOCATION = "Content-Location";
	/**
	 * The HTTP {@code Content-Range} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7233#section-4.2">Section 4.2 of RFC 7233</a>
	 */
	public static final String CONTENT_RANGE = "Content-Range";
	/**
	 * The HTTP {@code Content-Type} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7231#section-3.1.1.5">Section 3.1.1.5 of RFC 7231</a>
	 */
	public static final String CONTENT_TYPE = "Content-Type";
	/**
	 * The HTTP {@code Cookie} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc2109#section-4.3.4">Section 4.3.4 of RFC 2109</a>
	 */
	public static final String COOKIE = "Cookie";
	/**
	 * The HTTP {@code Date} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7231#section-7.1.1.2">Section 7.1.1.2 of RFC 7231</a>
	 */
	public static final String DATE = "Date";
	/**
	 * The HTTP {@code ETag} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7232#section-2.3">Section 2.3 of RFC 7232</a>
	 */
	public static final String ETAG = "ETag";
	/**
	 * The HTTP {@code Expect} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7231#section-5.1.1">Section 5.1.1 of RFC 7231</a>
	 */
	public static final String EXPECT = "Expect";
	/**
	 * The HTTP {@code Expires} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7234#section-5.3">Section 5.3 of RFC 7234</a>
	 */
	public static final String EXPIRES = "Expires";
	/**
	 * The HTTP {@code From} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7231#section-5.5.1">Section 5.5.1 of RFC 7231</a>
	 */
	public static final String FROM = "From";
	/**
	 * The HTTP {@code Host} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7230#section-5.4">Section 5.4 of RFC 7230</a>
	 */
	public static final String HOST = "Host";
	/**
	 * The HTTP {@code If-Match} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7232#section-3.1">Section 3.1 of RFC 7232</a>
	 */
	public static final String IF_MATCH = "If-Match";
	/**
	 * The HTTP {@code If-Modified-Since} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7232#section-3.3">Section 3.3 of RFC 7232</a>
	 */
	public static final String IF_MODIFIED_SINCE = "If-Modified-Since";
	/**
	 * The HTTP {@code If-None-Match} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7232#section-3.2">Section 3.2 of RFC 7232</a>
	 */
	public static final String IF_NONE_MATCH = "If-None-Match";
	/**
	 * The HTTP {@code If-Range} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7233#section-3.2">Section 3.2 of RFC 7233</a>
	 */
	public static final String IF_RANGE = "If-Range";
	/**
	 * The HTTP {@code If-Unmodified-Since} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7232#section-3.4">Section 3.4 of RFC 7232</a>
	 */
	public static final String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
	/**
	 * The HTTP {@code Last-Modified} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7232#section-2.2">Section 2.2 of RFC 7232</a>
	 */
	public static final String LAST_MODIFIED = "Last-Modified";
	/**
	 * The HTTP {@code Link} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc5988">RFC 5988</a>
	 */
	public static final String LINK = "Link";
	/**
	 * The HTTP {@code Location} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7231#section-7.1.2">Section 7.1.2 of RFC 7231</a>
	 */
	public static final String LOCATION = "Location";
	/**
	 * The HTTP {@code Max-Forwards} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7231#section-5.1.2">Section 5.1.2 of RFC 7231</a>
	 */
	public static final String MAX_FORWARDS = "Max-Forwards";
	/**
	 * The HTTP {@code Origin} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc6454">RFC 6454</a>
	 */
	public static final String ORIGIN = "Origin";
	/**
	 * The HTTP {@code Pragma} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7234#section-5.4">Section 5.4 of RFC 7234</a>
	 */
	public static final String PRAGMA = "Pragma";
	/**
	 * The HTTP {@code Proxy-Authenticate} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7235#section-4.3">Section 4.3 of RFC 7235</a>
	 */
	public static final String PROXY_AUTHENTICATE = "Proxy-Authenticate";
	/**
	 * The HTTP {@code Proxy-Authorization} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7235#section-4.4">Section 4.4 of RFC 7235</a>
	 */
	public static final String PROXY_AUTHORIZATION = "Proxy-Authorization";
	/**
	 * The HTTP {@code Range} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7233#section-3.1">Section 3.1 of RFC 7233</a>
	 */
	public static final String RANGE = "Range";
	/**
	 * The HTTP {@code Referer} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7231#section-5.5.2">Section 5.5.2 of RFC 7231</a>
	 */
	public static final String REFERER = "Referer";
	/**
	 * The HTTP {@code Retry-After} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7231#section-7.1.3">Section 7.1.3 of RFC 7231</a>
	 */
	public static final String RETRY_AFTER = "Retry-After";
	/**
	 * The HTTP {@code Server} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7231#section-7.4.2">Section 7.4.2 of RFC 7231</a>
	 */
	public static final String SERVER = "Server";
	/**
	 * The HTTP {@code Set-Cookie} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc2109#section-4.2.2">Section 4.2.2 of RFC 2109</a>
	 */
	public static final String SET_COOKIE = "Set-Cookie";
	/**
	 * The HTTP {@code Set-Cookie2} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc2965">RFC 2965</a>
	 */
	public static final String SET_COOKIE2 = "Set-Cookie2";
	/**
	 * The HTTP {@code TE} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7230#section-4.3">Section 4.3 of RFC 7230</a>
	 */
	public static final String TE = "TE";
	/**
	 * The HTTP {@code Trailer} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7230#section-4.4">Section 4.4 of RFC 7230</a>
	 */
	public static final String TRAILER = "Trailer";
	/**
	 * The HTTP {@code Transfer-Encoding} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7230#section-3.3.1">Section 3.3.1 of RFC 7230</a>
	 */
	public static final String TRANSFER_ENCODING = "Transfer-Encoding";
	/**
	 * The HTTP {@code Upgrade} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7230#section-6.7">Section 6.7 of RFC 7230</a>
	 */
	public static final String UPGRADE = "Upgrade";
	/**
	 * The HTTP {@code User-Agent} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7231#section-5.5.3">Section 5.5.3 of RFC 7231</a>
	 */
	public static final String USER_AGENT = "User-Agent";
	/**
	 * The HTTP {@code Vary} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7231#section-7.1.4">Section 7.1.4 of RFC 7231</a>
	 */
	public static final String VARY = "Vary";
	/**
	 * The HTTP {@code Via} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7230#section-5.7.1">Section 5.7.1 of RFC 7230</a>
	 */
	public static final String VIA = "Via";
	/**
	 * The HTTP {@code Warning} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7234#section-5.5">Section 5.5 of RFC 7234</a>
	 */
	public static final String WARNING = "Warning";
	/**
	 * The HTTP {@code WWW-Authenticate} header field name.
	 * @see <a href="http://tools.ietf.org/html/rfc7235#section-4.1">Section 4.1 of RFC 7235</a>
	 */
	public static final String WWW_AUTHENTICATE = "WWW-Authenticate";

	/**
	 * Date formats as specified in the HTTP RFC
	 * @see <a href="https://tools.ietf.org/html/rfc7231#section-7.1.1.1">Section 7.1.1.1 of RFC 7231</a>
	 */
	private static final String[] DATE_FORMATS = new String[] {
			"EEE, dd MMM yyyy HH:mm:ss zzz",
			"EEE, dd-MMM-yy HH:mm:ss zzz",
			"EEE MMM dd HH:mm:ss yyyy"
	};

	/**
	 * Pattern matching ETag multiple field values in headers such as "If-Match", "If-None-Match"
	 * @see <a href="https://tools.ietf.org/html/rfc7232#section-2.3">Section 2.3 of RFC 7232</a>
	 */
	private static final Pattern ETAG_HEADER_VALUE_PATTERN = Pattern.compile("\\*|\\s*((W\\/)?(\"[^\"]*\"))\\s*,?");

	private static TimeZone GMT = TimeZone.getTimeZone("GMT");


	private final Map<String, List<String>> headers;


	/**
	 * Constructs a new, empty instance of the {@code HttpHeaders} object.
	 */
	public HttpHeaders() {
		this(new LinkedCaseInsensitiveMap<List<String>>(8, Locale.ENGLISH), false);
	}

	/**
	 * Private constructor that can create read-only {@code HttpHeader} instances.
	 */
	private HttpHeaders(Map<String, List<String>> headers, boolean readOnly) {
		if (headers == null) {
			throw new IllegalArgumentException("'headers' must not be null");
		}
		if (readOnly) {
			Map<String, List<String>> map =
					new LinkedCaseInsensitiveMap<List<String>>(headers.size(), Locale.ENGLISH);
			for (Entry<String, List<String>> entry : headers.entrySet()) {
				List<String> values = Collections.unmodifiableList(entry.getValue());
				map.put(entry.getKey(), values);
			}
			this.headers = Collections.unmodifiableMap(map);
		}
		else {
			this.headers = headers;
		}
	}


	/**
	 * Set the list of acceptable {@linkplain MediaType media types},
	 * as specified by the {@code Accept} header.
	 */
	public void setAccept(List<MediaType> acceptableMediaTypes) {
		set(ACCEPT, MediaType.toString(acceptableMediaTypes));
	}

	/**
	 * Return the list of acceptable {@linkplain MediaType media types},
	 * as specified by the {@code Accept} header.
	 * <p>Returns an empty list when the acceptable media types are unspecified.
	 */
	public List<MediaType> getAccept() {
		return MediaType.parseMediaTypes(get(ACCEPT));
	}

	/**
	 * Set the length of the body in bytes, as specified by the
	 * {@code Content-Length} header.
	 */
	public void setContentLength(long contentLength) {
		set(CONTENT_LENGTH, Long.toString(contentLength));
	}

	/**
	 * Return the length of the body in bytes, as specified by the
	 * {@code Content-Length} header.
	 * <p>Returns -1 when the content-length is unknown.
	 */
	public long getContentLength() {
		String value = getFirst(CONTENT_LENGTH);
		return (value != null ? Long.parseLong(value) : -1);
	}

	/**
	 * Set the {@linkplain MediaType media type} of the body,
	 * as specified by the {@code Content-Type} header.
	 */
	public void setContentType(MediaType mediaType) {
		if (mediaType.isWildcardType()) {
			throw new IllegalArgumentException("'Content-Type' cannot contain wildcard type '*'");
		}
		if (mediaType.isWildcardSubtype()) {
			throw new IllegalArgumentException("'Content-Type' cannot contain wildcard subtype '*'");
		}
		set(CONTENT_TYPE, mediaType.toString());
	}

	/**
	 * Return the {@linkplain MediaType media type} of the body, as specified
	 * by the {@code Content-Type} header.
	 * <p>Returns {@code null} when the content-type is unknown.
	 */
	public MediaType getContentType() {
		String value = getFirst(CONTENT_TYPE);
		return value != null && value.length() > 0 ? MediaType.parseMediaType(value) : null;
	}

	/**
	 * Set the date and time at which the message was created, as specified
	 * by the {@code Date} header.
	 * <p>The date should be specified as the number of milliseconds since
	 * January 1, 1970 GMT.
	 */
	public void setDate(long date) {
		setDate(DATE, date);
	}

	/**
	 * Return the date and time at which the message was created, as specified
	 * by the {@code Date} header.
	 * <p>The date is returned as the number of milliseconds since
	 * January 1, 1970 GMT. Returns -1 when the date is unknown.
	 * @throws IllegalArgumentException if the value can't be converted to a date
	 */
	public long getDate() {
		return getFirstDate(DATE);
	}

	/**
	 * Set the (new) entity tag of the body, as specified by the {@code ETag} header.
	 */
	public void setETag(String eTag) {
		if (eTag != null) {
			if (!(eTag.startsWith("\"") || eTag.startsWith("W/"))) {
                throw new IllegalArgumentException("Invalid eTag, does not start with W/ or \"");
            }
			if (!eTag.endsWith("\"")) {
                throw new IllegalArgumentException("Invalid eTag, does not end with \"");
            }
		}
		set(ETAG, eTag);
	}

	/**
	 * Return the entity tag of the body, as specified by the {@code ETag} header.
	 */
	public String getETag() {
		return getFirst(ETAG);
	}

	/**
	 * Set the given date under the given header name after formatting it as a string
	 * using the pattern {@code "EEE, dd MMM yyyy HH:mm:ss zzz"}. The equivalent of
	 * {@link #set(String, String)} but for date headers.
	 * @since 3.2.4
	 */
	public void setDate(String headerName, long date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMATS[0], Locale.US);
		dateFormat.setTimeZone(GMT);
		set(headerName, dateFormat.format(new Date(date)));
	}

	/**
	 * Parse the first header value for the given header name as a date,
	 * return -1 if there is no value, or raise {@link IllegalArgumentException}
	 * if the value cannot be parsed as a date.
	 * @param headerName the header name
	 * @return the parsed date header, or -1 if none
	 * @since 3.2.4
	 */
	public long getFirstDate(String headerName) {
		return getFirstDate(headerName, true);
	}

	/**
	 * Parse the first header value for the given header name as a date,
	 * return -1 if there is no value or also in case of an invalid value
	 * (if {@code rejectInvalid=false}), or raise {@link IllegalArgumentException}
	 * if the value cannot be parsed as a date.
	 * @param headerName the header name
	 * @param rejectInvalid whether to reject invalid values with an
	 * {@link IllegalArgumentException} ({@code true}) or rather return -1
	 * in that case ({@code false})
	 * @return the parsed date header, or -1 if none (or invalid)
 	 */
	private long getFirstDate(String headerName, boolean rejectInvalid) {
		String headerValue = getFirst(headerName);
		if (headerValue == null) {
			// No header value sent at all
			return -1;
		}
		if (headerValue.length() >= 3) {
			// Short "0" or "-1" like values are never valid HTTP date headers...
			// Let's only bother with SimpleDateFormat parsing for long enough values.
			for (String dateFormat : DATE_FORMATS) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);
				simpleDateFormat.setTimeZone(GMT);
				try {
					return simpleDateFormat.parse(headerValue).getTime();
				}
				catch (ParseException ex) {
					// ignore
				}
			}
		}
		if (rejectInvalid) {
			throw new IllegalArgumentException("Cannot parse date value \"" + headerValue +
					"\" for \"" + headerName + "\" header");
		}
		return -1;
	}

	/**
	 * Return all values of a given header name,
	 * even if this header is set multiple times.
	 * @param headerName the header name
	 * @return all associated values
	 * @since 4.3
	 */
	public List<String> getValuesAsList(String headerName) {
		List<String> values = get(headerName);
		if (values != null) {
			List<String> result = new ArrayList<String>();
			for (String value : values) {
				if (value != null) {
					String[] tokens = StringUtils.tokenizeToStringArray(value, ",");
					for (String token : tokens) {
						result.add(token);
					}
				}
			}
			return result;
		}
		return Collections.emptyList();
	}

	// MultiValueMap implementation

	/**
	 * Return the first header value for the given header name, if any.
	 * @param headerName the header name
	 * @return the first header value, or {@code null} if none
	 */
	@Override
	public String getFirst(String headerName) {
		List<String> headerValues = this.headers.get(headerName);
		return (headerValues != null ? headerValues.get(0) : null);
	}

	/**
	 * Add the given, single header value under the given name.
	 * @param headerName the header name
	 * @param headerValue the header value
	 * @throws UnsupportedOperationException if adding headers is not supported
	 * @see #put(String, List)
	 * @see #set(String, String)
	 */
	@Override
	public void add(String headerName, String headerValue) {
		List<String> headerValues = this.headers.get(headerName);
		if (headerValues == null) {
			headerValues = new LinkedList<String>();
			this.headers.put(headerName, headerValues);
		}
		headerValues.add(headerValue);
	}

	/**
	 * Set the given, single header value under the given name.
	 * @param headerName the header name
	 * @param headerValue the header value
	 * @throws UnsupportedOperationException if adding headers is not supported
	 * @see #put(String, List)
	 * @see #add(String, String)
	 */
	@Override
	public void set(String headerName, String headerValue) {
		List<String> headerValues = new LinkedList<String>();
		headerValues.add(headerValue);
		this.headers.put(headerName, headerValues);
	}

	@Override
	public void setAll(Map<String, String> values) {
		for (Entry<String, String> entry : values.entrySet()) {
			set(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public Map<String, String> toSingleValueMap() {
		LinkedHashMap<String, String> singleValueMap = new LinkedHashMap<String,String>(this.headers.size());
		for (Entry<String, List<String>> entry : this.headers.entrySet()) {
			singleValueMap.put(entry.getKey(), entry.getValue().get(0));
		}
		return singleValueMap;
	}


	// Map implementation

	@Override
	public int size() {
		return this.headers.size();
	}

	@Override
	public boolean isEmpty() {
		return this.headers.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return this.headers.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return this.headers.containsValue(value);
	}

	@Override
	public List<String> get(Object key) {
		return this.headers.get(key);
	}

	@Override
	public List<String> put(String key, List<String> value) {
		return this.headers.put(key, value);
	}

	@Override
	public List<String> remove(Object key) {
		return this.headers.remove(key);
	}

	@Override
	public void putAll(Map<? extends String, ? extends List<String>> map) {
		this.headers.putAll(map);
	}

	@Override
	public void clear() {
		this.headers.clear();
	}

	@Override
	public Set<String> keySet() {
		return this.headers.keySet();
	}

	@Override
	public Collection<List<String>> values() {
		return this.headers.values();
	}

	@Override
	public Set<Entry<String, List<String>>> entrySet() {
		return this.headers.entrySet();
	}


	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof HttpHeaders)) {
			return false;
		}
		HttpHeaders otherHeaders = (HttpHeaders) other;
		return this.headers.equals(otherHeaders.headers);
	}

	@Override
	public int hashCode() {
		return this.headers.hashCode();
	}

	@Override
	public String toString() {
		return this.headers.toString();
	}


	/**
	 * Return a {@code HttpHeaders} object that can only be read, not written to.
	 */
	public static HttpHeaders readOnlyHttpHeaders(HttpHeaders headers) {
		return new HttpHeaders(headers, true);
	}

	/**
	 * Encode the given header field param as describe in RFC 5987.
	 * @param input the header field param
	 * @param charset the charset of the header field param string
	 * @return the encoded header field param
	 * @see <a href="https://tools.ietf.org/html/rfc5987">RFC 5987</a>
	 */
	static String encodeHeaderFieldParam(String input, Charset charset) {
		if (input == null) {
			throw new IllegalArgumentException("Input String should not be null");
		}
		if (charset == null) {
			throw new IllegalArgumentException("Charset should not be null");
		}
		if (charset.name().equals("US-ASCII")) {
			return input;
		}
		if (!(charset.name().equals("UTF-8") || charset.name().equals("ISO-8859-1"))) {
			throw new IllegalArgumentException("Charset should be UTF-8 or ISO-8859-1");
		}
		byte[] source = input.getBytes(charset);
		int len = source.length;
		StringBuilder sb = new StringBuilder(len << 1);
		sb.append(charset.name());
		sb.append("''");
		for (byte b : source) {
			if (isRFC5987AttrChar(b)) {
				sb.append((char) b);
			}
			else {
				sb.append('%');
				char hex1 = Character.toUpperCase(Character.forDigit((b >> 4) & 0xF, 16));
				char hex2 = Character.toUpperCase(Character.forDigit(b & 0xF, 16));
				sb.append(hex1);
				sb.append(hex2);
			}
		}
		return sb.toString();
	}

	private static boolean isRFC5987AttrChar(byte c) {
		return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') ||
				c == '!' || c == '#' || c == '$' || c == '&' || c == '+' || c == '-' ||
				c == '.' || c == '^' || c == '_' || c == '`' || c == '|' || c == '~';
	}

}
