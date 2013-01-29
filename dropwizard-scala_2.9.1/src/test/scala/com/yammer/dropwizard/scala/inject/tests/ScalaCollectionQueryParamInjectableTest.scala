package com.yammer.dropwizard.scala.inject.tests

import javax.ws.rs.core.MultivaluedMap
import org.junit.Test
import com.sun.jersey.server.impl.model.parameter.multivalued.MultivaluedParameterExtractor
import com.sun.jersey.api.core.{ ExtendedUriInfo, HttpContext }
import com.yammer.dropwizard.scala.inject.ScalaCollectionQueryParamInjectable
import org.scalatest.matchers.ShouldMatchers
import com.simple.simplespec.Mocks

class ScalaCollectionQueryParamInjectableTest extends ShouldMatchers with Mocks {
  // TODO: Aug 17, 2010 <coda> -- test error handling

  val extractor = mock[MultivaluedParameterExtractor]
  val context = mock[HttpContext]
  val uriInfo = mock[ExtendedUriInfo]
  val params = mock[MultivaluedMap[String, String]]
  val extracted = mock[Object]

  extractor.extract(params) returns extracted
  context.getUriInfo returns uriInfo

  val injectable = new ScalaCollectionQueryParamInjectable(extractor, true)
  uriInfo.getQueryParameters(true) returns params

  @Test def `A Scala collection query param injectable with decoding extracts the query parameters` {
    val e = injectable.getValue(context)

    e should equal(extracted)
  }

  @Test def `A Scala collection query param injectable without decoding extracts the query parameters` {
    val injectable = new ScalaCollectionQueryParamInjectable(extractor, false)
    uriInfo.getQueryParameters(false) returns params
    val e = injectable.getValue(context)

    e should equal(extracted)
  }

}
