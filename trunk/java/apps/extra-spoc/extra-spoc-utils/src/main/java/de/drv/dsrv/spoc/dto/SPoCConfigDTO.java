package de.drv.dsrv.spoc.dto;

public class SPoCConfigDTO {

	private final String extraProcedure;
	private final String extraDatatype;
	private final String extraProfile;
	private final String extraVersion;
	private final String startUrl;

	public SPoCConfigDTO(final String extraProcedure,
			final String extraDatatype, final String extraProfile,
			final String extraVersion, final String startUrl) {
		super();
		this.extraProcedure = extraProcedure;
		this.extraDatatype = extraDatatype;
		this.extraProfile = extraProfile;
		this.extraVersion = extraVersion;
		this.startUrl = startUrl;
	}

	public String getProcedure() {
		return extraProcedure;
	}

	public String getDatatype() {
		return extraDatatype;
	}

	public String getProfile() {
		return extraProfile;
	}

	public String getStartUrl() {
		return startUrl;
	}

	public String getVersion() {
		return extraVersion;
	}
}
