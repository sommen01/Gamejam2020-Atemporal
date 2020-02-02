package com.roguelike.game;

import javax.swing.JOptionPane;

import com.roguelike.entities.Prop;

public class PropFrame extends javax.swing.JFrame
{
	private static final long serialVersionUID = -6981234348753891910L;
	private int propId = 0;
	
    public PropFrame(int propId) {
    	this.propId = propId;
        initComponents();
        initFields();
    }
    
    public void initFields()
    {
    	Prop prop = AHME.prop.get(propId);
    	whoAmIText.setText(""+propId);
    	widthText.setText(""+prop.width);
    	heightText.setText(""+prop.height);
    	idText.setText(""+prop.id);
    	String txt = "";
    	for(int i = 0;i < prop.infos.length;i++)
    	{
    		txt += prop.infos[i];
    		if(i != prop.infos.length-1)
    		{
    			txt += ",";
    		}
    	}
    	infosText.setText(txt);
    	genTagText.setText(""+prop.genTag);
    	usableBox.setSelected(prop.usable);
    	this.setTitle("Editing Prop (" + propId + ")");
    }
    
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        whoAmIText = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        idText = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        infosText = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        genTagText = new javax.swing.JTextField();
        usableBox = new javax.swing.JCheckBox();
        applyButton = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        widthText = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        heightText = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);

        jLabel1.setText("ID:");

        whoAmIText.setEditable(false);

        jLabel2.setText("Type:");

        jLabel3.setText("Infos:");

        infosText.setColumns(20);
        infosText.setRows(5);
        jScrollPane1.setViewportView(infosText);

        jLabel4.setText("Gen. Tag:");

        usableBox.setText("Usable");

        applyButton.setText("Apply");
        applyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyButtonActionPerformed(evt);
            }
        });

        jLabel5.setText("Width:");

        jLabel6.setText("Height:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(usableBox)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(whoAmIText, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(applyButton)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(idText)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
                                        .addComponent(genTagText))))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(widthText, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(heightText, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 124, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(whoAmIText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(widthText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(heightText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(idText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(genTagText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usableBox)
                    .addComponent(applyButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }                     

    private void applyButtonActionPerformed(java.awt.event.ActionEvent evt)
    {
    	Prop prop = AHME.prop.get(propId);
    	try
    	{
    		prop.width = Integer.parseInt(widthText.getText());
    		prop.height = Integer.parseInt(heightText.getText());
    		prop.id = Integer.parseInt(idText.getText());
    		String[] infosTxt = infosText.getText().split(",");
    		int[] infos = new int[infosTxt.length];
    		for(int i = 0;i < infos.length;i++)
    		{
    			infos[i] = Integer.parseInt(infosTxt[i]);
    		}
    		prop.infos = infos;
    		prop.genTag = genTagText.getText();
    		prop.usable = usableBox.isSelected();
    		this.dispose();
    	}
    	catch(Exception ex)
    	{
    		JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
    	}
    }
    
    private javax.swing.JButton applyButton;
    private javax.swing.JTextField genTagText;
    private javax.swing.JTextField heightText;
    private javax.swing.JTextField idText;
    private javax.swing.JTextArea infosText;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JCheckBox usableBox;
    private javax.swing.JTextField whoAmIText;
    private javax.swing.JTextField widthText;               
}
