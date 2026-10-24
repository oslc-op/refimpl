package co.oslc.refimpl.lib;

import org.eclipse.lyo.oslc.domains.rm.Requirement;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.util.List;

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

    @Test
    public void testFetchResourcePageForSP() throws Exception {
        String sp = "test_sp";
        for (int i = 0; i < 5; i++) {
            Requirement req = new Requirement(new URI("http://example.com/req/" + i));
            req.setIdentifier("req-" + i);
            req.setTitle("Requirement " + i);
            repository.addResource(sp, "req-" + i, req);
        }

        // Page 0, pageSize 2: returns limit+1 = 3 items (lookahead to detect next page)
        List<Requirement> page0 = repository.fetchResourcePageForSP(sp, 0, 2);
        assertEquals(3, page0.size());

        // Page 1, pageSize 2: returns next items (limit+1 = 3 items)
        List<Requirement> page1 = repository.fetchResourcePageForSP(sp, 1, 2);
        assertEquals(3, page1.size());

        // When limited to pageSize (first 2 items of each page), page0 and page1 must not overlap
        List<Requirement> page0Slice = page0.subList(0, 2);
        List<Requirement> page1Slice = page1.subList(0, 2);
        assertNotEquals(page0Slice, page1Slice);
        for (Requirement r : page0Slice) {
            assertFalse("Page 1 should not contain page 0 item: " + r.getIdentifier(),
                    page1Slice.contains(r));
        }

        // Page 2, pageSize 2: returns 1 item (the 5th item, no lookahead since total is 5)
        List<Requirement> page2 = repository.fetchResourcePageForSP(sp, 2, 2);
        assertEquals(1, page2.size());
        assertFalse(page0Slice.contains(page2.get(0)));
        assertFalse(page1Slice.contains(page2.get(0)));

        // Page 3, pageSize 2: beyond total items, returns empty list
        List<Requirement> page3 = repository.fetchResourcePageForSP(sp, 3, 2);
        assertTrue(page3.isEmpty());
    }

    @Test
    public void testFetchResourcePageForSPEmptyRepository() {
        List<Requirement> page = repository.fetchResourcePageForSP("nonexistent_sp", 0, 10);
        assertNotNull(page);
        assertTrue(page.isEmpty());
    }
}
