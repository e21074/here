package ru.vtb.prfr.report.generation.client.services;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.vtb.msa.pdoc.convert.unijson.api.model.PoiJSON;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * Doc generation service API.
 *
 * @author idurdyev
 */
@Service
public class DocGenerationClientService {
    @Value("${report.config.doc-generation.host}")
    private String DOC_GENERATION_HOST;
    @Value("${report.config.doc-generation.context-path}")
    private String DOC_GENERATION_CONTEXT_PATH;
    private final String DOC_GENERATION_GENERATE_REPORT_API = "/generateReport";

    private final RestTemplate docGenerationTemplate;

    public DocGenerationClientService(@Qualifier("docGenerationRestTemplate") RestTemplate docGenerationTemplate) {
        this.docGenerationTemplate = docGenerationTemplate;
    }

    /**
     * Generate report
     * @param templateInputStream dotx file
     * @param poiJSON POIJson object
     * @return generated file (docx | pdf)
     */
    public InputStream generateReport(InputStream templateInputStream, PoiJSON poiJSON) throws IOException {
        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set("Content-Type", "multipart/form-data");

        HttpHeaders filePartHeaders = new HttpHeaders();
        filePartHeaders.setContentType(MediaType.TEXT_PLAIN);
        HttpEntity<ByteArrayResource> fileAsResource = new HttpEntity<>(
                new ByteArrayResource(IOUtils.toByteArray(templateInputStream)) {
                    @Override
                    public String getFilename() {
                        return String.valueOf(poiJSON.getTemplateId());
                    }
                },
                filePartHeaders
        );

        HttpHeaders messagePartHeaders = new HttpHeaders();
        messagePartHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PoiJSON> messagePart = new HttpEntity<>(poiJSON, messagePartHeaders);

        requestBody.add("template", fileAsResource);
        requestBody.set("poiJson", messagePart);

        HttpEntity<MultiValueMap<String,Object>> requestEntity = new HttpEntity<>(requestBody, requestHeaders);

        final String GENERATION_API_URL = getDocGenerationApiURL(DOC_GENERATION_GENERATE_REPORT_API);
        ResponseEntity<Resource> response = docGenerationTemplate.exchange(GENERATION_API_URL,
                HttpMethod.POST,
                requestEntity,
                Resource.class);
        return Objects.requireNonNull(response.getBody()).getInputStream();
    }

    private String getDocGenerationApiURL(String apiURI) {
        return DOC_GENERATION_HOST + DOC_GENERATION_CONTEXT_PATH + apiURI;
    }

}
