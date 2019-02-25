package com.Fenci;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

public class DemoUI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JScrollPane scrollPane = null;
	private JTextPane textResult = null;
	private Box box = null; // 放输入组件的容器
	private JButton buttonStartWork = null, buttonClear = null,buttonSelect=null; // 插入按钮;清除按钮
	private JTextField textFilePath = null; // 输入框

	private StyledDocument doc = null;

	public DemoUI() {
		super("词频统计");
		try { // 使用Windows的界面风格
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}

		textResult = new JTextPane();
		textResult.setEditable(false);
		doc = textResult.getStyledDocument(); // 获得JTextPane的Document
		scrollPane = new JScrollPane(textResult);
		scrollPane.setPreferredSize(new Dimension(400, 400));
		textFilePath = new JTextField(18);
		buttonStartWork = new JButton("分析"); // 
		buttonClear = new JButton("清空"); // 清除
		buttonSelect = new JButton("...");
		

		buttonStartWork.addActionListener(new ActionListener() { // 分析文字的事件
			public void actionPerformed(ActionEvent e) {
				startAnalyze();				
			}
		});

		buttonClear.addActionListener(new ActionListener() { // 清除事件
			public void actionPerformed(ActionEvent e) {
				textResult.setText("");
			}
		});
		buttonSelect.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFileChooser jfc=new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );
				jfc.showDialog(new JLabel(), "选择");
				File file=jfc.getSelectedFile();
				if(file==null)return;
				if(file.isDirectory()){
					System.out.println("文件夹:"+file.getAbsolutePath());
				}else if(file.isFile()){
					//System.out.println("文件:"+file.getAbsolutePath());
					textFilePath.setText(file.getAbsolutePath());
				}	
			}
			
		});

		box = Box.createVerticalBox(); // 竖结构
		Box box_1 = Box.createHorizontalBox(); // 横结构
		//box.add(box_1);
		box.add(Box.createVerticalStrut(8)); // 两行的间距
		box.add(box_1);
		box.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8)); // 8个的边距
		// 开始将所需组件加入容器

		
		box_1.add(textFilePath);
		box_1.add(Box.createHorizontalStrut(8));
		box_1.add(buttonSelect);
		box_1.add(Box.createHorizontalStrut(8));
		box_1.add(buttonStartWork);
		box_1.add(Box.createHorizontalStrut(8));
		box_1.add(buttonClear);
		this.getRootPane().setDefaultButton(buttonStartWork); // 默认回车按钮
		this.getContentPane().add(scrollPane);
		this.getContentPane().add(box, BorderLayout.SOUTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		textFilePath.requestFocus();
		textFilePath.setText("./testfile.txt");
	}


	Fenci fc = new Fenci();
	private void startAnalyze() {
		String filePath = textFilePath.getText();
		if(filePath.length()<3)
		{
			updateTextResult("文件路径不正确");
			return;
		}
		//如果是网络文件,下载并分析
		
		try { // 开始分析		
			updateTextResult("开始分析:"+filePath);
			String str="\t分析失败,找不到文件";
			if(fc.doFenci(filePath))
			 str= fc.getResult();
			updateTextResult("分析结果:\n"+str);
		} 
		catch(java.io.FileNotFoundException e)
		{
			updateTextResult(e.getMessage());
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void updateTextResult(String strContent)
	{
		try {
			doc.insertString(doc.getLength(), strContent+"\n", null);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}


}