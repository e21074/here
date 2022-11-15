package ru.vtb.prfr.report.generation.client.internal.service.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.stereotype.Component;
import ru.vtb.prfr.report.generation.client.client.model.JsonPropertyPathElement;
import ru.vtb.prfr.report.generation.client.client.model.ReportAttribute;
import ru.vtb.prfr.report.generation.client.client.model.SingleReportGenerationSettings;
import ru.vtb.prfr.report.generation.client.client.model.exception.ValidationException;
import ru.vtb.prfr.report.generation.client.client.model.exception.ValidationExceptionProfile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class ClientDataValidator {

    public ValidationExceptionProfile validateClientRequest(SingleReportGenerationSettings reportGenerationSettings, JsonNode clientJson) {
        if (clientJson.isEmpty()) {
            return ValidationExceptionProfile.createEmptyBookmarkJsonProvidedForReport(reportGenerationSettings
                    .getReportMetaInfo().getSysName());
        }
        Map<String, ReportAttribute> jsonAttributesForReport = reportGenerationSettings.getReportAttributes();
        return continueValidationUsingJsonAttributes(jsonAttributesForReport, clientJson);
    }

    private ValidationExceptionProfile continueValidationUsingJsonAttributes(Map<String, ReportAttribute> jsonAttributesForReport, JsonNode clientJson) {

        ValidationExceptionProfile validationExceptionProfile = new ValidationExceptionProfile();

        jsonAttributesForReport.entrySet()
                .stream()
                .map(it -> checkPropertyPresentInClientInput(it.getValue(), clientJson))
                .forEach(it -> it.ifPresent(validationExceptionProfile::addValidationException));

        return validationExceptionProfile;
    }

    /**
     * Checks that the valueFrom is set correctly and present in client json, only in case it is a required field
     */
    private Optional<ValidationException> checkPropertyPresentInClientInput(ReportAttribute reportAttribute, JsonNode clientJson) {
        if (reportAttribute.isRequired()) {
            return checkRequiredPropertyPresent(reportAttribute, clientJson);
        }
        return Optional.empty();
    }

    private static Optional<ValidationException> checkRequiredPropertyPresent(ReportAttribute reportAttribute, JsonNode rootClientJsonNode) {
        List<JsonPropertyPathElement> jsonPropertyPathElements = reportAttribute.toParsedValueFrom();

        JsonNode currentNode = rootClientJsonNode;

        for (JsonPropertyPathElement jsonPropertyPathElement : jsonPropertyPathElements) {
            if (!jsonPropertyPathElement.isArrayIndex()) {

                currentNode = currentNode.get(jsonPropertyPathElement.getValue());

                if (currentNode == null && reportAttribute.getDefaultValue() == null) {
                    return Optional.of(createPropertyIsAbsentValidationException(reportAttribute, jsonPropertyPathElement));
                }
            } else {
                ArrayNode currentNodeAsJsonArray = (ArrayNode) currentNode;
                currentNode = currentNodeAsJsonArray.get(jsonPropertyPathElement.getArrayIndexValue());

                if (currentNode == null) {
                    return Optional.of(createArrayIndexOutOfBoundException(reportAttribute, jsonPropertyPathElement));
                }
            }
        }
        return Optional.empty();
    }

    private static ValidationException createArrayIndexOutOfBoundException(ReportAttribute reportAttribute, JsonPropertyPathElement jsonPropertyPathElement) {
        return new ValidationException(
                jsonPropertyPathElement.getValue(),
                String.format(
                        "Provided index of the json array - '%s' is out of bounds, full property path : '%s'",
                        jsonPropertyPathElement.getArrayIndexValue(),
                        reportAttribute.getValueFrom()
                )
        );
    }

    private static ValidationException createPropertyIsAbsentValidationException(ReportAttribute reportAttribute, JsonPropertyPathElement jsonPropertyPathElement) {
        return new ValidationException(
                reportAttribute.getValueFrom(),
                String.format("In client provided json property '%s' is absent," +
                        " though is required and no default value were specified." +
                        " Full property path '%s'", jsonPropertyPathElement.getValue(), reportAttribute.getValueFrom())
        );
    }
}