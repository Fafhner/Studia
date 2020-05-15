/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.util.Date;

/**
 *
 * @author Konto
 */
public class Complaint {

    private Integer id;
    private Date complaintDate;
    private String complaintText;
    private String author;
    private String status;

    public Complaint(){}
    
    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the complaintDate
     */
    public Date getComplaintDate() {
        return complaintDate;
    }

    /**
     * @param complaintDate the complaintDate to set
     */
    public void setComplaintDate(Date complaintDate) {
        this.complaintDate = complaintDate;
    }

    /**
     * @return the complaintText
     */
    public String getComplaintText() {
        return complaintText;
    }

    /**
     * @param complaintText the complaintText to set
     */
    public void setComplaintText(String complaintText) {
        this.complaintText = complaintText;
    }

    /**
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author the author to set
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

}
