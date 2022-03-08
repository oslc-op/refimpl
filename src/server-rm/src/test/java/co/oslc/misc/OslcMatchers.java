package co.oslc.misc;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Assert;

import java.util.Locale;
import java.util.Set;

public class OslcMatchers {
    public static Matcher<? super String> isRdf() {
        return new BaseMatcher<>() {
            Set<String> rdfTypes = Set.of("text/turtle", "application/rdf+xml", "application/ld+json",
                "application/n-triples");

            @Override
            public boolean matches(Object actual) {
                Assert.assertNotNull(actual);
                String actualContentType = ((String) actual).toLowerCase(Locale.ROOT);
                return rdfTypes.contains(actualContentType);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("RDF type matches");
            }
        };
    }
}
