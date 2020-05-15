/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Konto
 */
@Entity
@Table(name = "COMPLAINTS")
@NamedQueries({
    @NamedQuery(name = "Complaint.findAll", query = "SELECT c FROM Complaint c"),
    @NamedQuery(name = "Complaint.findById", query = "SELECT c FROM Complaint c WHERE c.id = :id"),
    @NamedQuery(name = "Complaint.findByComplaintDate", query = "SELECT c FROM Complaint c WHERE c.complaintDate = :complaintDate"),
    @NamedQuery(name = "Complaint.findByComplaintText", query = "SELECT c FROM Complaint c WHERE c.complaintText = :complaintText"),
    @NamedQuery(name = "Complaint.findByAuthor", query = "SELECT c FROM Complaint c WHERE c.author = :author"),
    @NamedQuery(name = "Complaint.findByStatus", query = "SELECT c FROM Complaint c WHERE c.status = :status")})
public class Complaint implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "COMPLAINT_DATE")
    @Temporal(TemporalType.DATE)
    private Date complaintDate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "COMPLAINT_TEXT")
    private String complaintText;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "AUTHOR")
    private String author;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "STATUS")
    private String status;

    public Complaint() {
    }

    public Complaint(Integer id) {
        this.id = id;
    }

    public Complaint(Integer id, String complaintText, String author, String status) {
        this.id = id;
        this.complaintText = complaintText;
        this.author = author;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getComplaintDate() {
        return complaintDate;
    }

    public void setComplaintDate(Date complaintDate) {
        this.complaintDate = complaintDate;
    }

    public String getComplaintText() {
        return complaintText;
    }

    public void setComplaintText(String complaintText) {
        this.complaintText = complaintText;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Complaint)) {
            return false;
        }
        Complaint other = (Complaint) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Complaint[ id=" + id + " ]";
    }
    
}
