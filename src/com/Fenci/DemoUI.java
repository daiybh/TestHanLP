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
	private Box box = null; // ���������������
	private JButton buttonStartWork = null, buttonClear = null,buttonSelect=null; // ���밴ť;�����ť
	private JTextField textFilePath = null; // �����

	private StyledDocument doc = null;

	public DemoUI() {
		super("��Ƶͳ��");
		try { // ʹ��Windows�Ľ�����
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}

		textResult = new JTextPane();
		textResult.setEditable(false);
		doc = textResult.getStyledDocument(); // ���JTextPane��Document
		scrollPane = new JScrollPane(textResult);
		scrollPane.setPreferredSize(new Dimension(400, 400));
		textFilePath = new JTextField(18);
		buttonStartWork = new JButton("����"); // 
		buttonClear = new JButton("���"); // ���
		buttonSelect = new JButton("...");
		

		buttonStartWork.addActionListener(new ActionListener() { // �������ֵ��¼�
			public void actionPerformed(ActionEvent e) {
				startAnalyze();				
			}
		});

		buttonClear.addActionListener(new ActionListener() { // ����¼�
			public void actionPerformed(ActionEvent e) {
				textResult.setText("");
			}
		});
		buttonSelect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFileChooser jfc=new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );
				jfc.showDialog(new JLabel(), "ѡ��");
				File file=jfc.getSelectedFile();
				if(file==null)return;
				if(file.isDirectory()){
					System.out.println("�ļ���:"+file.getAbsolutePath());
				}else if(file.isFile()){
					//System.out.println("�ļ�:"+file.getAbsolutePath());
					textFilePath.setText(file.getAbsolutePath());
				}	
			}
			
		});

		box = Box.createVerticalBox(); // ���ṹ
		Box box_1 = Box.createHorizontalBox(); // ��ṹ
		//box.add(box_1);
		box.add(Box.createVerticalStrut(8)); // ���еļ��
		box.add(box_1);
		box.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8)); // 8���ı߾�
		// ��ʼ�����������������

		
		box_1.add(textFilePath);
		box_1.add(Box.createHorizontalStrut(8));
		box_1.add(buttonSelect);
		box_1.add(Box.createHorizontalStrut(8));
		box_1.add(buttonStartWork);
		box_1.add(Box.createHorizontalStrut(8));
		box_1.add(buttonClear);
		this.getRootPane().setDefaultButton(buttonStartWork); // Ĭ�ϻس���ť
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
			updateTextResult("�ļ�·������ȷ");
			return;
		}
		//����������ļ�,���ز�����
		
		try { // ��ʼ����		
			updateTextResult("��ʼ����:"+filePath);
			fc.doFenci(filePath);
			String str = fc.getResult();
			updateTextResult("�������:\n"+str);
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