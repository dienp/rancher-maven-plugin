package rancher.models;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.StringJoiner;

import org.apache.log4j.Logger;
import org.codehaus.plexus.util.StringUtils;

import lombok.Data;
import rancher.common.Constant;

@Data
public class URIBuilder {
	private String host;
	private int port;
	private String projectId;
	private String stackId;
	private String serviceId;
	private String query;

	private static final Logger LOGGER = Logger.getLogger(URIBuilder.class);

	public URIBuilder(String host, int port, String projectId, String stackId, String serviceId, String query) {
		super();
		this.host = host;
		this.port = port;
		this.projectId = projectId;
		this.stackId = stackId;
		this.serviceId = serviceId;
		this.query = query;
	}

	public URIBuilder(String rancherHost, int rancherPort, String query) {
		this.host = rancherHost;
		this.port = rancherPort;
		this.query = query;
	}

	public URIBuilder(String rancherHost, int rancherPort, String projectId, String query) {
		this.host = rancherHost;
		this.port = rancherPort;
		this.projectId = projectId;
		this.query = query;
	}

	public URIBuilder(String rancherHost, int rancherPort, String projectId, String serviceId, String query) {
		this.host = rancherHost;
		this.port = rancherPort;
		this.projectId = projectId;
		this.serviceId = serviceId;
	}

	public URI build() {
		StringJoiner sj = new StringJoiner("/");

		sj.add("");
		sj.add(Constant.RANCHER_API_VERSION);

		sj.add("projects");
		if (StringUtils.isEmpty(this.getProjectId())) {
			try {
				return new URI(Constant.HTTP_PROTOCOL, null, this.getHost(), this.getPort(), sj.toString(),
						this.getQuery(), null);
			} catch (URISyntaxException e) {
				LOGGER.error("URISyntaxException: ", e);
			}
		}
		sj.add(this.getProjectId());

		sj.add("stacks");
		if (StringUtils.isEmpty(this.getStackId())) {
			try {
				return new URI(Constant.HTTP_PROTOCOL, null, this.getHost(), this.getPort(), sj.toString(),
						this.getQuery(), null);
			} catch (URISyntaxException e) {
				LOGGER.error("URISyntaxException: ", e);
			}
		}
		sj.add(this.getStackId());

		sj.add("services");
		if (StringUtils.isEmpty(this.getServiceId())) {
			try {
				return new URI(Constant.HTTP_PROTOCOL, null, this.getHost(), this.getPort(), sj.toString(),
						this.getQuery(), null);
			} catch (URISyntaxException e) {
				LOGGER.error("URISyntaxException: ", e);
			}
		}
		sj.add(this.getServiceId());

		try {
			return new URI(Constant.HTTP_PROTOCOL, null, this.getHost(), this.getPort(), sj.toString(), this.getQuery(),
					null);
		} catch (URISyntaxException e) {
			LOGGER.error("URISyntaxException: ", e);
		}

		return null;
	}

	public URI buildServiceWithIdURL() {
		StringJoiner sj = new StringJoiner("/");

		sj.add("");
		sj.add(Constant.RANCHER_API_VERSION);

		sj.add("projects");
		sj.add(this.getProjectId());
		sj.add("services");
		sj.add(this.getServiceId());

		try {
			return new URI(Constant.HTTP_PROTOCOL, null, this.getHost(), this.getPort(), sj.toString(), this.getQuery(),
					null);
		} catch (URISyntaxException e) {
			LOGGER.error("URISyntaxException: ", e);
		}

		return null;
	}

	public URI buildCreateServiceURI() {
		StringJoiner sj = new StringJoiner("/");
		sj.add("");
		sj.add(Constant.RANCHER_API_VERSION);
		sj.add("projects");
		sj.add(this.getProjectId());
		sj.add("service");
		
		try {
			return new URI(Constant.HTTP_PROTOCOL, null, this.getHost(), this.getPort(), sj.toString(), this.getQuery(),
					null);
		} catch (URISyntaxException e) {
			LOGGER.error("URISyntaxException: ", e);
		}
		return null;
	}
}
