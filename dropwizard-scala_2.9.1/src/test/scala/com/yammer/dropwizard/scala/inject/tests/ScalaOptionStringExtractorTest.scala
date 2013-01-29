package com.yammer.dropwizard.scala.inject.tests

import org.junit.Test
import com.sun.jersey.core.util.MultivaluedMapImpl
import com.yammer.dropwizard.scala.inject.ScalaOptionStringExtractor
import org.scalatest.matchers.ShouldMatchers
import com.simple.simplespec.Mocks

class ScalaOptionStringExtractorTest extends ShouldMatchers with Mocks {

    val extractor = new ScalaOptionStringExtractor("name", "default")

    @Test def `Extracting a parameter has a name` {
      extractor.getName should equal ("name")
    }

    @Test def `Extracting a parameter has a default value` {
      extractor.getDefaultStringValue should equal ("default")
    }

    @Test def `Extracting a parameter extracts the first of a set of parameter values` {
      val params = new MultivaluedMapImpl()
      params.add("name", "one")
      params.add("name", "two")
      params.add("name", "three")

      val result = extractor.extract(params)
      result should equal (Some("one"))
    }

    @Test def `Extracting a parameter uses the default value if no parameter exists` {
      val params = new MultivaluedMapImpl()

      val result = extractor.extract(params)
      result should equal (Some("default"))
    }

    @Test def `Extracting a parameter with no default value returns None` {
      val extractor = new ScalaOptionStringExtractor("name", null)
      val params = new MultivaluedMapImpl()

      val result = extractor.extract(params)
      result should equal (None)
    }

}
