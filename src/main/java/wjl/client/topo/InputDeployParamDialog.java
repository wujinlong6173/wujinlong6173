package wjl.client.topo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import wjl.util.ErrorType;
import wjl.util.YamlLoader;

/**
 * 部署设备或链路时，选择供应商，输入供应商需要的参数
 */
class InputDeployParamDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    // 用户输入的文本
    private JTextArea textInput;
    // 让用户选择供应商名称
    private JComboBox<String> providerList;
    
    private boolean confirm;
    private Map<String,Object> inputs;

    /**
     * 让用户输入数据
     * 
     * @return 返回真表示用户确认输入，否则表示取消
     */
    public boolean acquireInputs() {
        confirm = false;
        this.setVisible(true);
        return confirm;
    }

    public Map<String,Object> getInputs() {
        return inputs;
    }

    public String getSelectedProvider() {
        return (String)providerList.getSelectedItem();
    }
    
    public InputDeployParamDialog() {
        super();
        setTitle("选择供应商并输入参数");
        setModal(true); // 必须关闭此对话框才能执行其它操作
        this.setLocationByPlatform(true); // 避免对话框显示在屏幕角落
        
        setLayout(new BorderLayout(10,5)); // 数字表示水平间距和垂直间距
        
        
        JPanel panelProvider = new JPanel(new FlowLayout());
        panelProvider.add(new Label("供应商"));
        providerList = new JComboBox<>();
        panelProvider.add(providerList);
        this.add(panelProvider, BorderLayout.NORTH);
        
        
        JPanel panelSchema = new JPanel(new BorderLayout());
        panelSchema.add(new Label("参数格式"), BorderLayout.NORTH);
        JTextArea textSchema = new JTextArea();
        textSchema.setEditable(false);
        textSchema.setBackground(Color.LIGHT_GRAY);
        textSchema.setText("name: {type: string, required: true, flag: CR}\n" +
                "host: {type: string, required: true, flag: CR}\n");
        JScrollPane scrollSchema = new JScrollPane(textSchema);
        scrollSchema.setPreferredSize(new Dimension(300, 200));
        panelSchema.add(scrollSchema, BorderLayout.CENTER);
        this.add(panelSchema, BorderLayout.EAST);
        
        
        JPanel panelInput = new JPanel(new BorderLayout());
        panelInput.add(new Label("填写参数(YAML格式)"), BorderLayout.NORTH);
        textInput = new JTextArea();
        JScrollPane scrollInput = new JScrollPane(textInput);
        scrollInput.setPreferredSize(new Dimension(500, 200));
        panelInput.add(scrollInput, BorderLayout.CENTER);
        this.add(panelInput, BorderLayout.CENTER);
        
        
        JPanel panelBtn = new JPanel(new FlowLayout());
        JButton ok = new JButton("确定");
        JButton cancel = new JButton("取消");
        panelBtn.add(ok);
        panelBtn.add(cancel);
        this.add(panelBtn, BorderLayout.SOUTH);
        pack(); // 完成界面组件的布局
        
        
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String raw = textInput.getText();
                    inputs = (Map<String,Object>)YamlLoader.str2obj(raw, Map.class);
                    confirm = true;
                    InputDeployParamDialog.this.dispose();
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(null, e1.getMessage(), 
                            ErrorType.INPUT_ERROR.getDesc(), JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InputDeployParamDialog.this.dispose();
            }
        });
    }

    public void setProviders(Set<String> listProviders) {
        for (String providerName : listProviders) {
            providerList.addItem(providerName);
        }
    }
}
