package co.oslc.refimpl.rm.gen;

import java.net.URI;
import java.util.UUID;

import jakarta.ws.rs.BadRequestException;
import org.eclipse.lyo.oslc.domains.rm.Requirement;
import org.eclipse.lyo.oslc4j.core.model.IResource;
import org.eclipse.lyo.oslc4j.trs.server.TrsEventHandler;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class RequirementValidationTest {
    private RestDelegate delegate;
    private String serviceProviderId;
    private RecordingEvents events;

    @Before
    public void setUp() {
        serviceProviderId = UUID.randomUUID().toString();
        delegate = new RestDelegate();
        delegate.resourcesFactory = new ResourcesFactory("http://example.org/");
        events = new RecordingEvents();
        delegate.trsEventHandler = events;
    }

    @Test
    public void createRejectsMissingTitle() {
        assertInvalidCreate("title");
    }

    @Test
    public void createRejectsMissingIdentifier() {
        assertInvalidCreate("identifier");
    }

    @Test
    public void updateRejectsMissingTitle() {
        assertInvalidUpdate("title");
    }

    @Test
    public void updateRejectsMissingIdentifier() {
        assertInvalidUpdate("identifier");
    }

    @Test
    public void createAcceptsValidRequirementWithoutSubject() {
        Requirement requirement = validRequirement();
        assertNull(requirement.getAbout());
        assertSame(requirement, delegate.createRequirement(null, requirement, serviceProviderId));
        assertEquals(subject(), requirement.getAbout());
        assertNotNull(requirement.getCreated());
        assertSame(requirement, delegate.getRequirement(null, serviceProviderId, "id"));
        assertEquals(1, events.created);
    }

    @Test
    public void updateAcceptsValidRequirement() {
        Requirement original = validRequirement();
        RestDelegate.requirementsForSP(serviceProviderId).put("id", original);
        Requirement replacement = validRequirement();
        replacement.setAbout(subject());
        assertSame(replacement, delegate.updateRequirement(null, replacement, serviceProviderId, "id"));
        assertSame(replacement, delegate.getRequirement(null, serviceProviderId, "id"));
        assertNotNull(replacement.getModified());
        assertEquals(1, events.modified);
    }

    @Test
    public void updateStillRejectsMismatchedSubject() {
        Requirement requirement = validRequirement();
        requirement.setAbout(URI.create("http://example.org/wrong"));
        jakarta.ws.rs.WebApplicationException exception = assertThrows(jakarta.ws.rs.WebApplicationException.class,
                () -> delegate.updateRequirement(null, requirement, serviceProviderId, "id"));
        assertEquals(400, exception.getResponse().getStatus());
        assertNull(requirement.getModified());
        assertEquals(0, events.modified);
    }

    @Test
    public void createRejectsNullBody() {
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> delegate.createRequirement(null, null, serviceProviderId));
        assertEquals(400, exception.getResponse().getStatus());
        assertEquals(0, events.created);
    }

    private void assertInvalidCreate(String property) {
        Requirement requirement = invalidRequirement(property);
        assertViolation(property, () -> delegate.createRequirement(null, requirement, serviceProviderId));
        assertTrue(RestDelegate.requirementsForSP(serviceProviderId).isEmpty());
        assertNull(requirement.getAbout());
        assertNull(requirement.getCreated());
        assertEquals(0, events.created);
    }

    private void assertInvalidUpdate(String property) {
        Requirement original = validRequirement();
        RestDelegate.requirementsForSP(serviceProviderId).put("id", original);
        Requirement replacement = invalidRequirement(property);
        replacement.setAbout(subject());
        assertViolation(property, () -> delegate.updateRequirement(null, replacement, serviceProviderId, "id"));
        assertSame(original, delegate.getRequirement(null, serviceProviderId, "id"));
        assertNull(replacement.getModified());
        assertEquals(0, events.modified);
    }

    private void assertViolation(String property, org.junit.function.ThrowingRunnable action) {
        BadRequestException exception = assertThrows(BadRequestException.class, action);
        assertEquals(400, exception.getResponse().getStatus());
        assertEquals("text/plain", exception.getResponse().getMediaType().toString());
        assertTrue(exception.getResponse().getEntity().toString()
                .contains("http://purl.org/dc/terms/" + property));
    }

    private Requirement invalidRequirement(String property) {
        Requirement requirement = validRequirement();
        if ("title".equals(property)) {
            requirement.setTitle(null);
        } else {
            requirement.setIdentifier(null);
        }
        return requirement;
    }

    private Requirement validRequirement() {
        Requirement requirement = new Requirement();
        requirement.setTitle("A requirement");
        requirement.setIdentifier("id");
        return requirement;
    }

    private URI subject() {
        return delegate.resourcesFactory.constructURIForRequirement(serviceProviderId, "id");
    }

    private static class RecordingEvents implements TrsEventHandler {
        private int created;
        private int modified;

        @Override
        public void onCreated(IResource resource) {
            created++;
        }

        @Override
        public void onModified(IResource resource) {
            modified++;
        }

        @Override
        public void onDeleted(URI resourceUri) {
            fail("Unexpected deletion");
        }
    }
}
