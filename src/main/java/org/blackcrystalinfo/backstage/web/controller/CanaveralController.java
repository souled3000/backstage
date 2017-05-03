package org.blackcrystalinfo.backstage.web.controller;

import java.awt.Color;
import java.awt.Font;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.blackcrystalinfo.backstage.busi.IAppService;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StatisticalLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.DefaultStatisticalCategoryDataset;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CanaveralController {
	private static final Logger log = LoggerFactory.getLogger(CanaveralController.class);

	private ResponseEntity<byte[]> l(JFreeChart chart,int n) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();// 图片是文件格式的，故要用到FileOutputStream用来输出。
		if(n<20){
			ChartUtilities.writeChartAsJPEG(os, chart, 1000, 800);
		}else{
			ChartUtilities.writeChartAsJPEG(os, chart, n*40, 800);
		}
		HttpHeaders headers = new HttpHeaders();
		MediaType mt = new MediaType("application", "octet-stream");
		headers.setContentType(mt);
		return new ResponseEntity<byte[]>(os.toByteArray(), headers, HttpStatus.OK);
	}

	private StandardChartTheme theme() {
		// 创建主题样式
		StandardChartTheme theme = new StandardChartTheme("CN");
		// 设置标题字体
		theme.setExtraLargeFont(new Font("隶书", Font.BOLD, 20));
		// 设置图例的字体
		theme.setRegularFont(new Font("宋书", Font.PLAIN, 15));
		// 设置轴向的字体
		theme.setLargeFont(new Font("宋书", Font.PLAIN, 15));
		return theme;
	}

	@Autowired
	IAppService appService;

	@RequestMapping(value = "/canaveral")
	public String canaveral() {
		return "canaveral";
	}

	@RequestMapping(value = "/canaveral/bar", method = RequestMethod.GET, produces = "application/octet-stream")
	public ResponseEntity<byte[]> bar(@RequestParam String start, @RequestParam String end, @RequestParam String dv) throws IOException {

		List<Map<String, Object>> rt = appService.canaveral(start, end, dv);

		ChartFactory.setChartTheme(theme());
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for (Map<String, Object> o : rt) {
			String t = String.valueOf(o.get("dv"));
			Long amount = ((Integer) o.get("amount")).longValue();
			String dt = (String) o.get("dt");
			dataset.setValue(amount, "", dt);
		}
		JFreeChart chart = ChartFactory.createBarChart("hi", "", "数量", dataset, PlotOrientation.VERTICAL, false, true, false); // 创建一个JFreeChart
		chart.setTitle(new TextTitle(start + "至" + end + "设备在线数量历史统计（小时）", new Font("宋体", Font.BOLD + Font.ITALIC, 20)));// 可以重新设置标题，替换“hi”标题
		CategoryPlot plot = (CategoryPlot) chart.getPlot();// 获得图标中间部分，即plot
		plot.setBackgroundPaint(Color.white);

		BarRenderer renderer1 = new BarRenderer();// 设置柱子的相关属性
		renderer1.setBaseItemLabelsVisible(true, false);
		// 设置柱子边框颜色
		// renderer1.setBaseOutlinePaint(Color.BLACK);
		// 设置柱子边框可见
		// renderer1.setDrawBarOutline(true);
		// 设置每个地区所包含的平行柱的之间距离，数值越大则间隔越大，图片大小一定的情况下会影响柱子的宽度，可以为负数
		renderer1.setItemMargin(0.1);
		// 是否显示阴影
		renderer1.setShadowVisible(false);
		// 阴影颜色
		// renderer1.setShadowPaint(Color.white);
		plot.setRenderer(renderer1);

		CategoryAxis horizontal = plot.getDomainAxis();// 获得横坐标
		// horizontal.setLabelFont(new Font("微软雅黑", Font.PLAIN, 10));// 设置横坐标字体
		horizontal.setCategoryLabelPositions(CategoryLabelPositions.DOWN_90);
		// 设置图形右边的空白
		horizontal.setUpperMargin(0);
		// 设置左边的空白
		horizontal.setLowerMargin(0);
		return l(chart,rt.size());
	}

	@RequestMapping(value = "/canaveral/area", method = RequestMethod.GET, produces = "application/octet-stream")
	public ResponseEntity<byte[]> area(@RequestParam String start, @RequestParam String end, @RequestParam String dv) throws IOException {

		ChartFactory.setChartTheme(theme());
		List<Map<String, Object>> rt = appService.canaveral(start, end, dv);

		XYSeries series1 = new XYSeries("Series 1");
		Long n = 0L;
		for (Map<String, Object> o : rt) {
			String t = String.valueOf(o.get("dv"));
			Long amount = ((Integer) o.get("amount")).longValue();
			Long dt = Long.parseLong((String) o.get("dt"));
			series1.add(n++, amount);
		}
		XYDataset dataset = new XYSeriesCollection(series1);
		// JFreeChart chart = ChartFactory.createXYAreaChart("Area Chart", "Domain", "Range", dataset);
		JFreeChart chart = ChartFactory.createXYAreaChart("Area Chart", "", "", dataset);
		chart.setTitle(new TextTitle(start + "至" + end + "设备在线数量历史统计（小时）", new Font("宋体", Font.BOLD + Font.ITALIC, 20)));// 可以重新设置标题，替换“hi”标题
		chart.getLegend().setVisible(false);

		return l(chart,rt.size());
	}

	@RequestMapping(value = "/canaveral/line", method = RequestMethod.GET, produces = "application/octet-stream")
	public ResponseEntity<byte[]> line(@RequestParam String start, @RequestParam String end, @RequestParam String dv) throws IOException {

		ChartFactory.setChartTheme(theme());
		List<Map<String, Object>> rt = appService.canaveral(start, end, dv);

		StatisticalLineAndShapeRenderer r = new StatisticalLineAndShapeRenderer(true, false);
		DefaultStatisticalCategoryDataset dataset = new DefaultStatisticalCategoryDataset();
		Long n = 0L;
		for (Map<String, Object> o : rt) {
			Long amount = ((Integer) o.get("amount")).longValue();
			Long dt = Long.parseLong((String) o.get("dt"));
			// dataset.add(amount,n++, "C1" , dt);
			dataset.add(amount, n++, "C1", dt);
		}

		CategoryPlot plot = new CategoryPlot(dataset, new CategoryAxis(""), new NumberAxis(""), r);
		// 设置图形右边的空白
		plot.getDomainAxis().setUpperMargin(0);
		// 设置左边的空白
		plot.getDomainAxis().setLowerMargin(0);
		JFreeChart chart = new JFreeChart(plot);
		chart.setTitle(new TextTitle(start + "至" + end + "设备在线数量历史统计（小时）", new Font("宋体", Font.BOLD + Font.ITALIC, 20)));
		chart.getLegend().setVisible(false);
		return l(chart,rt.size());
	}

	@RequestMapping(value = "/canaveral/time", method = RequestMethod.GET, produces = "application/octet-stream")
	public ResponseEntity<byte[]> time(@RequestParam String start, @RequestParam String end, @RequestParam String dv) throws IOException {

		ChartFactory.setChartTheme(theme());
		List<Map<String, Object>> rt = appService.canaveral(start, end, dv);

		TimeSeries timeSeries = new TimeSeries("设备在线情况统计");
		for (Map<String, Object> o : rt) {
			Long amount = ((Integer) o.get("amount")).longValue();
			Long dt = Long.parseLong((String) o.get("dt"));
			long year = dt / 1000000L;
			long month = dt / 10000L - year * 100;
			long day = dt / 100L - year * 10000L - month * 100L;
			long hour = dt - day * 100L - month * 10000L - year * 1000000L;
			log.info("{}, {},{}, {}, {}", new Object[] { dt, hour, year, month, day });
			try {
				timeSeries.add(new Hour((int) hour, (int) day, (int) month, (int) year), amount);
			} catch (Exception e) {

			}

		}
		TimeSeriesCollection lineDataset = new TimeSeriesCollection();
		lineDataset.addSeries(timeSeries);
		JFreeChart chart = ChartFactory.createTimeSeriesChart("", "", "", lineDataset, true, true, true);
		// 设置主标题
		chart.setTitle(new TextTitle(start + "至" + end + "设备在线数量统计（单位：小时）", new Font("宋体", Font.BOLD + Font.ITALIC, 20)));
		// 设置子标题
//		TextTitle subtitle = new TextTitle("2010年度", new Font("黑体", Font.BOLD, 12));
//		chart.addSubtitle(subtitle);
		chart.setAntiAlias(true);
		chart.getLegend().setVisible(false);
		XYPlot plot = (XYPlot) chart.getPlot();
		// 设置时间轴的范围。
		DateAxis dateaxis = (DateAxis) plot.getDomainAxis();
		dateaxis.setTickLabelFont(new Font("宋体", Font.BOLD, 12));// 设置x轴坐标上的字体
//		dateaxis.setLabelFont(new Font("宋体", Font.BOLD, 8));// 设置x轴坐标上的标题的字体
		
//		dateaxis.setDateFormatOverride(new java.text.SimpleDateFormat("HH时"));
		dateaxis.setDateFormatOverride(new java.text.SimpleDateFormat("yyyy年MM月dd日HH时"));
		dateaxis.setVerticalTickLabels(true);
		dateaxis.setTickUnit(new DateTickUnit(DateTickUnitType.HOUR, 1));
		// Calendar date = Calendar.getInstance();
		// date.set(2009, 11, 1);
		// Calendar mdate = Calendar.getInstance();
		// mdate.set(2010, 11, 30);
		// dateaxis.setRange(date.getTime(),mdate.getTime());
		// 设置最大坐标范围
		// ValueAxis axis = plot.getRangeAxis() ;
		// axis.setRange(800,1800) ;
		// plot.setRangeAxis(axis);
		// 设置曲线图与xy轴的距离 [上,左,下,右]
		plot.setAxisOffset(new RectangleInsets(00, 00, 00, 00));
//		plot.setAxisOffset(new RectangleInsets(0D, 0D, 0D, 12D));
		// 设置曲线是否显示数据点
		XYLineAndShapeRenderer xylinerenderer = (XYLineAndShapeRenderer) plot.getRenderer();
		xylinerenderer.setBaseShapesVisible(true);
		// 设置曲线显示各数据点的值
		XYItemRenderer xyitem = plot.getRenderer();
		xyitem.setBaseItemLabelsVisible(true);
		xyitem.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER));
		xyitem.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
		xyitem.setBaseItemLabelFont(new Font("Dialog", 1, 12));
		plot.setRenderer(xyitem);

		return l(chart,rt.size());
	}

	public static void main(String[] args) {
		// 时间序列图
		TimeSeries timeseries = new TimeSeries("L&G European Index Trust", Month.class);
		timeseries.add(new Month(2, 2001), 181.8D);// 这里用的是Month.class，同样还有Day.class Year.class 等等
		timeseries.add(new Month(3, 2001), 167.3D);
		timeseries.add(new Month(4, 2001), 153.8D);
		timeseries.add(new Month(5, 2001), 167.6D);
		timeseries.add(new Month(6, 2001), 158.8D);
		timeseries.add(new Month(7, 2001), 148.3D);
		timeseries.add(new Month(8, 2001), 153.9D);
		timeseries.add(new Month(9, 2001), 142.7D);
		timeseries.add(new Month(10, 2001), 123.2D);
		timeseries.add(new Month(11, 2001), 131.8D);
		timeseries.add(new Month(12, 2001), 139.6D);
		timeseries.add(new Month(1, 2002), 142.9D);
		timeseries.add(new Month(2, 2002), 138.7D);
		timeseries.add(new Month(3, 2002), 137.3D);
		timeseries.add(new Month(4, 2002), 143.9D);
		timeseries.add(new Month(5, 2002), 139.8D);
		timeseries.add(new Month(6, 2002), 137D);
		timeseries.add(new Month(7, 2002), 132.8D);
		TimeSeries timeseries1 = new TimeSeries("L&G UK Index Trust曾召帅", Month.class);

		timeseries1.add(new Month(2, 2001), 129.6D);
		timeseries1.add(new Month(3, 2001), 123.2D);
		timeseries1.add(new Month(4, 2001), 117.2D);
		timeseries1.add(new Month(5, 2001), 124.1D);
		timeseries1.add(new Month(6, 2001), 122.6D);
		timeseries1.add(new Month(7, 2001), 119.2D);
		timeseries1.add(new Month(8, 2001), 116.5D);
		timeseries1.add(new Month(9, 2001), 112.7D);
		timeseries1.add(new Month(10, 2001), 101.5D);
		timeseries1.add(new Month(11, 2001), 106.1D);
		timeseries1.add(new Month(12, 2001), 110.3D);
		timeseries1.add(new Month(1, 2002), 111.7D);
		timeseries1.add(new Month(2, 2002), 111D);
		timeseries1.add(new Month(3, 2002), 109.6D);
		timeseries1.add(new Month(4, 2002), 113.2D);
		timeseries1.add(new Month(5, 2002), 111.6D);
		timeseries1.add(new Month(6, 2002), 108.8D);
		timeseries1.add(new Month(7, 2002), 101.6D);
		TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();

		timeseriescollection.addSeries(timeseries);
		timeseriescollection.addSeries(timeseries1);
		timeseriescollection.setDomainIsPointsInTime(true); // domain轴上的刻度点代表的是时间点而不是时间段
		JFreeChart jfreechart = ChartFactory.createTimeSeriesChart("合法 & General Unit Trust Prices", "日期", "暗示的话发神经提防", timeseriescollection, true, true, false);
		jfreechart.setBackgroundPaint(Color.white);
		TextTitle textTitle = jfreechart.getTitle();
		textTitle.setFont(new Font("宋体", Font.BOLD, 20));
		LegendTitle legend = jfreechart.getLegend();
		if (legend != null) {
			legend.setItemFont(new Font("宋体", Font.BOLD, 20));
		}
		XYPlot xyplot = (XYPlot) jfreechart.getPlot(); // 获得 plot : XYPlot!!
		ValueAxis domainAxis = xyplot.getDomainAxis();
		domainAxis.setTickLabelFont(new Font("宋体", Font.BOLD, 8));// 设置x轴坐标上的字体
		domainAxis.setLabelFont(new Font("宋体", Font.BOLD, 20));// 设置x轴坐标上的标题的字体
		ValueAxis rangeAxis = xyplot.getRangeAxis();
		rangeAxis.setTickLabelFont(new Font("宋体", Font.BOLD, 20));// 设置y轴坐标上的字体
		rangeAxis.setLabelFont(new Font("宋体", Font.BOLD, 20));// 设置y轴坐标上的标题的字体
		xyplot.setBackgroundPaint(Color.lightGray);
		xyplot.setDomainGridlinePaint(Color.white);
		xyplot.setRangeGridlinePaint(Color.white);
		xyplot.setAxisOffset(new RectangleInsets(5D, 5D, 5D, 5D));
		xyplot.setDomainCrosshairVisible(true);
		xyplot.setRangeCrosshairVisible(true);
		ChartFrame frame = new ChartFrame("折线图 ", jfreechart, true);
		frame.pack();
		frame.setVisible(true);
	}

}
