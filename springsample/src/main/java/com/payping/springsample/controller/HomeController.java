package com.payping.springsample.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payping.springsample.model.ConfirmPayRequestModel;
import com.payping.springsample.model.CreatePayRequestModel;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

@Controller
public class HomeController {


    String PayPing_Token = "Bearer PUT_YOUR_PAYPING_TOKEN_HERE";
    String PayPing_PayURL = "https://api.payping.ir/v2/pay";
    String Return_URL = "http://localhost:8080/ConfirmPay";

    //Home Page
    @RequestMapping("/")
    public String index() {
        return "index";
    }

    //Create a Payment
    @PostMapping("/CreatePay")
    public RedirectView CreatePay(@RequestParam Map<String, String> model) {

        String PayCode = null;

        //Map Received Data To Model *Optional
        CreatePayRequestModel reqmodel = new CreatePayRequestModel();
        reqmodel.setAmount(Integer.parseInt(model.get("amount")));
        reqmodel.setPayerName(model.get("payerName"));
        reqmodel.setPayerIdentity(model.get("payerIdentity"));
        reqmodel.setDescription(model.get("description"));
        reqmodel.setClientRefId("YOUR OPTIONAL VALUE");
        reqmodel.setReturnUrl(Return_URL);



        try {
            //Create a Post Request to PayPing Create Payment EndPoint
            URL url = new URL(PayPing_PayURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            //Set Auth Header
            conn.setRequestProperty ("Authorization", PayPing_Token);


            //Handle RequestBody
            String jsonbody = "";
            ObjectMapper mapper = new ObjectMapper();
            //Converting the Object to JSONString
            try {
                jsonbody = mapper.writeValueAsString(reqmodel);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            //Or
            // jsonbody = String.format("{\"amount\":%d,\"returnUrl\":\"%s\"}", model.getAmount(), Return_URL);

            OutputStream os = conn.getOutputStream();
            os.write(jsonbody.getBytes());
            os.flush();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            //Pars PayPing Server Response (Json Object)
            String res = ReadResponse(conn.getInputStream());
            JSONObject obj = new JSONObject(res);
            PayCode = obj.getString("code");

            conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Redirect User To Payment Gateway
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(PayPing_PayURL+"/gotoipg/"+PayCode);
        return redirectView;

    }

    //Confirm a Payment
    //This method is called after user is done with gateway (Return_URL)
    @PostMapping("/ConfirmPay")
    public String ConfirmPay(@RequestParam Map<String, String> model) {

        try {

            //Map Received Data To Model *Optional
            ConfirmPayRequestModel reqmodel = new ConfirmPayRequestModel();
            reqmodel.setAmount(1000);
            reqmodel.setRefid(model.get("refid"));
            // Your Optional Value
            String clientRefId = model.get("clientrefid");

            //Create a Post Request To PayPing Verify EndPoint
            URL url = new URL(PayPing_PayURL+"/verify");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            //Set Auth Header
            conn.setRequestProperty ("Authorization", PayPing_Token);


            //Handle RequestBody
            String jsonbody = "";
            ObjectMapper mapper = new ObjectMapper();
            //Converting the Object to JSONString
            try {
                jsonbody = mapper.writeValueAsString(reqmodel);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            //Or
            // jsonbody = String.format("{\"amount\":%d,\"refId\":\"%s\"}", 1000, model.get("refid"));;

            OutputStream os = conn.getOutputStream();
            os.write(jsonbody.getBytes());
            os.flush();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            //Pars PayPing Server Response (Json Object)
            String res = ReadResponse(conn.getInputStream());
            JSONObject obj = new JSONObject(res);

            // Your payment detail (you can store these)
            String amount = obj.getString("amount");
            String cardNumber = obj.getString("cardNumber");
            String cardHashPan = obj.getString("cardHashPan");
            String merchants = obj.getString("merchants");

            conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Show Confirm Page
        return "confirm";
    }

    //Response Reader Helper
    private static String ReadResponse(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
