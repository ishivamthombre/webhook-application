package ai.active.morfeus.service;

import ai.active.fulfillment.webhook.data.request.MorfeusWebhookRequest;
import ai.active.fulfillment.webhook.data.request.NlpV1;
import ai.active.fulfillment.webhook.data.response.*;
import ai.active.morfeus.constants.Constants;
import ai.active.morfeus.model.ButtonModel;
import ai.active.morfeus.utils.ApplicationLogger;
import ai.active.morfeus.utils.BookingDetailUtils;
import ai.active.morfeus.utils.TemplateConversionUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ValidationService {

  public WebhookResponse mobileNumberValidation(MorfeusWebhookRequest request) {
    WebhookResponse webhookResponse = new WebhookResponse();
    String regex = "^([6-9]\\d{9})$";
    Pattern p = Pattern.compile(regex);
    String mobile_number = request.getWorkflowParams().getRequestVariables().get("any.freetext");
    if (mobile_number != null) {
      Matcher m = p.matcher(mobile_number);
      if (m.matches())
        webhookResponse.setStatus(Status.SUCCESS);
      else {
        webhookResponse.setMessageCode("ERROR_ON_MOBILE_NUMBER_VALIDATION");
        webhookResponse.setStatus(Status.FAILED);
      }
    }
    return webhookResponse;
  }
}
