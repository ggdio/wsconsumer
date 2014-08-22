package br.com.ggdio.wsconsumer.soap.model;

import java.util.List;

import br.com.ggdio.wsconsumer.api.TO;

/**
 * SOAP Webservice Instance Model
 * @author Guilherme Dio
 *
 */
public class Instance extends TO{
	
	private static final long serialVersionUID = 1L;
	
	public static final String WSDL = "WSDL";
	public static final String SOAP_PROTOCOL = "SOAP_PROTOCOL";
	public static final String TARGET_NAMESPACE = "TARGET_NAMESPACE";
	public static final String ELEMENT_FORM_DEFAULT = "ELEMENT_FORM_DEFAULT";
	public static final String STYLE = "STYLE";
	public static final String SERVICES = "SERVICES";

	public Instance() {
		// TODO Auto-generated constructor stub
	}

	public Instance(String wsdl, String soapProtocol, String targetNamespace, String elementFormDefault, String style, List<Service> services) {
		setWSDL(wsdl);
		setSOAPProtocol(soapProtocol);
		setTargetNamespace(targetNamespace);
		setElementFormDefault(elementFormDefault);
		setStyle(style);
		setServices(services);
	}
	
	public String getWSDL(){
		return getString(WSDL);
	}
	
	public void setWSDL(String wsdl){
		addData(WSDL, wsdl);
	}
	
	public String getSOAPProtocol(){
		return getString(SOAP_PROTOCOL);
	}
	
	public void setSOAPProtocol(String soapProtocol){
		addData(SOAP_PROTOCOL, soapProtocol);
	}
	
	public String getTargetNamespace(){
		return getString(TARGET_NAMESPACE);
	}
	
	public void setTargetNamespace(String targetNamespace){
		addData(TARGET_NAMESPACE, targetNamespace);
	}
	
	public String getElementFormDefault(){
		return getString(ELEMENT_FORM_DEFAULT);
	}
	
	public void setElementFormDefault(String elementFormDefault){
		addData(ELEMENT_FORM_DEFAULT, elementFormDefault);
	}
	
	public String getStyle(){
		return getString(STYLE);
	}
	
	public void setStyle(String style){
		addData(STYLE, style);
	}
	
	@SuppressWarnings("unchecked")
	public List<Service> getServices(){
		return (List<Service>) getData(SERVICES);
	}
	
	public void setServices(List<Service> services){
		addData(SERVICES, services);
	}
	
}