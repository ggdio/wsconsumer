package br.com.ggdio.wsconsumer.api;

/**
 * SOAP TO Wrapper
 * @author Guilherme Dio
 *
 */
public class SOAPBean {

	private TO model;

	public SOAPBean() {
		this(new TO());
	}
	
	public SOAPBean(TO configuration, TO input, TO output){
		TO model = new TO();
		model.addData(Constants.KEY_CONFIGURATION, configuration);
		model.addData(Constants.KEY_INPUT_PARTS, input);
		model.addData(Constants.KEY_OUTPUT_PARTS, output);
		this.model = model;
	}

	public SOAPBean(TO model) {
		this.model = model;
	}
	
	public TO getModel() {
		return model;
	}
	
	public void setModel(TO model) {
		this.model = model;
	}
	
	public TO getConfiguration() {
		return (TO) getModel().getData(Constants.KEY_CONFIGURATION);
	}

	public void setConfiguration(TO configuration) {
		getModel().addData(Constants.KEY_CONFIGURATION, configuration);
	}
	
	public TO getInputParts(){
		return (TO) getModel().getData(Constants.KEY_INPUT_PARTS);
	}
	
	public void setInputParts(TO inputParts){
		getModel().addData(Constants.KEY_INPUT_PARTS, inputParts);
	}
	
	public TO getOutputParts(){
		return (TO) getModel().getData(Constants.KEY_INPUT_PARTS);
	}
	
	public void setOutputParts(TO outputParts){
		getModel().addData(Constants.KEY_OUTPUT_PARTS, outputParts);
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
	
	public String getTargetNamespace() {
		return getConfiguration().getString(Constants.KEY_TARGET_NAMESPACE);
	}
	
	public void setTargetNamespace(String targetNamespace) {
		getConfiguration().addData(Constants.KEY_TARGET_NAMESPACE, targetNamespace);
	}
	
	public String getService() {
		return getConfiguration().getString(Constants.KEY_SERVICE);
	}
	
	public void setService(String service) {
		getConfiguration().addData(Constants.KEY_SERVICE, service);
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