package br.com.ggdio.wsconsumer.soap.model;

import java.util.ArrayList;
import java.util.List;

import br.com.ggdio.wsconsumer.common.model.TO;
import br.com.ggdio.wsconsumer.soap.api.constant.WSDLConstants;

/**
 * SOAP Webservice Instance Model
 * @author Guilherme Dio
 *
 */
public class Instance extends TO{
	
	private static final long serialVersionUID = 1L;
	
	public static final String WSDL = "WSDL";
	public static final String TARGET_NAMESPACE = "TARGET_NAMESPACE";
	public static final String ELEMENT_FORM_DEFAULT = "ELEMENT_FORM_DEFAULT";
	public static final String STYLE = "STYLE";
	public static final String SERVICES = "SERVICES";

	public Instance() {
		this("", "", WSDLConstants.ELEMENT_FORM_DEFAULT_QUALIFIED, WSDLConstants.STYLE_LITERAL, new ArrayList<Service>());
	}

	public Instance(String wsdl, String targetNamespace, String elementFormDefault, String style, List<Service> services) {
		setWSDL(wsdl);
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