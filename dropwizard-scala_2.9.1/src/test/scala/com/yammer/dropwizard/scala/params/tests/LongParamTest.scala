package com.yammer.dropwizard.scala.params.tests

import org.junit.Test
import javax.ws.rs.WebApplicationException
import com.yammer.dropwizard.scala.params.LongParam
import org.scalatest.matchers.ShouldMatchers

class LongParamTest extends ShouldMatchers {

    private val param = LongParam("40")

    @Test def `A valid long parameter has an int value` {
      param.value should equal (40L)
    }

    @Test def `An invalid long parameter throws a WebApplicationException with an error message` {
      val thrown = evaluating {
        LongParam("poop")
      } should produce[WebApplicationException]
          val response = thrown.getResponse
          response.getStatus should equal (400)
          response.getEntity should equal ("Invalid parameter: poop (Must be an integer value.)")
    }

}
