package limeng32.mirage.account.web;

import javax.servlet.ServletContext;

import org.springframework.web.context.ServletContextAware;

public class ResourcePathExposer implements ServletContextAware {
	private ServletContext servletContext;

	private String resourceRoot;

	public void init() {
		String version = "201511291507";
		resourceRoot = "/resources-" + version;
		getServletContext().setAttribute("resourceRoot",
				getServletContext().getContextPath() + resourceRoot);
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public String getResourceRoot() {
		return resourceRoot;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

}
