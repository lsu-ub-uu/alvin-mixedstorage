package se.uu.ub.cora.alvin.mixedstorage.fedora;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;

public class TransformerFactorySpy extends TransformerFactory {

	public static TransformerFactorySpy factory = new TransformerFactorySpy();
	// public static List<TransformerFactorySpy> factories = new ArrayList<>();
	public List<TransformerSpy> transformers = new ArrayList<>();

	public Map<String, Object> attributes = new HashMap<>();
	public Map<String, Boolean> features = new HashMap<>();

	public List<Source> sources = new ArrayList<>();

	public TransformerFactorySpy() {
		// factories.add(this);
		factory = this;
	}

	public static TransformerFactorySpy newInstance() {
		// factories.add(factory);
		return factory;
	}

	@Override
	public Source getAssociatedStylesheet(Source arg0, String arg1, String arg2, String arg3)
			throws TransformerConfigurationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getAttribute(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ErrorListener getErrorListener() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getFeature(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public URIResolver getURIResolver() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Templates newTemplates(Source arg0) throws TransformerConfigurationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Transformer newTransformer() throws TransformerConfigurationException {
		TransformerSpy transformerSpy = new TransformerSpy();
		transformers.add(transformerSpy);
		return transformerSpy;
	}

	@Override
	public Transformer newTransformer(Source arg0) throws TransformerConfigurationException {
		sources.add(arg0);
		TransformerSpy transformerSpy = new TransformerSpy();
		transformers.add(transformerSpy);
		return transformerSpy;
	}

	@Override
	public void setAttribute(String arg0, Object arg1) {
		attributes.put(arg0, arg1);
	}

	@Override
	public void setErrorListener(ErrorListener arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFeature(String arg0, boolean arg1) throws TransformerConfigurationException {
		features.put(arg0, arg1);
	}

	@Override
	public void setURIResolver(URIResolver arg0) {
		// TODO Auto-generated method stub

	}

}
