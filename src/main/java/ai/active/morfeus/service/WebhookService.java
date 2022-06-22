package ai.active.morfeus.service;

import ai.active.fulfillment.webhook.data.request.MorfeusWebhookRequest;
import ai.active.fulfillment.webhook.data.response.*;
import ai.active.morfeus.utils.ApplicationLogger;
import ai.active.morfeus.model.ButtonModel;
import ai.active.morfeus.utils.BookingDetailUtils;
import ai.active.morfeus.utils.TemplateConversionUtil;
import ai.active.morfeus.constants.Constants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class WebhookService {

  @Autowired
  private final ObjectMapper objectMapper;

  @Autowired
  private TemplateConversionUtil templateConsversionUtil;

  @Autowired
  private BookingDetailUtils bookingDetailUtils;

  @Autowired
  private ResourceLoader resourceLoader;

  private static final String CLASSPATH = "classpath:";
  private static final String STATUS = "Your booking details has been shared successfully. Thank you! \n Please click on the below link to check your booking: \n"
      + "https://drive.google.com/file/d/1ClVx_-19SDqjrJDpzC4H6XqlzRym8spr/view?usp=sharing";

  public WebhookService(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  /**
   *
   * @return static text message
   */
  public String getDownloadStatus() {
    return STATUS;
  }

  /**
   *
   * @return manipulated payload for Template creation
   */
  public List<Map<String, Object>> getBookings() {
    try {
      InputStream resourceAsStream =
          resourceLoader.getResource(CLASSPATH + Constants.FOLDER_PATH + Constants.BOOKINGS + Constants.JSON).getInputStream();
      JsonNode bookingDetails = objectMapper.readValue(resourceAsStream, JsonNode.class);
      return formPayloadFromJson(bookingDetails);
    } catch (IOException e) {
      ApplicationLogger.logError("Error while gettings bookings : ", e);
      return Collections.emptyList();
    }
  }

  /**
   *
   * @return raw payload as List<Map<String, Object>>
   */
  public List<Map<String, Object>> getBookingsPayload() {
    try {
      InputStream resourceAsStream =
          resourceLoader.getResource(CLASSPATH + Constants.FOLDER_PATH + Constants.BOOKINGS + Constants.JSON).getInputStream();
      return objectMapper.readValue( resourceAsStream,new TypeReference<List<Map<String, Object>>>() {
      });
    } catch (IOException e) {
      ApplicationLogger.logError("Error while gettings bookings : ", e);
      return Collections.emptyList();
    }
  }

  // will return list of payloads.
  public List<Map<String, Object>> formPayloadFromJson(JsonNode js) {
    List<Map<String, Object>> listOfFlights = new ArrayList<>();
    for (JsonNode flightDetail : js) {
      Map<String, Object> payloadMap = new HashMap<>();
      payloadMap.put("title", " *" + flightDetail.get("flightName").asText() + "* " + " \n"  + "PNR Number : " + flightDetail.get("orderNumber").asText() + " \n" + "Date : " + flightDetail.get("date").asText());
      payloadMap.put("subtitle", "");
      List<ButtonModel> button = new ArrayList<>();
      button.add(bookingDetailUtils.createButton("Select", "txn-accountsettings", "sys_number", flightDetail.get("displayOrderNumber").asText()));
      payloadMap.put("buttons", button);
      listOfFlights.add(payloadMap);
    }
    return listOfFlights;
  }

  /**
   *
   * @param request
   * @return MorfeusWebhookResponse with Template
   */
  public MorfeusWebhookResponse getDownloadStatusAsTemplate(MorfeusWebhookRequest request) {
    WorkflowValidationResponse workflowValidationResponse = new WorkflowValidationResponse.Builder(Status.SUCCESS).build();
    Content content = new Content();
    content.setImage("https://i.ibb.co/GRzCZpp/success.gif");
    content.setTitle(STATUS);
    content.setSubtitle("Please save the reference id 5zMbyvHX for future reference.");

    CarouselMessage carouselMessage = new CarouselMessage();
    carouselMessage.setType("carousel");
    List<Content> ListOfcontents = new ArrayList<>();
    ListOfcontents.add(content);
    carouselMessage.setContent(ListOfcontents);
    workflowValidationResponse.setMessages(Arrays.asList(carouselMessage));
    return workflowValidationResponse;

  }
}
