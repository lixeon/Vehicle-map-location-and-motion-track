/*
 * LBSAPP
 */
package com.app.lbs.common.sms.qxt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.app.lbs.common.sms.SmsReqMessage;
import com.app.lbs.common.sms.SmsRespMessage;
import com.app.lbs.common.sms.SmsSender;
import com.app.platform.base.utils.CommonUtil;
import com.app.platform.base.utils.StringUtil;

/**
 * Abstract Sms Sender
 *
 * @author colen
 *
 */
public abstract class AbstractSmsSender implements SmsSender {

    /** logger */
    private static final Logger LOG = Logger.getLogger(AbstractSmsSender.class);
    
    /** http request time out */
    private int timeout = 3000; 

    
    
    @Override
    public SmsRespMessage send(SmsReqMessage smsMsg) {
        SmsRespMessage errorResp = checkReqMessage(smsMsg);
        if (errorResp != null) {
            return errorResp;
        }
        
        // Packet Message
        Map<String, String> args = packetReqMessage(smsMsg);
        
        String respText = executeSend(getReqUrl(), args, getEncode());
        
        if (LOG.isInfoEnabled()) {
            LOG.info("SMS Server Response: " + respText);
        }

        if (StringUtil.isTrimEmpty(respText) || "null".equals(respText)) {
            if (LOG.isInfoEnabled()) {
                LOG.info("Try Resend SMS Again! " + smsMsg.getContent());
            }
            try {
                Thread.sleep(1000L * 2);
            } catch (InterruptedException e) {
                // ignore
            }
            respText = executeSend(getReqUrl(), args, getEncode());
            
            if (LOG.isInfoEnabled()) {
                LOG.info("SMS Server Response (2): " + respText);
            }
            
        }

        return parseRespText(respText);
    }
    
    
    /**
     * get Encode
     *
     * @return
     */
    public abstract String getEncode();


    /**
     * get Request Url
     *
     * @return
     */
    public abstract String getReqUrl();



    /**
     * Parse Response
     *
     * @param respText
     * @return
     */
    public abstract SmsRespMessage parseRespText(String respText);



    /**
     * Packet Request Message
     *
     * @param smsMsg
     * @return
     */
    public abstract Map<String, String> packetReqMessage(SmsReqMessage smsMsg);


    /**
     * Check Req Message
     *
     * @param smsMsg
     * @return
     */
    protected SmsRespMessage checkReqMessage(SmsReqMessage smsMsg) {
        // check system configuration
        boolean enable = isEnable();
        if (!enable) {
            LOG.warn("SMS Channel now is disable!");
            SmsRespMessage resp = new SmsRespMessage();
            resp.setRespCode(ERROR_901);
            return resp;
        }

        if (smsMsg == null) {
            LOG.warn("sms message object is null, RETURN!");
            SmsRespMessage resp = new SmsRespMessage();
            resp.setRespCode(ERROR_900);
            return resp;
        }

        if (StringUtil.isTrimEmpty(smsMsg.getTo())) {
            LOG.warn("not specified target Number, RETURN!");
            SmsRespMessage resp = new SmsRespMessage();
            resp.setRespCode(ERROR_900);
            return resp;
        }
        
        return null;
    }
    

    
    
    
    /**
     * Execute Send Action
     *
     * @param reqUrl
     * @param params
     * @return response Text
     */
    protected String executeSend(String reqUrl, Map<String, String> params, String encode) {
        HttpURLConnection conn = null;
        String resp = null;
        OutputStream out = null;
        InputStream in = null;
        try {
            StringBuilder bud = new StringBuilder();
            for (Iterator<?> iter = params.entrySet().iterator(); iter.hasNext();) {
                Entry<?, ?> element = (Entry<?, ?>) iter.next();
                bud.append(element.getKey().toString());
                bud.append("=");
                bud.append(URLEncoder.encode(element.getValue().toString(), encode));
                bud.append("&");
            }

            if (bud.length() > 0) {
                bud.deleteCharAt(bud.length() - 1);
            }

            URL url = new URL(reqUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(this.timeout);
            conn.setReadTimeout(this.timeout * 2);
            conn.setDoOutput(true);
            byte[] b = bud.toString().getBytes();
            out = conn.getOutputStream();
            out.write(b, 0, b.length);
            out.flush();

            in = conn.getInputStream();
            byte[] echo = new byte[1 * 1024];
            int len = in.read(echo);
            resp = (new String(echo, 0, len)).trim();
            int code = conn.getResponseCode();
            if (code != 200) {
                resp = "error:" + code;
            }
        } catch (IOException ioe) {
            // log
            LOG.error(ioe.getMessage());
        } finally {
            CommonUtil.close(out);
            CommonUtil.close(in);
            if (conn != null) {
                conn.disconnect();
            }
        }

        return resp;
    }


/*    private String executeSend(String svrUrl, Map<String, String> args) {
        // DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> e : args.entrySet()) {
                list.add(new BasicNameValuePair(e.getKey(), e.getValue()));
            }

            String resp = this.executor.execute(Request.Post(this.url)
                .connectTimeout(this.timeout)
                .socketTimeout(this.timeout)
                .bodyForm(list, Charset.forName(this.encode)))
                .returnContent().asString();

            // log
            LOG.info("Resp: " + resp);

            return resp;
        } catch (IOException ioe) {
            // log
            LOG.error(ioe.getMessage());
            // throw business exception
            throw new CommonBusinessException(ioe.getMessage());
        } finally {
            // httpclient.getConnectionManager().shutdown();
        }
    }*/

}
