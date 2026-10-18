package co.oslc.refimpl.rm;

import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.text.ParseException;
import javax.xml.datatype.DatatypeConfigurationException;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.lyo.oslc4j.core.model.AbstractResource;
import org.eclipse.lyo.oslc4j.core.exception.OslcCoreApplicationException;
import org.eclipse.lyo.shacl.ValidationReport;
import org.eclipse.lyo.validation.ValidatorFactory;

public final class RequirementValidation {
    private RequirementValidation() {
    }

    public static <T extends AbstractResource> ValidatedResource<T> validate(T resource) {
        if (resource == null) {
            return new ValidatedResource<>(null, false, null);
        }

        final ValidationReport report;
        try {
            report = ValidatorFactory.createValidator().validate(resource);
        } catch (OslcCoreApplicationException | URISyntaxException | ParseException
                | IllegalAccessException | InvocationTargetException | DatatypeConfigurationException
                | InstantiationException | NoSuchMethodException exception) {
            throw new IllegalStateException("Unable to validate resource", exception);
        }

        return new ValidatedResource<>(resource, Boolean.TRUE.equals(report.isConforms()), report);
    }

    public static void throwBadRequestIfInvalid(ValidatedResource<?> validatedResource) {
        if (validatedResource.valid()) {
            return;
        }
        throw badRequest(validatedResource.getSummaryMessage());
    }

    private static BadRequestException badRequest(String message) {
        return new BadRequestException(Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.TEXT_PLAIN_TYPE).entity(message).build());
    }
}
