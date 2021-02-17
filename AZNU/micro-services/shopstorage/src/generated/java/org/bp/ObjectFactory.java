
package org.bp;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import org.bp.types.Fault;
import org.bp.types.StorageInfo;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.bp package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _PurchaseItemsResponse_QNAME = new QName("http://www.bp.org", "purchaseItemsResponse");
    private final static QName _GetItemsRequest_QNAME = new QName("http://www.bp.org", "getItemsRequest");
    private final static QName _StorageFault_QNAME = new QName("http://www.bp.org", "storageFault");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.bp
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PurchaseItemsRequest }
     * 
     */
    public PurchaseItemsRequest createPurchaseItemsRequest() {
        return new PurchaseItemsRequest();
    }

    /**
     * Create an instance of {@link CancelPurchasingRequest }
     * 
     */
    public CancelPurchasingRequest createCancelPurchasingRequest() {
        return new CancelPurchasingRequest();
    }

    /**
     * Create an instance of {@link CancelPurchasingResponse }
     * 
     */
    public CancelPurchasingResponse createCancelPurchasingResponse() {
        return new CancelPurchasingResponse();
    }

    /**
     * Create an instance of {@link GetItemsResponse }
     * 
     */
    public GetItemsResponse createGetItemsResponse() {
        return new GetItemsResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StorageInfo }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link StorageInfo }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.bp.org", name = "purchaseItemsResponse")
    public JAXBElement<StorageInfo> createPurchaseItemsResponse(StorageInfo value) {
        return new JAXBElement<StorageInfo>(_PurchaseItemsResponse_QNAME, StorageInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Object }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.bp.org", name = "getItemsRequest")
    public JAXBElement<Object> createGetItemsRequest(Object value) {
        return new JAXBElement<Object>(_GetItemsRequest_QNAME, Object.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Fault }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Fault }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.bp.org", name = "storageFault")
    public JAXBElement<Fault> createStorageFault(Fault value) {
        return new JAXBElement<Fault>(_StorageFault_QNAME, Fault.class, null, value);
    }

}
