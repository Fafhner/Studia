/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Konto
 */
@JMSDestinationDefinition(name = "java:app/jms/NewsQueue", interfaceName = "javax.jms.Queue", resourceAdapter = "jmsra", destinationName = "NewsQueue")
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:app/jms/NewsQueue"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class NewsMDB implements MessageListener {

    @PersistenceContext
    private EntityManager em;

    public NewsMDB() {
    }

    @Override
    public void onMessage(Message message) {
        ObjectMessage objmsg = null;
        TextMessage txtmsg = null;
        try {
            if (message instanceof TextMessage){
                String strmsg = ((TextMessage) message).getText();
                NewsItem e = new NewsItem();
                e.setHeading(strmsg.split("\\|")[0]);
                e.setBody(strmsg.split("\\|")[1]);
                em.persist(e);
            }
            if (message instanceof ObjectMessage) {
                objmsg = (ObjectMessage) message;
                NewsItem e = (NewsItem) objmsg.getObject();
                em.persist(e);
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

}
