package org.apache.activemq.web.util;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;

import org.json.JSONArray;
import org.json.JSONObject;

public class MessageTool {

	public static String getBodyToJSONString(Message message) {
		try {
			Object o = getBody(message);
			if (o != null) {
				if (o instanceof String) {
					return o.toString();
				} else if (o instanceof Collection || o.getClass().isArray()) {
					return new JSONArray((Collection) o).toString();
				} else if (o instanceof Object) {
					return new JSONObject(o).toString();
				} else {
					return o.toString();
				}
			}
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public static String getBodyString(Message message) throws JMSException {
		return getBody(message).toString();
	}

	public static Object getBody(Message message) throws JMSException {
		if (message instanceof TextMessage) {
			return ((TextMessage) message).getText();
		}
		if (message instanceof ObjectMessage) {
			try {
				return ((ObjectMessage) message).getObject();
			} catch (Exception e) {
				// message could not be parsed, make the reason available
				return new String("Cannot display ObjectMessage body. Reason: "
						+ e.getMessage());
			}
		}
		if (message instanceof MapMessage) {
			return createMapBody((MapMessage) message);
		}
		if (message instanceof BytesMessage) {
			BytesMessage msg = (BytesMessage) message;
			int len = (int) msg.getBodyLength();
			if (len > -1) {
				byte[] data = new byte[len];
				msg.readBytes(data);
				return new String(data);
			} else {
				return "";
			}
		}
		if (message instanceof StreamMessage) {
			return "StreamMessage is not viewable";
		}

		// unknown message type
		if (message != null) {
			return "Unknown message type [" + message.getClass().getName()
					+ "] " + message;
		}

		return null;
	}

	protected static String createMapBody(MapMessage mapMessage)
			throws JMSException {
		JSONObject answer = new JSONObject();
		Enumeration iter = mapMessage.getMapNames();
		while (iter.hasMoreElements()) {
			String name = (String) iter.nextElement();
			Object value = mapMessage.getObject(name);
			if (value != null) {
				answer.put(name, value);
			}
		}
		return answer.toString();
	}

	public static void main(String[] args) {
		Map m = new HashMap<String, String>();
		m.put("aa", "aaaaaaa");
		m.put("bb", "bbbbbbbbbbbbb");
		System.out.println(new JSONObject(m));
		System.out.println(new JSONObject((new JSONObject(m))));

		System.out.println((new JSONObject(m)).get("aa"));
	}
}
