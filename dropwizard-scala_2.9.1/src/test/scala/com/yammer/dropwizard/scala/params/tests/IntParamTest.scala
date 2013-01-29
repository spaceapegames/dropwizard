package com.yammer.dropwizard.scala.params.tests

import org.junit.Test
import javax.ws.rs.WebApplicationException
import com.yammer.dropwizard.scala.params.IntParam
import org.scalatest.matchers.ShouldMatchers

class IntParamTest extends ShouldMatchers {

    val param = IntParam("40")

    @Test def `A valid int parameter has an int value` {
      param.value should equal (40)
    }

    @Test def `An invalid int parameter throws a WebApplicationException with an error message` {
      val thrown = evaluating {
        IntParam("poop")
      } should produce [WebApplicationException]
          val response = thrown.getResponse
          response.getStatus should equal (400)
          response.getEntity should equal ("Invalid parameter: poop (Must be an integer value.)")
    }

}
