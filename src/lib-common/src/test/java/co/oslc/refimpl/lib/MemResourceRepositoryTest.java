package co.oslc.refimpl.lib;

import org.eclipse.lyo.oslc.domains.rm.Requirement;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.*;

public class MemResourceRepositoryTest {

    private MemResourceRepository<Requirement> repository;

    @Before
    public void setUp() {
        repository = new MemResourceRepository<>();
    }

    @Test
    public void testCalculateETag() throws Exception {
        Requirement req = new Requirement(new URI("http://example.com/req/1"));
        req.setTitle("Test Requirement");

        String etag1 = repository.calculateETag(req);
        assertNotNull(etag1);
        assertFalse(etag1.isEmpty());

        // Check uppercase hex pattern (MD5 hex string is 32 uppercase hex characters)
        assertTrue(etag1.matches("^[0-9A-F]{32}$"));

        // ETag should be deterministic for the same content
        String etag2 = repository.calculateETag(req);
        assertEquals(etag1, etag2);

        // Modifying resource should produce different ETag
        req.setTitle("Updated Requirement");
        String etag3 = repository.calculateETag(req);
        assertFalse(etag1.equals(etag3));
    }
}
