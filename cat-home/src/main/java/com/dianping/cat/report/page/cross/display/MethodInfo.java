package com.dianping.cat.report.page.cross.display;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.dianping.cat.consumer.cross.model.entity.Local;
import com.dianping.cat.consumer.cross.model.entity.Name;
import com.dianping.cat.consumer.cross.model.entity.Remote;
import com.dianping.cat.consumer.cross.model.entity.Type;
import com.dianping.cat.consumer.cross.model.transform.BaseVisitor;
import com.dianping.cat.service.HostinfoService;

public class MethodInfo extends BaseVisitor {

	private static final String ALL_METHOD = "AllMethods";

	private Map<String, NameDetailInfo> m_callProjectsInfo = new LinkedHashMap<String, NameDetailInfo>();

	private Map<String, NameDetailInfo> m_serviceProjectsInfo = new LinkedHashMap<String, NameDetailInfo>();

	private Map<String, NameDetailInfo> m_callerProjectsInfo = new LinkedHashMap<String, NameDetailInfo>();

	private String m_remoteProject;

	private String m_clientIp;

	private String m_currentType;

	private String m_currentRole;

	private String m_remoteIp;

	private long m_reportDuration;

	private String m_query;

	private String m_callSortBy = "Avg";

	private String m_serviceSortBy = "Avg";

	private HostinfoService m_hostinfoService;

	public MethodInfo(long reportDuration) {
		m_reportDuration = reportDuration;
	}

	private void addCallProject(String type, Name name) {
		String id = name.getId();
		NameDetailInfo all = m_callProjectsInfo.get(ALL_METHOD);

		if (all == null) {
			all = new NameDetailInfo(m_reportDuration, ALL_METHOD, m_remoteIp, type);
			m_callProjectsInfo.put(ALL_METHOD, all);
		}
		NameDetailInfo info = m_callProjectsInfo.get(id);

		if (info == null) {
			info = new NameDetailInfo(m_reportDuration, name.getId(), m_remoteIp, type);
			m_callProjectsInfo.put(id, info);
		}
		info.mergeName(name);
		all.mergeName(name);
	}

	private void addCallerProject(String type, Name name) {
		String id = name.getId();
		NameDetailInfo all = m_callerProjectsInfo.get(ALL_METHOD);

		if (all == null) {
			all = new NameDetailInfo(m_reportDuration, ALL_METHOD, m_remoteIp, type);
			m_callerProjectsInfo.put(ALL_METHOD, all);
		}
		NameDetailInfo info = m_callerProjectsInfo.get(id);

		if (info == null) {
			info = new NameDetailInfo(m_reportDuration, name.getId(), m_remoteIp, type);
			m_callerProjectsInfo.put(id, info);
		}
		info.mergeName(name);
		all.mergeName(name);
	}

	private void addServiceProject(String type, Name name) {
		String id = name.getId();
		NameDetailInfo all = m_serviceProjectsInfo.get(ALL_METHOD);

		if (all == null) {
			all = new NameDetailInfo(m_reportDuration, ALL_METHOD, m_remoteIp, type);
			m_serviceProjectsInfo.put(ALL_METHOD, all);
		}
		NameDetailInfo info = m_serviceProjectsInfo.get(id);

		if (info == null) {
			info = new NameDetailInfo(m_reportDuration, name.getId(), m_remoteIp, type);
			m_serviceProjectsInfo.put(id, info);
		}
		info.mergeName(name);
		all.mergeName(name);
	}

	public Collection<NameDetailInfo> getCallProjectsInfo() {
		List<NameDetailInfo> values = new ArrayList<NameDetailInfo>(m_callProjectsInfo.values());

		Collections.sort(values, new NameComparator(m_callSortBy));
		return values;
	}

	public Map<String, NameDetailInfo> getCallerProjectsInfo() {
		return m_callerProjectsInfo;
	}

	public String getQuery() {
		return m_query;
	}

	public long getReportDuration() {
		return m_reportDuration;
	}

	public List<NameDetailInfo> getServiceProjectsInfo() {
		List<NameDetailInfo> values = new ArrayList<NameDetailInfo>(m_serviceProjectsInfo.values());

		Collections.sort(values, new NameComparator(m_serviceSortBy));
		return values;
	}

	private boolean isFit(String queryName, String methodName) {
		String[] args = queryName.split("\\|");

		if (args != null) {
			for (String str : args) {
				if (str.length() > 0 && methodName.toLowerCase().contains(str.trim().toLowerCase())) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean projectContains(String projectName, String app, String ip, String role) {
		if (m_remoteIp.startsWith("All")) {
			if (m_remoteProject.startsWith("AllClient") && (role.endsWith("Client") || role.endsWith("Caller"))) {
				return true;
			} else if (m_remoteProject.startsWith("AllServer") && role.endsWith("Server")) {
				return true;
			}
			if (StringUtils.isNotEmpty(app)) {
				if (app.equalsIgnoreCase(projectName)) {
					return true;
				}
			} else {
				if (ip.indexOf(':') > 0) {
					ip = ip.substring(0, ip.indexOf(':'));
				}
				String domain = m_hostinfoService.queryDomainByIp(ip);
				if (projectName.equalsIgnoreCase(domain)) {
					return true;
				}
			}
			return false;
		}
		return false;
	}

	public MethodInfo setCallSortBy(String callSoryBy) {
		m_callSortBy = callSoryBy;
		return this;
	}

	public MethodInfo setClientIp(String clientIp) {
		m_clientIp = clientIp;
		return this;
	}

	public void setHostinfoService(HostinfoService hostinfoService) {
		m_hostinfoService = hostinfoService;
	}

	public MethodInfo setQuery(String query) {
		m_query = query;
		return this;
	}

	public MethodInfo setRemoteIp(String remoteIp) {
		m_remoteIp = remoteIp;
		return this;
	}

	public void setRemoteProject(String remoteProject) {
		m_remoteProject = remoteProject;
	}

	public MethodInfo setServiceSortBy(String serviceSortBy) {
		m_serviceSortBy = serviceSortBy;
		return this;
	}

	@Override
	public void visitLocal(Local local) {
		if (m_clientIp.equalsIgnoreCase("All") || m_clientIp.equalsIgnoreCase(local.getId())) {
			super.visitLocal(local);
		}
	}

	@Override
	public void visitName(Name name) {
		String role = m_currentRole;

		if (StringUtils.isEmpty(m_query) || isFit(m_query, name.getId())) {
			if (role != null && role.endsWith("Client")) {
				addServiceProject(m_currentType, name);
			} else if (role != null && role.endsWith("Server")) {
				addCallProject(m_currentType, name);
			} else if (role != null && role.endsWith("Caller")) {
				addCallerProject(m_currentType, name);
			}
		}
	}

	@Override
	public void visitRemote(Remote remote) {
		String role = remote.getRole();
		String ip = remote.getId();
		String app = remote.getApp();

		if (ip.endsWith(":Caller") && role.endsWith("Caller")) {
			ip = ip.substring(0, ip.indexOf(":Caller"));
		}
		if (projectContains(m_remoteProject, app, ip, role) || m_remoteIp.equals(ip)) {
			m_currentRole = role;
			super.visitRemote(remote);
		}
	}

	@Override
	public void visitType(Type type) {
		m_currentType = type.getId();
		super.visitType(type);
	}

}
