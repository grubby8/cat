package com.dianping.cat.report.page.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.dianping.cat.configuration.url.pattern.entity.PatternItem;
import com.dianping.cat.report.page.AbstractReportModel;
import com.dianping.cat.report.page.JsonBuilder;
import com.dianping.cat.report.page.LineChart;
import com.dianping.cat.report.page.PieChart;

public class Model extends AbstractReportModel<Action, Context> {

	private Collection<PatternItem> m_pattermItems;

	private List<String> m_cities;

	private Map<String, LineChart> m_lineCharts;

	private List<PieChart> m_pieCharts;

	private LineChart m_lineChart;

	private PieChart m_pieChart;

	private Date m_start;

	private Date m_end;

	private Date m_compareStart;

	private Date m_compareEnd;

	private String m_cityInfo;

	private String m_json;

	public Model(Context ctx) {
		super(ctx);
	}

	public List<String> getCities() {
		return m_cities;
	}

	public String getCityInfo() {
		return m_cityInfo;
	}

	public Date getCompareEnd() {
		return m_compareEnd;
	}

	public Date getCompareStart() {
		return m_compareStart;
	}

	@Override
	public Action getDefaultAction() {
		return Action.VIEW;
	}

	@Override
	public String getDomain() {
		return getDisplayDomain();
	}

	@Override
	public Collection<String> getDomains() {
		return new ArrayList<String>();
	}

	public Date getEnd() {
		return m_end;
	}

	public String getItems() {
		Map<String, List<PatternItem>> maps = new LinkedHashMap<String, List<PatternItem>>();

		for (PatternItem item : m_pattermItems) {
			List<PatternItem> items = maps.get(item.getGroup());

			if (items == null) {
				items = new ArrayList<PatternItem>();
				maps.put(item.getGroup(), items);
			}
			items.add(item);
		}
		return new JsonBuilder().toJson(maps);
	}

	public String getJson() {
		return m_json;
	}

	public LineChart getLineChart() {
		return m_lineChart;
	}

	public List<LineChart> getLineCharts() {
		if (m_lineCharts != null) {
			return new ArrayList<LineChart>(m_lineCharts.values());
		} else {
			return new ArrayList<LineChart>();
		}
	}

	public Collection<PatternItem> getPattermItems() {
		return m_pattermItems;
	}

	public PieChart getPieChart() {
		return m_pieChart;
	}

	public List<PieChart> getPieCharts() {
		return m_pieCharts;
	}

	public Date getStart() {
		return m_start;
	}

	public void setCities(List<String> cities) {
		m_cities = cities;
	}

	public void setCityInfo(String cityInfo) {
		m_cityInfo = cityInfo;
	}

	public void setCompareEnd(Date compareEnd) {
		m_compareEnd = compareEnd;
	}

	public void setCompareStart(Date compareStart) {
		m_compareStart = compareStart;
	}

	public void setEnd(Date end) {
		m_end = end;
	}

	public void setJson(String json) {
		m_json = json;
	}

	public void setLineChart(LineChart lineChart) {
		m_lineChart = lineChart;
	}

	public void setLineCharts(Map<String, LineChart> lineCharts) {
		m_lineCharts = lineCharts;
	}

	public void setPattermItems(Collection<PatternItem> pattermItems) {
		m_pattermItems = pattermItems;
	}

	public void setPieChart(PieChart pieChart) {
		m_pieChart = pieChart;
	}

	public void setPieCharts(List<PieChart> pieCharts) {
		m_pieCharts = pieCharts;
	}

	public void setStart(Date start) {
		m_start = start;
	}

}
