/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.adapter.deferred.response;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "AdapterDocRetrieveDeferredResponse", portName = "AdapterDocRetrieveDeferredResponsePortSoap", endpointInterface = "gov.hhs.fha.nhinc.adapterdocretrievedeferredresp.AdapterDocRetrieveDeferredResponsePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterdocretrievedeferredresp", wsdlLocation = "WEB-INF/wsdl/AdapterDocRetrieveDeferredResponseUnsecured/AdapterDocRetrieveDeferredResp.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterDocRetrieveDeferredResponseUnsecured {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType crossGatewayRetrieveResponse(gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveResponseType crossGatewayRetrieveResponse) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}