/*
 * Created on 12-gen-2005
 *
 *
 */
package it.iccu.sbn.web.report;

import it.iccu.sbn.ejb.vo.IdentityVO;

import java.lang.reflect.Method;
import java.util.Collection;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.apache.log4j.Logger;

public class CommonReportDS implements JRDataSource {

    static Logger log = Logger.getLogger(CommonReportDS.class);

    private Object[] data = null;

    private int index = -1;

    private Class c = IdentityVO.class;

    public CommonReportDS(Collection d, Class c) {
        super();
        this.data = d.toArray();
        this.c = c;
    }

    public CommonReportDS(Collection d) {
        super();
        this.data = d.toArray();
    }

    /*
     * (non-Javadoc)
     *
     * @see net.sf.jasperreports.engine.JRDataSource#next()
     */
    public boolean next() throws JRException {
        index++;
        return (index < data.length);
    }

    /*
     * (non-Javadoc)
     *
     * @see net.sf.jasperreports.engine.JRDataSource#getFieldValue(net.sf.jasperreports.engine.JRField)
     */
    public Object getFieldValue(JRField field) throws JRException {
        Object value = null;
        String fieldName = field.getName();
        try {
            Method method = this.c.getMethod("get" + fieldName, (Class[])null);
            value = method.invoke(data[index], (Object[])null);
        } catch (NoSuchMethodException e1) {
            log.warn("Field not found in DataSource:" + fieldName);
            value = "Field Not Found";
        } catch (Exception e1) {
            log.warn("Error invoking method: get[" + fieldName + "]");
            value = "Field Not Found";
        }
        return value;
    }

}
