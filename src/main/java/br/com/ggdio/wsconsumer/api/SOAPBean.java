package br.com.ggdio.wsconsumer.api;

public class SOAPBean {

	private TO configuration;
	private TO model;

	public SOAPBean() {
		this(new TO(), new TO());
	}

	public SOAPBean(TO configuration, TO model) {
		this.configuration = configuration;
		this.model = model;
	}

	public TO getConfiguration() {
		return configuration;
	}

	public void setConfiguration(TO configuration) {
		this.configuration = configuration;
	}

	public TO getModel() {
		return model;
	}

	public void setModel(TO model) {
		this.model = model;
	}
	
	public String getWSDLUrl() {
		return getConfiguration().getString(Constants.KEY_WSDL);
	}
	
	public void setWSDLUrl(String wsdlUrl) {
		if(!wsdlUrl.toLowerCase().endsWith("?wsdl"))
			wsdlUrl = wsdlUrl.concat("?wsdl");
		getConfiguration().addData(Constants.KEY_WSDL, wsdlUrl);
	}
	
	public String getProtocol() {
		return getConfiguration().getString(Constants.KEY_SOAP_PROTOCOL);
	}
	
	public void setProtocol(String protocol) {
		getConfiguration().addData(Constants.KEY_SOAP_PROTOCOL, protocol);
	}
	
	public String getService() {
		return getConfiguration().getString(Constants.KEY_SERVICE);
	}
	
	public void setService(String service) {
		getConfiguration().addData(Constants.KEY_SERVICE, service);
	}
	
	public String getTargetNamespace() {
		return getConfiguration().getString(Constants.KEY_TARGET_NAMESPACE);
	}
	
	public void setTargetNamespace(String targetNamespace) {
		getConfiguration().addData(Constants.KEY_TARGET_NAMESPACE, targetNamespace);
	}
	
	public String getPort() {
		return getConfiguration().getString(Constants.KEY_PORT);
	}
	
	public void setPort(String port) {
		getConfiguration().addData(Constants.KEY_PORT, port);
	}
	
	public String getOperation() {
		return getConfiguration().getString(Constants.KEY_OPERATION);
	}
	
	public void setOperation(String operation) {
		getConfiguration().addData(Constants.KEY_OPERATION, operation);
	}
	
	public String getOperationInputName() {
		return getConfiguration().getString(Constants.KEY_OPERATION_INPUT_NAME);
	}
	
	public void setOperationInputName(String operation) {
		getConfiguration().addData(Constants.KEY_OPERATION_INPUT_NAME, operation);
	}
	
	public String getOperationOutputName() {
		return getConfiguration().getString(Constants.KEY_OPERATION_OUTPUT_NAME);
	}
	
	public void setOperationOutputName(String operation) {
		getConfiguration().addData(Constants.KEY_OPERATION_OUTPUT_NAME, operation);
	}

}