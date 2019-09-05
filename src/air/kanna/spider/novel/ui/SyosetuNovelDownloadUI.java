package air.kanna.spider.novel.ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import air.kanna.spider.novel.download.NovelDownloader;
import air.kanna.spider.novel.factory.NovelSpiderFactory;
import air.kanna.spider.novel.factory.impl.SyosetuSpiderFactory;
import air.kanna.spider.novel.model.Novel;
import air.kanna.spider.novel.model.NovelChapter;
import air.kanna.spider.novel.model.NovelSection;
import air.kanna.spider.novel.spider.ProcessListener;
import air.kanna.spider.novel.spider.impl.BaseNovelSpider;
import air.kanna.spider.novel.syosetu.impl.SyosetuDownloadWithDownloadId;
import air.kanna.spider.novel.syosetu.impl.SyosetuDownloadWithHtml;
import air.kanna.spider.novel.util.Entry;
import air.kanna.spider.novel.util.StringUtil;
import air.kanna.spider.novel.util.log.LoggerProvider;
import air.kanna.spider.novel.util.log.impl.Log4jLoggerFactory;

/**
 * 
 * @author kan-na
 */
public class SyosetuNovelDownloadUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private static List<Entry> versions;
	
	private JPanel contentPane;
	private JTextField urlTf;
	private JTextField savePathTf;
	private JTextField sepNumTf;
	private JLabel messageLb;
	private JButton startDownBtn;
	private JButton cancelDownBtn;
	private JProgressBar progressBar;
	private JComboBox sepTypeCb;
	private JButton pathSelectBtn;
	
	private JFileChooser pathSelect;
	private JFrame thisFrame;
	
	private ProcessListener process;
	private NovelSpiderFactory factory;
	private NovelDownloader downloader;
	
	private NovelSpiderFactory[] buffer;
	private boolean isStop = false;
	private File lastPath = null;
	
	private void setDownloading(){
		urlTf.setEnabled(false);
		savePathTf.setEnabled(false);
		sepNumTf.setEnabled(false);
		startDownBtn.setEnabled(false);
		sepTypeCb.setEnabled(false);
		pathSelectBtn.setEnabled(false);
		
		cancelDownBtn.setEnabled(true);
		
		progressBar.setVisible(true);
	}
	
	private void setNormal(){
		urlTf.setEnabled(true);
		savePathTf.setEnabled(true);
		startDownBtn.setEnabled(true);
		sepTypeCb.setEnabled(true);
		pathSelectBtn.setEnabled(true);
		
		sepNumTf.setEnabled(sepTypeCb.getSelectedIndex() == 1);
		
		cancelDownBtn.setEnabled(false);
		
		progressBar.setVisible(false);
	}
	private void initDownload(){
		process = new ProcessBarListenerAdapter(progressBar, messageLb);
		buffer = new NovelSpiderFactory[] {null, null};
	}
	
	private boolean checkInput(){
		String checkStr = urlTf.getText();
		if(checkStr == null || checkStr.length() <= 0){
			JOptionPane.showMessageDialog(thisFrame, "请输入要下载的小说的URL路径。", "错误", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		checkStr = savePathTf.getText();
		if(checkStr == null || checkStr.length() <= 0){
			JOptionPane.showMessageDialog(thisFrame, "请选择要保存小说的本地路径。", "错误", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		checkStr = sepNumTf.getText();
		if(sepTypeCb.getSelectedIndex() == 1){
			try{
				Integer.parseInt(checkStr);
			}catch(Exception e){
				JOptionPane.showMessageDialog(thisFrame, ("请输入正确的数字，而不是：" + checkStr), "错误", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		
		return true;
	}

	/**
	 * Create the frame.
	 */
	public SyosetuNovelDownloadUI() {
		setResizable(false);
		initVersions();
		
		LoggerProvider.resetLoggerFactory(new Log4jLoggerFactory());
		
		setTitle("小説家になろう — 小说下载器 " + versions.get(versions.size() - 1).key);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 240);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("网址：");
		lblNewLabel.setBounds(10, 10, 86, 15);
		contentPane.add(lblNewLabel);
		
		urlTf = new JTextField();
		urlTf.setBounds(106, 7, 328, 21);
		contentPane.add(urlTf);
		urlTf.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("保存路径：");
		lblNewLabel_1.setBounds(10, 35, 86, 15);
		contentPane.add(lblNewLabel_1);
		
		savePathTf = new JTextField();
		savePathTf.setBounds(106, 32, 278, 21);
		contentPane.add(savePathTf);
		savePathTf.setColumns(10);
		
		pathSelectBtn = new JButton("...");
		pathSelectBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pathSelect.setCurrentDirectory(lastPath);
				int result = pathSelect.showOpenDialog(thisFrame);
				
				if(result == JFileChooser.APPROVE_OPTION){
					lastPath = pathSelect.getSelectedFile();
					savePathTf.setText(pathSelect.getSelectedFile().getAbsolutePath());
				}
			}
		});
		pathSelectBtn.setBounds(394, 31, 40, 23);
		contentPane.add(pathSelectBtn);
		
		JLabel label = new JLabel("分割类型：");
		label.setBounds(10, 60, 86, 15);
		contentPane.add(label);
		
		sepTypeCb = new JComboBox();
		sepTypeCb.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					sepNumTf.setEditable(sepTypeCb.getSelectedIndex() == 1);
				}
			}
		});
		sepTypeCb.setModel(new DefaultComboBoxModel(new String[] {"按章节分割", "按字数分割"}));
		sepTypeCb.setBounds(106, 57, 278, 21);
		contentPane.add(sepTypeCb);
		
		JLabel label_1 = new JLabel("分割字数：");
		label_1.setBounds(10, 85, 86, 15);
		contentPane.add(label_1);
		
		sepNumTf = new JTextField();
		sepNumTf.setBounds(106, 82, 278, 21);
		contentPane.add(sepNumTf);
		sepNumTf.setColumns(10);
		sepNumTf.setEditable(false);
		sepNumTf.setText("" + SyosetuDownloadWithDownloadId.DEFAULT_LENGTH);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(10, 110, 424, 14);
		contentPane.add(progressBar);
		progressBar.setVisible(false);
		
		messageLb = new JLabel("");
		messageLb.setForeground(Color.BLUE);
		messageLb.setHorizontalAlignment(SwingConstants.CENTER);
		messageLb.setToolTipText("");
		messageLb.setBounds(10, 134, 424, 15);
		contentPane.add(messageLb);
		
		startDownBtn = new JButton("开始下载");
		startDownBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setDownloading();
				isStop = false;
				if(!checkInput()){
					setNormal();
					return;
				}
				factory = getNovelSpiderFactory(urlTf.getText());
				
				BaseNovelSpider novelSpider = (BaseNovelSpider)factory.getSpider(urlTf.getText());
				
				novelSpider.setNovelId(getNovelIdFromURL(urlTf.getText()));
				
				new Thread(){
					@Override
					public void run(){
						process.setMax(10);
						process.setPosition(0, "开始下载小说，编号：" + novelSpider.getNovelId());
						
						List<Novel> list = null;
						
						try{
						    list = novelSpider.getNovel();
						
    						if(list == null || list.size() <= 0){
    							process.finish("没有找到该小说");
    							setNormal();
    							return;
    						}
						}catch(Exception e) {
						    process.finish("获取小说目录失败：" + e.getMessage());
                            setNormal();
                            return;
						}
						
						Novel novel = list.get(0);
						int count = 1;
						for(NovelChapter chapter: novel.getNovelContent()){
							for(NovelSection section: chapter.getChapterContent()){
								count++;
							}
						}
						
						process.setMax(count);
						process.setPosition(1, "开始下载小说：" + novel.getNovelTitle());
						
						try{
						    downloader = factory.getDownloader(novel);
						    downloader.setProcess(process);
						    downloader.setStop(false);
							String msg = downloader.download(
									novel, 
									new File(savePathTf.getText()),
									sepTypeCb.getSelectedIndex() == 1 ? SyosetuDownloadWithDownloadId.MODEL_LENGTH : SyosetuDownloadWithDownloadId.MODEL_CHAPTER,
									Integer.parseInt(sepNumTf.getText()));
							
							if(isStop){
								process.finish("下载小说《" + novel.getNovelTitle() + "》用户中断操作");
							}else{
								process.finish("下载小说《" + novel.getNovelTitle() + "》成功");
							}
							
							if(msg != null){
								JOptionPane.showMessageDialog(
										thisFrame, 
										("下载失败信息：\n" + msg),
										"下载失败信息", 
										JOptionPane.ERROR_MESSAGE);
							}
						}catch(Exception e){
							process.finish("下载小说失败：" + e.getMessage());
						}
						
						setNormal();
					}
				}.start();
			}
			
			private String getNovelIdFromURL(String url){
				if(url.startsWith("https")){
					int idx = url.lastIndexOf('/', url.length() - 2);
					if(idx >= 0){
						if(url.endsWith("/")){
							return url.substring(idx + 1, url.length() - 1);
						}else{
							return url.substring(idx + 1);
						}
					}else{
						return url;
					}
				}
				return url;
			}
		});
		startDownBtn.setBounds(10, 159, 118, 23);
		contentPane.add(startDownBtn);
		
		cancelDownBtn = new JButton("取消下载");
		cancelDownBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				messageLb.setText("下载停止中");
				isStop = true;
				downloader.setStop(true);
				cancelDownBtn.setEnabled(false);
			}
		});
		cancelDownBtn.setBounds(316, 159, 118, 23);
		contentPane.add(cancelDownBtn);
		cancelDownBtn.setEnabled(false);
		
		pathSelect = new JFileChooser();
		lastPath = new File(".");
		pathSelect.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		pathSelect.setDialogTitle("请选择小说保存目录");
		pathSelect.setMultiSelectionEnabled(false);
		
		initDownload();
		
		thisFrame = this;
	}
	
	private void initVersions(){
		versions = new ArrayList<Entry>();
		
		Entry entry = new Entry();
		entry.key = "V1.0.0";
		entry.value = "爬取“小説家になろう”网站小说";
		versions.add(entry);
		
		entry = new Entry();
		entry.key = "V1.0.1";
		entry.value = "非法字符清理用于全路径的bug修改";
		versions.add(entry);
		
		entry = new Entry();
		entry.key = "V1.0.2";
		entry.value = "日志处理优化";
		
		entry = new Entry();
        entry.key = "V1.0.3";
        entry.value = "支持不通过下载TXT，直接从网页抓取";
        
		versions.add(entry);
	}
	
	private NovelSpiderFactory getNovelSpiderFactory(String url) {
	    if(buffer[0] == null) {
	        buffer[0] = new SyosetuSpiderFactory();
	    }
	    return buffer[0];
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SyosetuNovelDownloadUI frame = new SyosetuNovelDownloadUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
