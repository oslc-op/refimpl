package co.oslc.refimpl.am.gen;

import co.oslc.refimpl.am.gen.servlet.ApplicationBinder;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.lyo.oslc.domains.am.Resource;
import org.eclipse.lyo.oslc.domains.am.LinkType;
import org.eclipse.lyo.oslc4j.trs.server.InmemPagedTrs;
import org.eclipse.lyo.oslc4j.trs.server.PagedTrsFactory;
import org.eclipse.lyo.core.trs.Base;
import org.eclipse.lyo.core.trs.ChangeLog;
import org.eclipse.lyo.core.trs.ChangeEvent;
import org.eclipse.lyo.core.trs.Creation;
import org.eclipse.lyo.core.trs.Modification;
import org.eclipse.lyo.core.trs.Deletion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TrsTest {

    private RestDelegate restDelegate;
    private InmemPagedTrs trs;

    @BeforeEach
    public void setUp() {
        restDelegate = new RestDelegate();
        ResourcesFactory resourcesFactory = new ResourcesFactory("http://localhost:8803/services/");
        restDelegate.resourcesFactory = resourcesFactory;

        ArrayList<URI> uris = new ArrayList<URI>();
        URI trsBaseUri = URI.create("http://localhost:8803/services/trs");
        // Initialize TRS with base page limit 5, changelog page limit 5, using explicit overload to avoid servlet dependency
        trs = new PagedTrsFactory().getInmemPagedTrs(5, 5, trsBaseUri, "base", "changelog", uris);
        restDelegate.trsEventHandler = trs;
    }

    @Test
    public void testTrsLifecycleEvents() throws Exception {
        // 1. Initial State: base should be empty, changelog should be null (since it is lazily created on first event)
        Base base = trs.getBaseFirst();
        assertNotNull(base);
        assertEquals(0, base.getMembers().size());

        ChangeLog changeLog = trs.getChangeLogLast();
        assertNull(changeLog, "Change log should be null before any events are recorded");

        // 2. Add resource (Creation event)
        Resource resource = new Resource();
        resource.setIdentifier("res-1");
        resource.setTitle("Test Resource 1");
        Resource createdRes = restDelegate.createResource(null, resource);
        assertNotNull(createdRes);

        // ChangeLog should now have 1 change event (Creation)
        changeLog = trs.getChangeLogLast();
        assertNotNull(changeLog, "Change log should be created after the first event");
        assertEquals(1, changeLog.getChange().size());
        ChangeEvent event1 = changeLog.getChange().get(0);
        assertTrue(event1 instanceof Creation, "Event should be of type Creation");
        assertEquals(createdRes.getAbout(), event1.getChanged());

        // 3. Modify resource (Modification event)
        createdRes.setTitle("Modified Test Resource 1");
        Resource updatedRes = restDelegate.updateResource(null, createdRes, "res-1");
        assertNotNull(updatedRes);

        // ChangeLog should now have 2 change events (Creation + Modification)
        changeLog = trs.getChangeLogLast();
        assertNotNull(changeLog);
        assertEquals(2, changeLog.getChange().size());
        ChangeEvent event2 = changeLog.getChange().get(1);
        assertTrue(event2 instanceof Modification, "Event should be of type Modification");
        assertEquals(updatedRes.getAbout(), event2.getChanged());

        // 4. Delete resource (Deletion event)
        Boolean deleted = restDelegate.deleteResource(null, "res-1");
        assertTrue(deleted);

        // ChangeLog should now have 3 change events (Creation + Modification + Deletion)
        changeLog = trs.getChangeLogLast();
        assertNotNull(changeLog);
        assertEquals(3, changeLog.getChange().size());
        ChangeEvent event3 = changeLog.getChange().get(2);
        assertTrue(event3 instanceof Deletion, "Event should be of type Deletion");
        assertEquals(updatedRes.getAbout(), event3.getChanged());
    }
}
