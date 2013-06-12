package de.extra.client.core.model.inputdata.impl;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataSource;

import com.sun.istack.ByteArrayDataSource;

import de.extrastandard.api.model.content.IInputDataPluginDescription;
import de.extrastandard.api.model.content.ISingleContentInputData;

public class SingleStringInputData extends Implementor implements
		ISingleContentInputData {

	private final String content;

	private List<IInputDataPluginDescription> plugins = new ArrayList<IInputDataPluginDescription>();

	private String requestId;

	private final static String INPUT_DATA_TYPE = "STRING_INPUT_DATA";

	public SingleStringInputData(final String inputdata) {
		this.content = inputdata;
	}

	public SingleStringInputData(final String inputdata,
			final List<IInputDataPluginDescription> pluginDescriptions) {
		this.content = inputdata;
		this.plugins = pluginDescriptions;
	}

	@Override
	public List<IInputDataPluginDescription> getPlugins() {
		return plugins;
	}

	@Override
	public String getInputDataAsString() {
		return content;
	}

	@Override
	public String getInputDataAsString(final Charset encoding) {
		return content;
	}

	@Override
	public byte[] getInputDataAsByteArray() {
		return content.getBytes();
	}

	@Override
	public void setRequestId(final String requestId) {
		this.requestId = requestId;

	}

	/**
	 * @return the requestId
	 */
	@Override
	public String getRequestId() {
		return requestId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("SingleStringInputData [");
		if (content != null) {
			builder.append("content=");
			builder.append(content);
			builder.append(", ");
		}
		if (plugins != null) {
			builder.append("plugins=");
			builder.append(plugins);
			builder.append(", ");
		}
		if (requestId != null) {
			builder.append("requestId=");
			builder.append(requestId);
		}
		builder.append("]");
		return builder.toString();
	}

	@Override
	public String getHashCode() {
		return String.valueOf(content.hashCode());
	}

	@Override
	public String getInputIdentifier() {
		return "SingleStringInputData: " + content;
	}

	@Override
	public String getInputDataType() {
		return INPUT_DATA_TYPE;
	}

	@Override
	public DataSource getInputDataAsDataSource() {
		final DataSource source = new ByteArrayDataSource(
				this.getInputDataAsByteArray(), "application/octet-stream");
		return source;
	}

}
