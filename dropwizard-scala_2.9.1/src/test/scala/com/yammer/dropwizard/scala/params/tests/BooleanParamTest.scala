package com.yammer.dropwizard.scala.params.tests

import org.junit.Test
import javax.ws.rs.WebApplicationException
import com.yammer.dropwizard.scala.params.BooleanParam
import org.scalatest.matchers.ShouldMatchers

class BooleanParamTest extends ShouldMatchers {

  val param = BooleanParam("true")

  @Test def `A valid boolean parameter has a boolean value` {
    param.value should equal(true)
  }

  @Test def `An invalid boolean parameter throws a WebApplicationException with an error message` {
    val thrown = evaluating {
      BooleanParam("poop")
    } should produce[WebApplicationException]
    val response = thrown.getResponse
    response.getStatus should equal(400)
    response.getEntity should equal("Invalid parameter: poop (Must be \"true\" or \"false\".)")
  }

}
