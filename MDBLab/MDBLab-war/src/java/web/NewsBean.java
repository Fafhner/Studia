/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import ejb.NewsItem;
import ejb.NewsItemFacadeLocal;
import java.util.List;
import javax.annotation.Resource;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

/**
 *
 * @author Konto
 */
@Named(value = "newsBean")
@RequestScoped
public class NewsBean {

    @Inject
    private NewsItemFacadeLocal facade;
    @Inject
    private JMSContext context;
    @Resource(lookup = "java:app/jms/NewsQueue")
    private javax.jms.Queue queue;

    private String headingText;
    private String bodyText;

    /**
     * Creates a new instance of NewsBean
     */
    public NewsBean() {
    }

    void sendNewsItem(String heading, String body) {
        try {
            TextMessage message = context.createTextMessage();
            //NewsItem e = new NewsItem();
            //e.setHeading(heading);
            //e.setBody(body);
            message.setText(heading+"|"+body);
            context.createProducer().send(queue, message);
        } catch (JMSException ex) {
            ex.printStackTrace();
        }
    }

    public List<NewsItem> getNewsItems() {
        return facade.getAllNewsItems();
    }

    /**
     * @return the headingText
     */
    public String getHeadingText() {
        return headingText;
    }

    /**
     * @param headingText the headingText to set
     */
    public void setHeadingText(String headingText) {
        this.headingText = headingText;
    }

    /**
     * @return the bodyText
     */
    public String getBodyText() {
        return bodyText;
    }

    /**
     * @param bodyText the bodyText to set
     */
    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }

    public String submitNews() {
        this.sendNewsItem(this.getHeadingText(), this.getBodyText());
        return null;
    }

}
