package co.oslc.refimpl.rm;

import java.util.HashSet;

import jakarta.ws.rs.BadRequestException;
import org.eclipse.lyo.oslc.domains.rm.Requirement;
import org.eclipse.lyo.shacl.ValidationReport;
import org.eclipse.lyo.shacl.ValidationResult;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ValidatedResourceTest {
    @Test
    public void summaryIsOnlyCalculatedOnDemand() throws Exception {
        ValidationReport report = new ValidationReport() {
            @Override
            public HashSet<ValidationResult> getResult() {
                throw new AssertionError("Report details should not be read eagerly");
            }
        };
        ValidatedResource<Requirement> invalid = new ValidatedResource<>(new Requirement(), false, report);
        assertSame(report, invalid.report());
        assertThrows(AssertionError.class, invalid::getSummaryMessage);

        ValidatedResource<Requirement> valid = new ValidatedResource<>(new Requirement(), true, report);
        RequirementValidation.throwBadRequestIfInvalid(valid);
        assertEquals("Resource validation passed", valid.getSummaryMessage());
    }

    @Test
    public void validResourceRetainsResourceAndReport() {
        Requirement requirement = new Requirement();
        requirement.setTitle("A requirement");
        requirement.setIdentifier("id");

        ValidatedResource<Requirement> result = RequirementValidation.validate(requirement);

        assertSame(requirement, result.resource());
        assertTrue(result.valid());
        assertTrue(result.report().isConforms());
        RequirementValidation.throwBadRequestIfInvalid(result);
    }

    @Test
    public void invalidResourceReturnsReportWithoutThrowing() {
        Requirement requirement = new Requirement();
        ValidatedResource<Requirement> result = RequirementValidation.validate(requirement);

        assertSame(requirement, result.resource());
        assertFalse(result.valid());
        assertFalse(result.report().isConforms());
        assertTrue(result.report().getResult().stream()
                .anyMatch(error -> error.getResultPath().toString().endsWith("/title")));
        assertTrue(result.report().getResult().stream()
                .anyMatch(error -> error.getResultPath().toString().endsWith("/identifier")));

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> RequirementValidation.throwBadRequestIfInvalid(result));
        assertEquals(400, exception.getResponse().getStatus());
        assertEquals(result.getSummaryMessage(), exception.getResponse().getEntity());
        assertTrue(result.getSummaryMessage().contains("http://purl.org/dc/terms/title"));
        assertTrue(result.getSummaryMessage().contains("http://purl.org/dc/terms/identifier"));
    }

    @Test
    public void nullResourceReturnsInvalidResultWithoutThrowing() {
        ValidatedResource<Requirement> result = RequirementValidation.validate(null);

        assertFalse(result.valid());
        assertNull(result.resource());
        assertNull(result.report());
        assertEquals("A resource is required", result.getSummaryMessage());
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> RequirementValidation.throwBadRequestIfInvalid(result));
        assertEquals(400, exception.getResponse().getStatus());
    }
}
