package com.yammer.dropwizard.scala.inject.tests

import org.junit.Test
import com.sun.jersey.core.util.MultivaluedMapImpl
import com.yammer.dropwizard.scala.inject.ScalaCollectionStringReaderExtractor
import org.scalatest.matchers.ShouldMatchers
import com.simple.simplespec.Mocks

class ScalaCollectionStringReaderExtractorTest extends ShouldMatchers with Mocks {

    val extractor = new ScalaCollectionStringReaderExtractor[Set]("name", "default", Set)

    @Test def `Extracting a parameter has a name` {
      extractor.getName should equal ("name")
    }

    @Test def `Extracting a parameter has a default value` {
      extractor.getDefaultStringValue should equal ("default")
    }

    @Test def `Extracting a parameter extracts a set of parameter values` {
      val params = new MultivaluedMapImpl()
      params.add("name", "one")
      params.add("name", "two")
      params.add("name", "three")

      val result = extractor.extract(params).asInstanceOf[Set[String]]
      result should equal (Set("one", "two", "three"))
    }

    @Test def `Extracting a parameter uses the default value if no parameter exists` {
      val params = new MultivaluedMapImpl()

      val result = extractor.extract(params).asInstanceOf[Set[String]]
      result should equal (Set("default"))
    }

    @Test def `Extracting a parameter with no default value returns an empty collection` {
      val extractor = new ScalaCollectionStringReaderExtractor[Set]("name", null, Set)
      val params = new MultivaluedMapImpl()

      val result = extractor.extract(params).asInstanceOf[Set[String]]
      result should equal (Set.empty[String])
    }

}
