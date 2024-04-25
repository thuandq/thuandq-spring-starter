package org.thuandq.spring.starter.utils;

import org.thuandq.spring.starter.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public final class XmlUtil {

    /**
     * encode object to String
     *
     * @param data
     * @param clazz truyền khi sử dụng Generics
     * @return
     */
    public static <T> String encode(T data, Class<?>... clazz) {
        try {
            var context = JAXBContext.newInstance(toArray(data, clazz));
            return encode(context, data);
        } catch (Exception e) {
            log.error("error to encode xml", e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * encode object to String
     *
     * @param jaxbContext
     * @param data
     * @return
     */
    public static <T> String encode(JAXBContext jaxbContext, T data) {
        try {
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter productXml = new StringWriter();
            marshaller.marshal(data, productXml);
            return productXml.toString();
        } catch (JAXBException e) {
            log.error("error to encode xml", e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * decode String to object
     *
     * @param data
     * @param clazz truyền kiểu dữ liệu trả về và kiểu dữ liệu generics nếu có
     * @return
     */
    public static <T> T decode(String data, Class<?>... clazz) {
        try {
            var context = JAXBContext.newInstance(clazz);
            return decode(context, data);
        } catch (JAXBException e) {
            log.error("error to decode xml", e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * decode String to object
     *
     * @param xmlString
     * @return
     * @Param context
     */
    public static <T> T decode(JAXBContext context, String xmlString) {
        try {
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (T) unmarshaller.unmarshal(new StringReader(xmlString));
        } catch (JAXBException e) {
            log.error("error to decode xml", e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    private static <T> Class<?>[] toArray(T data, Class<?>... clazzs) {
        List<Class<?>> list = new ArrayList<>(Arrays.asList(clazzs));
        list.add(data.getClass());
        return list.toArray(Class[]::new);
    }
}
