package co.oslc.refimpl.rm;

import java.util.stream.Collectors;

import org.eclipse.lyo.oslc4j.core.model.AbstractResource;
import org.eclipse.lyo.shacl.ValidationReport;

/** A validation result; the report is absent when the resource itself is null. */
public record ValidatedResource<T extends AbstractResource>(T resource, boolean valid, ValidationReport report) {
    public String getSummaryMessage() {
        if (valid) {
            return "Resource validation passed";
        }
        if (resource == null) {
            return "A resource is required";
        }
        String details = report.getResult().stream()
                .map(result -> result.getResultPath() + ": " + result.getMessage())
                .sorted()
                .collect(Collectors.joining("; "));
        return "Resource validation failed: " + details;
    }
}
