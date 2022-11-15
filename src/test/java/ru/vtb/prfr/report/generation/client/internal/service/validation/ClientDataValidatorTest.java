package ru.vtb.prfr.report.generation.client.internal.service.validation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.vtb.prfr.report.generation.client.client.model.ReportAttribute;
import ru.vtb.prfr.report.generation.client.client.model.ReportMetaInfo;
import ru.vtb.prfr.report.generation.client.client.model.SingleReportGenerationSettings;
import ru.vtb.prfr.report.generation.client.client.model.exception.ValidationException;
import ru.vtb.prfr.report.generation.client.client.model.exception.ValidationExceptionProfile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class ClientDataValidatorTest {

    @Test
    void validateClientRequestSimpleCaseCheck() {
        Map<String, ReportAttribute> attributes = new HashMap<>();
        attributes.put("attr1", createAttribute("attr1","attr1", false));
        attributes.put("attr2", createAttribute("attr2",null, false));
        attributes.put("attr3", createAttribute("attr3",null, false));

        SingleReportGenerationSettings report = createReportGenerationSettings("first", attributes);
        ClientDataValidator clientDataValidator = new ClientDataValidator();
        ValidationExceptionProfile validationExceptionProfile = clientDataValidator.validateClientRequest(report, createJsonNodeFromString(
                "{\n" +
                        "\t\"averagePay\" : 50000.25,\n" +
                        "\t\"credit-info\" : {\n" +
                        "\t\t\"creditAmount\" : 1500000,\n" +
                        "\t\t\"creditTerm\" : 12 \n" +
                        "\t},\n" +
                        "\t\"guaranteeIncomePart\" : [1200, 4500, 3700, 2330],\n" +
                        "\t\"initialPayMSK\" : 400000,\n" +
                        "\t\"insurExtraCharge\" :  900\n" +
                        "}")
        );

        Assertions.assertTrue(validationExceptionProfile.noErrorFound());
    }

    @Test
    void checkFullyValidJsonAllFieldsRequired() {
        Map<String, ReportAttribute> attributes = new HashMap<>();
        attributes.put("averagePay", createAttribute("averagePay", null, true));
        attributes.put("creditAmount", createAttribute("credit-info/creditAmount",null, true));
        attributes.put("creditTerm", createAttribute("credit-info/creditTerm",null, true));
        attributes.put("guaranteeIncomePart0", createAttribute("guaranteeIncomePart/[0]",null, true));
        attributes.put("guaranteeIncomePart1", createAttribute("guaranteeIncomePart/[1]",null, true));
        attributes.put("guaranteeIncomePart2", createAttribute("guaranteeIncomePart/[2]",null, true));
        attributes.put("guaranteeIncomePart3", createAttribute("guaranteeIncomePart/[3]",null, true));
        attributes.put("initialPayMSK", createAttribute("initialPayMSK",null, true));
        attributes.put("insurExtraCharge", createAttribute("insurExtraCharge",null, true));

        SingleReportGenerationSettings report = createReportGenerationSettings("checkFullyValidJson", attributes);
        ClientDataValidator clientDataValidator = new ClientDataValidator();
        ValidationExceptionProfile validationExceptionProfile = clientDataValidator.validateClientRequest(report, createJsonNodeFromString(
                        "{\n" +
                        "\t\"averagePay\" : 50000.25,\n" +
                        "\t\"credit-info\" : {\n" +
                        "\t\t\"creditAmount\" : 1500000,\n" +
                        "\t\t\"creditTerm\" : 12 \n" +
                        "\t},\n" +
                        "\t\"guaranteeIncomePart\" : [1200, 4500, 3700, 2330],\n" +
                        "\t\"initialPayMSK\" : 400000,\n" +
                        "\t\"insurExtraCharge\" :  900\n" +
                        "}")
        );

        Assertions.assertTrue(validationExceptionProfile.noErrorFound());
    }

    @Test
    void checkFullyValidJsonWithSomeNonRequiredFields() {
        Map<String, ReportAttribute> attributes = new HashMap<>();
        attributes.put("averagePay", createAttribute("averagePay", null, false));
        attributes.put("creditAmount", createAttribute("credit-info/creditAmount",null, true));
        attributes.put("creditTerm", createAttribute("credit-info/creditTerm",null, true));
        attributes.put("guaranteeIncomePart0", createAttribute("guaranteeIncomePart/[0]",null, true));
        attributes.put("guaranteeIncomePart1", createAttribute("guaranteeIncomePart/[1]",null, false));
        attributes.put("guaranteeIncomePart2", createAttribute("guaranteeIncomePart/[2]",null, true));
        attributes.put("guaranteeIncomePart", createAttribute("guaranteeIncomePart/[3]",null, false));
        attributes.put("initialPayMSK", createAttribute("initialPayMSK",null, true));
        attributes.put("insurExtraCharge", createAttribute("insurExtraCharge",null, true));

        SingleReportGenerationSettings report = createReportGenerationSettings("checkFullyValidJsonWithSomeNonRequiredFields", attributes);
        ClientDataValidator clientDataValidator = new ClientDataValidator();
        ValidationExceptionProfile validationExceptionProfile = clientDataValidator.validateClientRequest(report, createJsonNodeFromString(
                "{\n" +
                        "\t\"averagePay\" : 50000.25,\n" +
                        "\t\"credit-info\" : {\n" +
                        "\t\t\"creditAmount\" : 1500000,\n" +
                        "\t\t\"creditTerm\" : 12 \n" +
                        "\t},\n" +
                        "\t\"guaranteeIncomePart\" : [1200, 4500, 3700, 2330],\n" +
                        "\t\"initialPayMSK\" : 400000,\n" +
                        "\t\"insurExtraCharge\" :  900\n" +
                        "}")
        );

        Assertions.assertTrue(validationExceptionProfile.noErrorFound());
    }

    @Test
    void checkInValidJsonWithSomeFieldsAbsent() {
        Map<String, ReportAttribute> attributes = new HashMap<>();
        attributes.put("averagePay", createAttribute("averagePay", null, true));
        attributes.put("creditAmount", createAttribute("credit-info/creditAmount",null, true));
        attributes.put("creditTerm", createAttribute("credit-info/creditTerm",null, true));
        attributes.put("guaranteeIncomePart0", createAttribute("guaranteeIncomePart/[0]","12345", true));
        attributes.put("guaranteeIncomePart1", createAttribute("guaranteeIncomePart/[1]","12345", false));
        attributes.put("guaranteeIncomePart2", createAttribute("guaranteeIncomePart/[2]",null, true));
        attributes.put("guaranteeIncomePart3", createAttribute("guaranteeIncomePart/[3]",null, false));
        attributes.put("initialPayMSK", createAttribute("initialPayMSK",null, true));
        attributes.put("insurExtraCharge", createAttribute("insurExtraCharge",null, true));

        SingleReportGenerationSettings report = createReportGenerationSettings("checkFullyValidJsonWithSomeNonRequiredFields", attributes);
        ClientDataValidator clientDataValidator = new ClientDataValidator();
        ValidationExceptionProfile validationExceptionProfile = clientDataValidator.validateClientRequest(report, createJsonNodeFromString(
                        "{\n" +
                        "\t\"credit-info\" : {\n" +
                        "\t\t\"creditAmount\" : 1500000,\n" +
                        "\t\t\"creditTerm\" : 12 \n" +
                        "\t},\n" +
                        "\t\"guaranteeIncomePart\" : [1200, 4500, 3700, 2330],\n" +
                        "\t\"insurExtraCharge\" :  900\n" +
                        "}")
        );

        assertThat(validationExceptionProfile.amountOfErrorsFound()).isEqualTo(2);

        List<String> offendingAttributeNames = validationExceptionProfile.getValidationExceptions()
                .stream()
                .map(ValidationException::getOffendingReportAttributeName)
                .collect(Collectors.toList());

        assertThat(offendingAttributeNames).containsExactly("averagePay", "initialPayMSK");
    }

    @Test
    void testClientProvidedEmptyJson() {
        Map<String, ReportAttribute> attributes = new HashMap<>();
        attributes.put("averagePay", createAttribute("averagePay", null, true));
        attributes.put("creditAmount", createAttribute("credit-info/creditAmount",null, true));
        attributes.put("creditTerm", createAttribute("credit-info/creditTerm",null, true));
        attributes.put("guaranteeIncomePart0", createAttribute("guaranteeIncomePart/[0]","12345", true));
        attributes.put("guaranteeIncomePart1", createAttribute("guaranteeIncomePart/[1]","12345", false));
        attributes.put("guaranteeIncomePart2", createAttribute("guaranteeIncomePart/[2]",null, true));
        attributes.put("guaranteeIncomePart3", createAttribute("guaranteeIncomePart/[3]",null, false));
        attributes.put("initialPayMSK", createAttribute("initialPayMSK",null, true));
        attributes.put("insurExtraCharge", createAttribute("insurExtraCharge",null, true));

        SingleReportGenerationSettings report = createReportGenerationSettings("checkFullyValidJsonWithSomeNonRequiredFields", attributes);
        ClientDataValidator clientDataValidator = new ClientDataValidator();
        ValidationExceptionProfile validationExceptionProfile = clientDataValidator.validateClientRequest(report, new ObjectMapper().createObjectNode());

        assertThat(validationExceptionProfile.amountOfErrorsFound()).isEqualTo(1);
    }

    private static ReportAttribute createAttribute(String valueFrom, String defaultValue, boolean isRequired) {
        ReportAttribute firstAttr = new ReportAttribute();
        firstAttr.setValueFrom(valueFrom);
        firstAttr.setDefaultValue(defaultValue);
        firstAttr.setIsRequired(isRequired);
        return firstAttr;
    }

    private static SingleReportGenerationSettings createReportGenerationSettings(String reportName, Map<String, ReportAttribute> reportAttributes) {
        SingleReportGenerationSettings singleReportGenerationSettings = new SingleReportGenerationSettings();
        ReportMetaInfo reportMetaInfo = new ReportMetaInfo();
        reportMetaInfo.setSysName(reportName);
        singleReportGenerationSettings.setReportMetaInfo(reportMetaInfo);
        singleReportGenerationSettings.setReportAttributes(reportAttributes);
        return singleReportGenerationSettings;
    }

    private JsonNode createJsonNodeFromString(String string) {
        try {
            return new ObjectMapper().readTree(string);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}