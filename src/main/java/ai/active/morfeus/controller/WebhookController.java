package ai.active.morfeus.controller;

import ai.active.fulfillment.webhook.WebhookUtil;
import ai.active.fulfillment.webhook.data.request.MorfeusWebhookRequest;
import ai.active.fulfillment.webhook.data.response.Status;
import ai.active.fulfillment.webhook.data.response.WebhookResponse;
import ai.active.morfeus.Exception.Validation;
import ai.active.morfeus.service.ValidationService;
import ai.active.morfeus.service.WebhookService;
import ai.active.morfeus.utils.TemplateConversionUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class WebhookController {
  @Autowired
  private final ObjectMapper objectMapper;

  @Autowired
  private WebhookService webhookService;

  @Autowired
  private ValidationService validationService;

  @Autowired
  private TemplateConversionUtil templateConversionUtil;

  private static final String SECRET = "morFeu5";

  private static final String SIGNATURE_MISMATCH = "Signature Mismatch";

  public WebhookController(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Operation(tags = "Student Information", summary = "Check if the Student is Existing or not ?", requestBody =
  @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(example = "{\"id\":\"eb9f3d03-d6f2-442e-bb75-f7770ac0bbe9\",\"event\":\"wf_prompt\",\"user\":{\"id\":\"9\",\"profile\":{\"firstName\":\"918983726887\"},\"channel_id\":\"918983726887\",\"logged_id\":false,\"customer_id\":\"918983726887\"},\"bot\":{\"id\":\"2\",\"channel_type\":\"wn\",\"channel_id\":\"6531632959\",\"developer_mode\":true,\"sync\":false},\"request\":{\"type\":\"text\",\"text\":\"show\"},\"nlp\":{\"version\":\"v1\",\"data\":{\"intent\":{\"name\":\"show-more\",\"feature\":null,\"confidence\":100,\"classifierName\":\"ruleEngine\",\"adversarialScore\":null,\"starter\":true,\"userSelection\":false},\"intents\":[{\"name\":\"qry-accountenquiry\",\"feature\":null,\"confidence\":93.56175065040588,\"classifierName\":\"unifiedAPIv2Engine\",\"adversarialScore\":0.449097007513,\"starter\":false,\"userSelection\":false}],\"entities\":{\"intentModifier\":[{\"name\":\"intentModifier\",\"value\":null,\"extractorName\":\"unifiedAPIv2Engine\",\"modifiers\":[]},{\"name\":\"intentModifier\",\"value\":\"\",\"extractorName\":\"unifiedAPIv2Engine\",\"modifiers\":null}]},\"sentimentReport\":{\"positiveConfidence\":null,\"negativeConfidence\":null,\"neutralConfidence\":null,\"polarity\":null},\"parseOutputs\":[{\"action\":{\"name\":\"view\"}}],\"langCode\":\"en\",\"tentativeContextChange\":false,\"compoundQuery\":false,\"maskedMessage\":\"show\"}},\"workflow\":{\"additionalParams\":null,\"workflowVariables\":{\"parse_product\":\"view\"},\"globalVariables\":null,\"requestVariables\":{},\"nodeId\":\"Step-2\",\"workflowId\":\"show-more\",\"status\":\"proceed\",\"dataVersion\":null,\"enableJumpNode\":true}}"))))
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(example = "{\"messages\":[{\"type\":\"carousel\",\"content\":[{\"buttons\":[{\"title\":\"Select\",\"type\":\"postback\",\"payload\":\"{\\\"data\\\":{\\\"sys_number\\\":\\\"7223\\\"}, \\\"intent\\\": \\\"txn-accountsettings\\\"}\",\"intent\":\"txn-accountsettings\"}],\"title\":\" *Air India*  \\nPNR Number : 4055467223 \\nDate : 1st May 2022\",\"subtitle\":\"\",\"header\":false},{\"buttons\":[{\"title\":\"Select\",\"type\":\"postback\",\"payload\":\"{\\\"data\\\":{\\\"sys_number\\\":\\\"7770\\\"}, \\\"intent\\\": \\\"txn-accountsettings\\\"}\",\"intent\":\"txn-accountsettings\"}],\"title\":\" *Indigo*  \\nPNR Number : 45066127770 \\nDate : 24th April 2022\",\"subtitle\":\"\",\"header\":false}]}],\"status\":\"success\"}"))),
      @ApiResponse(responseCode = "500", description = "Internal server Error", content = @Content(schema = @Schema(example = "{\"errorCode\":\"500\",\"description\":\"Internal server error!\"}"))),
      @ApiResponse(responseCode = "404", description = "Bot not found!", content = @Content(schema = @Schema(example = "{\"errorCode\":\"404\",\"description\":\"Bot not found\"}"))),
      @ApiResponse(responseCode = "401", description = "Not authorised!", content = @Content(schema = @Schema(example = "{\"errorCode\":\"401\",\"description\":\"Not Authorised!\"}"))),})
  @PostMapping(path = "/view/student/detail", consumes = "application/json", produces = "application/json")
  public WebhookResponse getExistingNonExistingStudent(@RequestBody(required = true) String body,
      @RequestHeader(name = "X-Hub-Signature", required = true) String signature, HttpServletResponse response) throws Exception {
    MorfeusWebhookRequest request = objectMapper.readValue(body, MorfeusWebhookRequest.class);
    String s = WebhookUtil.generateSignature(objectMapper.writeValueAsString(request), SECRET);
    WebhookResponse webhookResponse = new WebhookResponse();
    if (s.equals(signature)) {
      webhookResponse.setTemplateCode("existing_non_existing_student");
      webhookResponse.setPayload("{\"isActive\":\"yes\",\"isNonActive\":\"no\"}");
      webhookResponse.setMessageCode("EXISTING_NON_EXISTING_USER");
      webhookResponse.setStatus(Status.SUCCESS);
      return webhookResponse;
    } else {
      webhookResponse.setStatus(Status.FAILED);
      throw new Validation(SIGNATURE_MISMATCH, 404);
    }
  }

  @Operation(tags = "Student Information", summary = "This will update the details of the existing student", requestBody =
  @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(example = "{\"id\":\"eb9f3d03-d6f2-442e-bb75-f7770ac0bbe9\",\"event\":\"wf_prompt\",\"user\":{\"id\":\"9\",\"profile\":{\"firstName\":\"918983726887\"},\"channel_id\":\"918983726887\",\"logged_id\":false,\"customer_id\":\"918983726887\"},\"bot\":{\"id\":\"2\",\"channel_type\":\"wn\",\"channel_id\":\"6531632959\",\"developer_mode\":true,\"sync\":false},\"request\":{\"type\":\"text\",\"text\":\"show\"},\"nlp\":{\"version\":\"v1\",\"data\":{\"intent\":{\"name\":\"show-more\",\"feature\":null,\"confidence\":100,\"classifierName\":\"ruleEngine\",\"adversarialScore\":null,\"starter\":true,\"userSelection\":false},\"intents\":[{\"name\":\"qry-accountenquiry\",\"feature\":null,\"confidence\":93.56175065040588,\"classifierName\":\"unifiedAPIv2Engine\",\"adversarialScore\":0.449097007513,\"starter\":false,\"userSelection\":false}],\"entities\":{\"intentModifier\":[{\"name\":\"intentModifier\",\"value\":null,\"extractorName\":\"unifiedAPIv2Engine\",\"modifiers\":[]},{\"name\":\"intentModifier\",\"value\":\"\",\"extractorName\":\"unifiedAPIv2Engine\",\"modifiers\":null}]},\"sentimentReport\":{\"positiveConfidence\":null,\"negativeConfidence\":null,\"neutralConfidence\":null,\"polarity\":null},\"parseOutputs\":[{\"action\":{\"name\":\"view\"}}],\"langCode\":\"en\",\"tentativeContextChange\":false,\"compoundQuery\":false,\"maskedMessage\":\"show\"}},\"workflow\":{\"additionalParams\":null,\"workflowVariables\":{\"parse_product\":\"view\"},\"globalVariables\":null,\"requestVariables\":{},\"nodeId\":\"Step-2\",\"workflowId\":\"show-more\",\"status\":\"proceed\",\"dataVersion\":null,\"enableJumpNode\":true}}"))))
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(example = "{\"messages\":[{\"type\":\"carousel\",\"content\":[{\"buttons\":[{\"title\":\"Select\",\"type\":\"postback\",\"payload\":\"{\\\"data\\\":{\\\"sys_number\\\":\\\"7223\\\"}, \\\"intent\\\": \\\"txn-accountsettings\\\"}\",\"intent\":\"txn-accountsettings\"}],\"title\":\" *Air India*  \\nPNR Number : 4055467223 \\nDate : 1st May 2022\",\"subtitle\":\"\",\"header\":false},{\"buttons\":[{\"title\":\"Select\",\"type\":\"postback\",\"payload\":\"{\\\"data\\\":{\\\"sys_number\\\":\\\"7770\\\"}, \\\"intent\\\": \\\"txn-accountsettings\\\"}\",\"intent\":\"txn-accountsettings\"}],\"title\":\" *Indigo*  \\nPNR Number : 45066127770 \\nDate : 24th April 2022\",\"subtitle\":\"\",\"header\":false}]}],\"status\":\"success\"}"))),
      @ApiResponse(responseCode = "500", description = "Internal server Error", content = @Content(schema = @Schema(example = "{\"errorCode\":\"500\",\"description\":\"Internal server error!\"}"))),
      @ApiResponse(responseCode = "404", description = "Bot not found!", content = @Content(schema = @Schema(example = "{\"errorCode\":\"404\",\"description\":\"Bot not found\"}"))),
      @ApiResponse(responseCode = "401", description = "Not authorised!", content = @Content(schema = @Schema(example = "{\"errorCode\":\"401\",\"description\":\"Not Authorised!\"}"))),})
  @PostMapping(path = "/view/student/updateDetails", consumes = "application/json", produces = "application/json")
  public WebhookResponse getUpdateDetails(@RequestBody(required = true) String body,
      @RequestHeader(name = "X-Hub-Signature", required = true) String signature, HttpServletResponse response) throws Exception {
    MorfeusWebhookRequest request = objectMapper.readValue(body, MorfeusWebhookRequest.class);
    String s = WebhookUtil.generateSignature(objectMapper.writeValueAsString(request), SECRET);
    WebhookResponse webhookResponse = new WebhookResponse();
    if (s.equals(signature)) {
      webhookResponse.setTemplateCode("update_details");
      webhookResponse.setStatus(Status.SUCCESS);
      return webhookResponse;
    } else {
      webhookResponse.setStatus(Status.FAILED);
      throw new Validation(SIGNATURE_MISMATCH, 404);
    }
  }

  @Operation(tags = "Student Information", summary = "Success Response for student information update", requestBody =
  @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(example = "{\"id\":\"eb9f3d03-d6f2-442e-bb75-f7770ac0bbe9\",\"event\":\"wf_prompt\",\"user\":{\"id\":\"9\",\"profile\":{\"firstName\":\"918983726887\"},\"channel_id\":\"918983726887\",\"logged_id\":false,\"customer_id\":\"918983726887\"},\"bot\":{\"id\":\"2\",\"channel_type\":\"wn\",\"channel_id\":\"6531632959\",\"developer_mode\":true,\"sync\":false},\"request\":{\"type\":\"text\",\"text\":\"show\"},\"nlp\":{\"version\":\"v1\",\"data\":{\"intent\":{\"name\":\"show-more\",\"feature\":null,\"confidence\":100,\"classifierName\":\"ruleEngine\",\"adversarialScore\":null,\"starter\":true,\"userSelection\":false},\"intents\":[{\"name\":\"qry-accountenquiry\",\"feature\":null,\"confidence\":93.56175065040588,\"classifierName\":\"unifiedAPIv2Engine\",\"adversarialScore\":0.449097007513,\"starter\":false,\"userSelection\":false}],\"entities\":{\"intentModifier\":[{\"name\":\"intentModifier\",\"value\":null,\"extractorName\":\"unifiedAPIv2Engine\",\"modifiers\":[]},{\"name\":\"intentModifier\",\"value\":\"\",\"extractorName\":\"unifiedAPIv2Engine\",\"modifiers\":null}]},\"sentimentReport\":{\"positiveConfidence\":null,\"negativeConfidence\":null,\"neutralConfidence\":null,\"polarity\":null},\"parseOutputs\":[{\"action\":{\"name\":\"view\"}}],\"langCode\":\"en\",\"tentativeContextChange\":false,\"compoundQuery\":false,\"maskedMessage\":\"show\"}},\"workflow\":{\"additionalParams\":null,\"workflowVariables\":{\"parse_product\":\"view\"},\"globalVariables\":null,\"requestVariables\":{},\"nodeId\":\"Step-2\",\"workflowId\":\"show-more\",\"status\":\"proceed\",\"dataVersion\":null,\"enableJumpNode\":true}}"))))
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(example = "{\"messages\":[{\"type\":\"carousel\",\"content\":[{\"buttons\":[{\"title\":\"Select\",\"type\":\"postback\",\"payload\":\"{\\\"data\\\":{\\\"sys_number\\\":\\\"7223\\\"}, \\\"intent\\\": \\\"txn-accountsettings\\\"}\",\"intent\":\"txn-accountsettings\"}],\"title\":\" *Air India*  \\nPNR Number : 4055467223 \\nDate : 1st May 2022\",\"subtitle\":\"\",\"header\":false},{\"buttons\":[{\"title\":\"Select\",\"type\":\"postback\",\"payload\":\"{\\\"data\\\":{\\\"sys_number\\\":\\\"7770\\\"}, \\\"intent\\\": \\\"txn-accountsettings\\\"}\",\"intent\":\"txn-accountsettings\"}],\"title\":\" *Indigo*  \\nPNR Number : 45066127770 \\nDate : 24th April 2022\",\"subtitle\":\"\",\"header\":false}]}],\"status\":\"success\"}"))),
      @ApiResponse(responseCode = "500", description = "Internal server Error", content = @Content(schema = @Schema(example = "{\"errorCode\":\"500\",\"description\":\"Internal server error!\"}"))),
      @ApiResponse(responseCode = "404", description = "Bot not found!", content = @Content(schema = @Schema(example = "{\"errorCode\":\"404\",\"description\":\"Bot not found\"}"))),
      @ApiResponse(responseCode = "401", description = "Not authorised!", content = @Content(schema = @Schema(example = "{\"errorCode\":\"401\",\"description\":\"Not Authorised!\"}"))),})
  @PostMapping(path = "/view/student/updateSuccess", consumes = "application/json", produces = "application/json")
  public WebhookResponse updateSuccess(@RequestBody(required = true) String body,
      @RequestHeader(name = "X-Hub-Signature", required = true) String signature, HttpServletResponse response) throws Exception {
    MorfeusWebhookRequest request = objectMapper.readValue(body, MorfeusWebhookRequest.class);
    String s = WebhookUtil.generateSignature(objectMapper.writeValueAsString(request), SECRET);
    WebhookResponse webhookResponse = new WebhookResponse();
    if (s.equals(signature)) {
      webhookResponse.setTemplateCode("update_success");
      webhookResponse.setStatus(Status.SUCCESS);
      return webhookResponse;
    } else {
      webhookResponse.setStatus(Status.FAILED);
      throw new Validation(SIGNATURE_MISMATCH, 404);
    }
  }


  @Operation(tags = "Student Information", summary = "Success Response for student information saved", requestBody =
  @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(example = "{\"id\":\"eb9f3d03-d6f2-442e-bb75-f7770ac0bbe9\",\"event\":\"wf_prompt\",\"user\":{\"id\":\"9\",\"profile\":{\"firstName\":\"918983726887\"},\"channel_id\":\"918983726887\",\"logged_id\":false,\"customer_id\":\"918983726887\"},\"bot\":{\"id\":\"2\",\"channel_type\":\"wn\",\"channel_id\":\"6531632959\",\"developer_mode\":true,\"sync\":false},\"request\":{\"type\":\"text\",\"text\":\"show\"},\"nlp\":{\"version\":\"v1\",\"data\":{\"intent\":{\"name\":\"show-more\",\"feature\":null,\"confidence\":100,\"classifierName\":\"ruleEngine\",\"adversarialScore\":null,\"starter\":true,\"userSelection\":false},\"intents\":[{\"name\":\"qry-accountenquiry\",\"feature\":null,\"confidence\":93.56175065040588,\"classifierName\":\"unifiedAPIv2Engine\",\"adversarialScore\":0.449097007513,\"starter\":false,\"userSelection\":false}],\"entities\":{\"intentModifier\":[{\"name\":\"intentModifier\",\"value\":null,\"extractorName\":\"unifiedAPIv2Engine\",\"modifiers\":[]},{\"name\":\"intentModifier\",\"value\":\"\",\"extractorName\":\"unifiedAPIv2Engine\",\"modifiers\":null}]},\"sentimentReport\":{\"positiveConfidence\":null,\"negativeConfidence\":null,\"neutralConfidence\":null,\"polarity\":null},\"parseOutputs\":[{\"action\":{\"name\":\"view\"}}],\"langCode\":\"en\",\"tentativeContextChange\":false,\"compoundQuery\":false,\"maskedMessage\":\"show\"}},\"workflow\":{\"additionalParams\":null,\"workflowVariables\":{\"parse_product\":\"view\"},\"globalVariables\":null,\"requestVariables\":{},\"nodeId\":\"Step-2\",\"workflowId\":\"show-more\",\"status\":\"proceed\",\"dataVersion\":null,\"enableJumpNode\":true}}"))))
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(example = "{\"messages\":[{\"type\":\"carousel\",\"content\":[{\"buttons\":[{\"title\":\"Select\",\"type\":\"postback\",\"payload\":\"{\\\"data\\\":{\\\"sys_number\\\":\\\"7223\\\"}, \\\"intent\\\": \\\"txn-accountsettings\\\"}\",\"intent\":\"txn-accountsettings\"}],\"title\":\" *Air India*  \\nPNR Number : 4055467223 \\nDate : 1st May 2022\",\"subtitle\":\"\",\"header\":false},{\"buttons\":[{\"title\":\"Select\",\"type\":\"postback\",\"payload\":\"{\\\"data\\\":{\\\"sys_number\\\":\\\"7770\\\"}, \\\"intent\\\": \\\"txn-accountsettings\\\"}\",\"intent\":\"txn-accountsettings\"}],\"title\":\" *Indigo*  \\nPNR Number : 45066127770 \\nDate : 24th April 2022\",\"subtitle\":\"\",\"header\":false}]}],\"status\":\"success\"}"))),
      @ApiResponse(responseCode = "500", description = "Internal server Error", content = @Content(schema = @Schema(example = "{\"errorCode\":\"500\",\"description\":\"Internal server error!\"}"))),
      @ApiResponse(responseCode = "404", description = "Bot not found!", content = @Content(schema = @Schema(example = "{\"errorCode\":\"404\",\"description\":\"Bot not found\"}"))),
      @ApiResponse(responseCode = "401", description = "Not authorised!", content = @Content(schema = @Schema(example = "{\"errorCode\":\"401\",\"description\":\"Not Authorised!\"}"))),})
  @PostMapping(path = "/view/student/info", consumes = "application/json", produces = "application/json")
  public WebhookResponse getStudentInfo(@RequestBody(required = true) String body,
      @RequestHeader(name = "X-Hub-Signature", required = true) String signature, HttpServletResponse response) throws Exception {
    MorfeusWebhookRequest request = objectMapper.readValue(body, MorfeusWebhookRequest.class);
    String s = WebhookUtil.generateSignature(objectMapper.writeValueAsString(request), SECRET);
    WebhookResponse webhookResponse = new WebhookResponse();
    if (s.equals(signature)) {
      webhookResponse.setTemplateCode("student_info_5244");
      webhookResponse.setStatus(Status.SUCCESS);
      return webhookResponse;
    } else {
      webhookResponse.setStatus(Status.FAILED);
      throw new Validation(SIGNATURE_MISMATCH, 404);
    }
  }

  @Operation(tags = "Student Information", summary = "for the selection student gender", requestBody =
  @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(example = "{\"id\":\"eb9f3d03-d6f2-442e-bb75-f7770ac0bbe9\",\"event\":\"wf_prompt\",\"user\":{\"id\":\"9\",\"profile\":{\"firstName\":\"918983726887\"},\"channel_id\":\"918983726887\",\"logged_id\":false,\"customer_id\":\"918983726887\"},\"bot\":{\"id\":\"2\",\"channel_type\":\"wn\",\"channel_id\":\"6531632959\",\"developer_mode\":true,\"sync\":false},\"request\":{\"type\":\"text\",\"text\":\"show\"},\"nlp\":{\"version\":\"v1\",\"data\":{\"intent\":{\"name\":\"show-more\",\"feature\":null,\"confidence\":100,\"classifierName\":\"ruleEngine\",\"adversarialScore\":null,\"starter\":true,\"userSelection\":false},\"intents\":[{\"name\":\"qry-accountenquiry\",\"feature\":null,\"confidence\":93.56175065040588,\"classifierName\":\"unifiedAPIv2Engine\",\"adversarialScore\":0.449097007513,\"starter\":false,\"userSelection\":false}],\"entities\":{\"intentModifier\":[{\"name\":\"intentModifier\",\"value\":null,\"extractorName\":\"unifiedAPIv2Engine\",\"modifiers\":[]},{\"name\":\"intentModifier\",\"value\":\"\",\"extractorName\":\"unifiedAPIv2Engine\",\"modifiers\":null}]},\"sentimentReport\":{\"positiveConfidence\":null,\"negativeConfidence\":null,\"neutralConfidence\":null,\"polarity\":null},\"parseOutputs\":[{\"action\":{\"name\":\"view\"}}],\"langCode\":\"en\",\"tentativeContextChange\":false,\"compoundQuery\":false,\"maskedMessage\":\"show\"}},\"workflow\":{\"additionalParams\":null,\"workflowVariables\":{\"parse_product\":\"view\"},\"globalVariables\":null,\"requestVariables\":{},\"nodeId\":\"Step-2\",\"workflowId\":\"show-more\",\"status\":\"proceed\",\"dataVersion\":null,\"enableJumpNode\":true}}"))))
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(example = "{\"messages\":[{\"type\":\"carousel\",\"content\":[{\"buttons\":[{\"title\":\"Select\",\"type\":\"postback\",\"payload\":\"{\\\"data\\\":{\\\"sys_number\\\":\\\"7223\\\"}, \\\"intent\\\": \\\"txn-accountsettings\\\"}\",\"intent\":\"txn-accountsettings\"}],\"title\":\" *Air India*  \\nPNR Number : 4055467223 \\nDate : 1st May 2022\",\"subtitle\":\"\",\"header\":false},{\"buttons\":[{\"title\":\"Select\",\"type\":\"postback\",\"payload\":\"{\\\"data\\\":{\\\"sys_number\\\":\\\"7770\\\"}, \\\"intent\\\": \\\"txn-accountsettings\\\"}\",\"intent\":\"txn-accountsettings\"}],\"title\":\" *Indigo*  \\nPNR Number : 45066127770 \\nDate : 24th April 2022\",\"subtitle\":\"\",\"header\":false}]}],\"status\":\"success\"}"))),
      @ApiResponse(responseCode = "500", description = "Internal server Error", content = @Content(schema = @Schema(example = "{\"errorCode\":\"500\",\"description\":\"Internal server error!\"}"))),
      @ApiResponse(responseCode = "404", description = "Bot not found!", content = @Content(schema = @Schema(example = "{\"errorCode\":\"404\",\"description\":\"Bot not found\"}"))),
      @ApiResponse(responseCode = "401", description = "Not authorised!", content = @Content(schema = @Schema(example = "{\"errorCode\":\"401\",\"description\":\"Not Authorised!\"}"))),})
  @PostMapping(path = "/view/student/gender", consumes = "application/json", produces = "application/json")
  public WebhookResponse getStudentGender(@RequestBody(required = true) String body,
      @RequestHeader(name = "X-Hub-Signature", required = true) String signature, HttpServletResponse response) throws Exception {
    MorfeusWebhookRequest request = objectMapper.readValue(body, MorfeusWebhookRequest.class);
    String s = WebhookUtil.generateSignature(objectMapper.writeValueAsString(request), SECRET);
    WebhookResponse webhookResponse = new WebhookResponse();
    if (s.equals(signature)) {
      webhookResponse.setTemplateCode("select_gender");
      webhookResponse.setStatus(Status.SUCCESS);
      return webhookResponse;
    } else {
      webhookResponse.setStatus(Status.FAILED);
      throw new Validation(SIGNATURE_MISMATCH, 404);
    }
  }


  @Operation(tags = "Buy Music", summary = "For the selection of type of music", requestBody =
  @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(example = "{\"id\":\"eb9f3d03-d6f2-442e-bb75-f7770ac0bbe9\",\"event\":\"wf_prompt\",\"user\":{\"id\":\"9\",\"profile\":{\"firstName\":\"918983726887\"},\"channel_id\":\"918983726887\",\"logged_id\":false,\"customer_id\":\"918983726887\"},\"bot\":{\"id\":\"2\",\"channel_type\":\"wn\",\"channel_id\":\"6531632959\",\"developer_mode\":true,\"sync\":false},\"request\":{\"type\":\"text\",\"text\":\"show\"},\"nlp\":{\"version\":\"v1\",\"data\":{\"intent\":{\"name\":\"show-more\",\"feature\":null,\"confidence\":100,\"classifierName\":\"ruleEngine\",\"adversarialScore\":null,\"starter\":true,\"userSelection\":false},\"intents\":[{\"name\":\"qry-accountenquiry\",\"feature\":null,\"confidence\":93.56175065040588,\"classifierName\":\"unifiedAPIv2Engine\",\"adversarialScore\":0.449097007513,\"starter\":false,\"userSelection\":false}],\"entities\":{\"intentModifier\":[{\"name\":\"intentModifier\",\"value\":null,\"extractorName\":\"unifiedAPIv2Engine\",\"modifiers\":[]},{\"name\":\"intentModifier\",\"value\":\"\",\"extractorName\":\"unifiedAPIv2Engine\",\"modifiers\":null}]},\"sentimentReport\":{\"positiveConfidence\":null,\"negativeConfidence\":null,\"neutralConfidence\":null,\"polarity\":null},\"parseOutputs\":[{\"action\":{\"name\":\"view\"}}],\"langCode\":\"en\",\"tentativeContextChange\":false,\"compoundQuery\":false,\"maskedMessage\":\"show\"}},\"workflow\":{\"additionalParams\":null,\"workflowVariables\":{\"parse_product\":\"view\"},\"globalVariables\":null,\"requestVariables\":{},\"nodeId\":\"Step-2\",\"workflowId\":\"show-more\",\"status\":\"proceed\",\"dataVersion\":null,\"enableJumpNode\":true}}"))))
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(example = "{\"messages\":[{\"type\":\"carousel\",\"content\":[{\"buttons\":[{\"title\":\"Select\",\"type\":\"postback\",\"payload\":\"{\\\"data\\\":{\\\"sys_number\\\":\\\"7223\\\"}, \\\"intent\\\": \\\"txn-accountsettings\\\"}\",\"intent\":\"txn-accountsettings\"}],\"title\":\" *Air India*  \\nPNR Number : 4055467223 \\nDate : 1st May 2022\",\"subtitle\":\"\",\"header\":false},{\"buttons\":[{\"title\":\"Select\",\"type\":\"postback\",\"payload\":\"{\\\"data\\\":{\\\"sys_number\\\":\\\"7770\\\"}, \\\"intent\\\": \\\"txn-accountsettings\\\"}\",\"intent\":\"txn-accountsettings\"}],\"title\":\" *Indigo*  \\nPNR Number : 45066127770 \\nDate : 24th April 2022\",\"subtitle\":\"\",\"header\":false}]}],\"status\":\"success\"}"))),
      @ApiResponse(responseCode = "500", description = "Internal server Error", content = @Content(schema = @Schema(example = "{\"errorCode\":\"500\",\"description\":\"Internal server error!\"}"))),
      @ApiResponse(responseCode = "404", description = "Bot not found!", content = @Content(schema = @Schema(example = "{\"errorCode\":\"404\",\"description\":\"Bot not found\"}"))),
      @ApiResponse(responseCode = "401", description = "Not authorised!", content = @Content(schema = @Schema(example = "{\"errorCode\":\"401\",\"description\":\"Not Authorised!\"}"))),})
  @PostMapping(path = "/view/music/selection", consumes = "application/json", produces = "application/json")
  public WebhookResponse buyMusicConfirm(@RequestBody(required = true) String body,
      @RequestHeader(name = "X-Hub-Signature", required = true) String signature, HttpServletResponse response) throws Exception {
    MorfeusWebhookRequest request = objectMapper.readValue(body, MorfeusWebhookRequest.class);
    String s = WebhookUtil.generateSignature(objectMapper.writeValueAsString(request), SECRET);
    WebhookResponse webhookResponse = new WebhookResponse();
    if (s.equals(signature)) {
      webhookResponse.setTemplateCode("buy_music_7834");
      webhookResponse.setStatus(Status.SUCCESS);
      return webhookResponse;
    } else {
      webhookResponse.setStatus(Status.FAILED);
      throw new Validation(SIGNATURE_MISMATCH, 404);
    }
  }

  @Operation(tags = "Student Information", summary = "For the validation of mobile number", requestBody =
  @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(example = "{\"id\":\"eb9f3d03-d6f2-442e-bb75-f7770ac0bbe9\",\"event\":\"wf_prompt\",\"user\":{\"id\":\"9\",\"profile\":{\"firstName\":\"918983726887\"},\"channel_id\":\"918983726887\",\"logged_id\":false,\"customer_id\":\"918983726887\"},\"bot\":{\"id\":\"2\",\"channel_type\":\"wn\",\"channel_id\":\"6531632959\",\"developer_mode\":true,\"sync\":false},\"request\":{\"type\":\"text\",\"text\":\"show\"},\"nlp\":{\"version\":\"v1\",\"data\":{\"intent\":{\"name\":\"show-more\",\"feature\":null,\"confidence\":100,\"classifierName\":\"ruleEngine\",\"adversarialScore\":null,\"starter\":true,\"userSelection\":false},\"intents\":[{\"name\":\"qry-accountenquiry\",\"feature\":null,\"confidence\":93.56175065040588,\"classifierName\":\"unifiedAPIv2Engine\",\"adversarialScore\":0.449097007513,\"starter\":false,\"userSelection\":false}],\"entities\":{\"intentModifier\":[{\"name\":\"intentModifier\",\"value\":null,\"extractorName\":\"unifiedAPIv2Engine\",\"modifiers\":[]},{\"name\":\"intentModifier\",\"value\":\"\",\"extractorName\":\"unifiedAPIv2Engine\",\"modifiers\":null}]},\"sentimentReport\":{\"positiveConfidence\":null,\"negativeConfidence\":null,\"neutralConfidence\":null,\"polarity\":null},\"parseOutputs\":[{\"action\":{\"name\":\"view\"}}],\"langCode\":\"en\",\"tentativeContextChange\":false,\"compoundQuery\":false,\"maskedMessage\":\"show\"}},\"workflow\":{\"additionalParams\":null,\"workflowVariables\":{\"parse_product\":\"view\"},\"globalVariables\":null,\"requestVariables\":{},\"nodeId\":\"Step-2\",\"workflowId\":\"show-more\",\"status\":\"proceed\",\"dataVersion\":null,\"enableJumpNode\":true}}"))))
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(example = "{\"messages\":[{\"type\":\"carousel\",\"content\":[{\"buttons\":[{\"title\":\"Select\",\"type\":\"postback\",\"payload\":\"{\\\"data\\\":{\\\"sys_number\\\":\\\"7223\\\"}, \\\"intent\\\": \\\"txn-accountsettings\\\"}\",\"intent\":\"txn-accountsettings\"}],\"title\":\" *Air India*  \\nPNR Number : 4055467223 \\nDate : 1st May 2022\",\"subtitle\":\"\",\"header\":false},{\"buttons\":[{\"title\":\"Select\",\"type\":\"postback\",\"payload\":\"{\\\"data\\\":{\\\"sys_number\\\":\\\"7770\\\"}, \\\"intent\\\": \\\"txn-accountsettings\\\"}\",\"intent\":\"txn-accountsettings\"}],\"title\":\" *Indigo*  \\nPNR Number : 45066127770 \\nDate : 24th April 2022\",\"subtitle\":\"\",\"header\":false}]}],\"status\":\"success\"}"))),
      @ApiResponse(responseCode = "500", description = "Internal server Error", content = @Content(schema = @Schema(example = "{\"errorCode\":\"500\",\"description\":\"Internal server error!\"}"))),
      @ApiResponse(responseCode = "404", description = "Bot not found!", content = @Content(schema = @Schema(example = "{\"errorCode\":\"404\",\"description\":\"Bot not found\"}"))),
      @ApiResponse(responseCode = "401", description = "Not authorised!", content = @Content(schema = @Schema(example = "{\"errorCode\":\"401\",\"description\":\"Not Authorised!\"}"))),})
  @PostMapping(path = "/mobile/number/validation", consumes = "application/json", produces = "application/json")
  public WebhookResponse validateMobileNumber(@RequestBody(required = true) String body,
      @RequestHeader(name = "X-Hub-Signature", required = true) String signature, HttpServletResponse response) throws Exception {
    MorfeusWebhookRequest request = objectMapper.readValue(body, MorfeusWebhookRequest.class);
    return validationService.mobileNumberValidation(request);
  }
}
