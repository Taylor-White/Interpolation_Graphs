//*******************************************
// UserPanel.java
// 
// Creates the Panel that the user interacts with
//
// Copyright, 2015
// Taylor White
// 4/2/2015
//*******************************************
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class UserPanel {
	
	final private Border blue = BorderFactory.createLineBorder(Color.blue);
    final private List<JTextField> xList = new ArrayList<JTextField>(); 
    final private List<JTextField> yList = new ArrayList<JTextField>();
    final private JPanel updatePanel = new JPanel();

    final private JButton addPoint = new JButton("Add point");
    final private JButton update = new JButton("Update");
    final private JPanel pointPanel = new JPanel();
    final JCheckBox polyCheck = new JCheckBox("Polynomial", true);
    final JCheckBox linearCheck = new JCheckBox("Linear Spline");
    final JCheckBox quadCheck = new JCheckBox("Quadratic Spline");
    final JCheckBox natCubicCheck = new JCheckBox("Natural Cubic Spline");
    final JCheckBox claCubicCheck = new JCheckBox("Clamped Cubic Spline");
    final JTextField dfa = new JTextField("0", 5);
    final JTextField dfn = new JTextField("0", 5);
    final private JTextField domainNeg = new JTextField("", 5);
    final private JTextField domainPos = new JTextField("", 5);
    final JCheckBox linearFitCheck = new JCheckBox("Linear");
    final JCheckBox quadFitCheck = new JCheckBox("Quadratic");
    final JCheckBox cubicFitCheck = new JCheckBox("Cubic");
    final JCheckBox nthFitCheck = new JCheckBox("nth degree");
    final JTextField nthFit = new JTextField("4", 5);
    
    final private JButton random = new JButton("Random Points");
    
    final private List<JPanel> coordinatePanel = new ArrayList<JPanel>(); 

    /* Creates container panel for user input */
	public JPanel createUserPanel(){
		JPanel userPanel = new JPanel();
		userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        JPanel scrollPanel = new JPanel();
        scrollPanel.setLayout(new BoxLayout(scrollPanel, BoxLayout.Y_AXIS));
        scrollPanel.add(this.pointPanel());
        scrollPanel.add(viewPanel());
        scrollPanel.add(bestFitPanel());
        scrollPanel.add(interpolationPanel());
		JScrollPane scrollPane = new JScrollPane(scrollPanel);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(50, 30, 30, 50);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        userPanel.add(scrollPane);
		userPanel.add(updatePanel());
		return userPanel;
	}
	  
	/* Creates panel containing the points */
	private JPanel pointPanel(){
		pointPanel.setBorder(BorderFactory.createTitledBorder(blue, "Points"));
		pointPanel.setLayout(new BoxLayout(pointPanel, BoxLayout.Y_AXIS));  
	    for(int i=1; i<6; ++i){
	    	pointPanel.add(createCoordinatePanel(i, xList, yList, coordinatePanel));
	    } 
	    return pointPanel;
	} 
	
	/* Creates the panel storing information about the view */
	private JPanel viewPanel(){
		JPanel viewPanel = new JPanel();
	    final JLabel dlabel = new JLabel("Domain: ");
	    
	    viewPanel.add(dlabel);
	    viewPanel.add(domainNeg);
	    viewPanel.add(domainPos);
		viewPanel.setBorder(BorderFactory.createTitledBorder(this.blue, "View"));
	    return viewPanel;
	} 
	
	/* Creates the panel with information with the best fit functions */
	private JPanel bestFitPanel(){
		JPanel bestFitPanel = new JPanel();
	    bestFitPanel.setBorder(BorderFactory.createTitledBorder(blue, "Best Fit Type"));
	    bestFitPanel.setLayout(new FlowLayout());
	    JPanel bestFitContainer = new JPanel();
	    JPanel nthFitPanel = new JPanel();
	    JLabel nthFitLabel = new JLabel("n: ");
	    
	    bestFitContainer.setLayout(new BoxLayout(bestFitContainer, BoxLayout.Y_AXIS));
	    nthFitPanel.add(nthFitLabel);
	    nthFitPanel.add(nthFit);
	    bestFitContainer.add(linearFitCheck);
	    bestFitContainer.add(quadFitCheck);
	    bestFitContainer.add(cubicFitCheck);
	    bestFitContainer.add(nthFitCheck);      
	    bestFitContainer.add(nthFitPanel);
	    bestFitPanel.add(bestFitContainer);
	    nthFitPanel.setLayout(new FlowLayout());
	    return bestFitPanel;
	}  
	
	/* Creates the panel with the interpolation functions */
    private JPanel interpolationPanel(){
    	JPanel interpolationPanel = new JPanel();
		interpolationPanel.setBorder(BorderFactory.createTitledBorder(blue, "Interpolation Type"));
		interpolationPanel.setLayout(new FlowLayout()); 
	    final JPanel checkTypeContainer = new JPanel();//Necessary for formatting Type type
        final JPanel df = new JPanel();
        checkTypeContainer.setLayout(new BoxLayout(checkTypeContainer, BoxLayout.Y_AXIS));
	    final JLabel cubicSplineDerivs = new JLabel("df(0), df(n): ");
	    df.add(cubicSplineDerivs);
	    df.add(dfa);
	    df.add(dfn);
	    df.setLayout(new FlowLayout()); 
	    checkTypeContainer.add(polyCheck);
	    checkTypeContainer.add(linearCheck);
	    checkTypeContainer.add(quadCheck);
	    checkTypeContainer.add(natCubicCheck);
	    checkTypeContainer.add(claCubicCheck);
	    checkTypeContainer.add(df);
	    interpolationPanel.add(checkTypeContainer);
	    return interpolationPanel;
    } 	
    
    /* Creates panel with update buttons */
    private JPanel updatePanel(){
    	updatePanel.add(update);
    	updatePanel.add(addPoint);
    	//updatePanel.add(random);
		updatePanel.setBorder(BorderFactory.createTitledBorder(blue, "Update"));
		  
	    return updatePanel;
   }   	

   /* Creates panel containing one set of coordinate x/y pair */
   private JPanel createCoordinatePanel(int counter, List<JTextField> xList, List<JTextField> yList, List<JPanel> xyPanelList){
      JLabel label = new JLabel("Point " + counter);
      
      //Set up field for new x coordinate
      JTextField xField = new JTextField(5);
      xList.add(xField);
      
      //Set up field for new y coordinate
      JTextField yField = new JTextField(5);
      yList.add(yField);      
      
      //Add fields to panel
      JPanel xyPanel = new JPanel();
      xyPanel.setLayout(new FlowLayout());
      xyPanel.add(label);
      xyPanel.add(xField);
      xyPanel.add(yField); 
      xyPanelList.add(xyPanel);
                
      return xyPanel;
   } 
   
   /* */
   public void addToPointPanel(){
	   pointPanel.add(createCoordinatePanel(coordinatePanel.size()+1, xList, yList, coordinatePanel));
	   return;
   }
   
   /* Getters */
   public JButton getAddPoint(){
	return addPoint;
	   
   }
   public JButton getUpdate(){
	return update;
	   
   }
	public JPanel getPointPanel() {
		return pointPanel;
	}	   
	public List<JPanel> getCoordinatePanel() {
		return coordinatePanel;
	}	   
	public List<JTextField> getXList() {
		return xList;
	}	   
	public List<JTextField> getYList() {
		return yList;
	}	 
	public String domainPosToString() {
		return domainPos.getText();
	}	
	public String domainNegToString() {
		return domainNeg.getText();
	}	
	public JCheckBox getLinearFitCheck(){
		return linearFitCheck;
	}
	public JCheckBox getQuadFitCheck(){
		return quadFitCheck;
	}
	public JCheckBox getCubicFitCheck(){
		return cubicFitCheck;
	}
	public JCheckBox getNthFitCheck(){
		return nthFitCheck;
	}
	public JCheckBox getPolyCheck(){
		return polyCheck;
	}
	public JCheckBox getLinearCheck(){
		return linearCheck;
	}
	public JCheckBox getQuadCheck(){
		return quadCheck;
	}
	public JCheckBox getNatCubicCheck(){
		return natCubicCheck;
	}
	public JCheckBox getClampedCubicCheck(){
		return claCubicCheck;
	}
	public String getDFAString(){
		return dfa.getText();
	}
	public String getDFNString(){
		return dfn.getText();
	}
	public String getNthString(){
		return nthFit.getText();
	}
	
	/* Setters */
	public void setDomainPos(double p) {
		domainPos.setText(Double.toString(p));
		return;
	}	
	public void setDomainNeg(double n) {
		domainNeg.setText(Double.toString(n));
		return;
	}	
	
	/* Updates coordinates in UserPanel.java */
	public ArrayList<Coordinates> updateCoordinates() {
		ArrayList <Coordinates> coordinates = new ArrayList<Coordinates>();
		for(int i=0;i<xList.size(); ++i){
			try{
	            Coordinates xyCoordinate = new Coordinates(Double.parseDouble(xList.get(i).getText()), Double.parseDouble(yList.get(i).getText()));
	            coordinates.add(xyCoordinate);
			} catch (Exception e){
				System.out.println("Blank or invalid number");
			}
		}
		return coordinates;
	}
}
