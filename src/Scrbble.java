

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Scrbble extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		root = new BorderPane();
		
		Scene scene = new Scene(root,800,400);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Scribble");
		
		Label label1 = new Label("Strichstärke");
		
		slider = new Slider(1,10,1);
		slider.setShowTickLabels(true);
		
		Label label2 = new Label("Stricharten");
		
		Label label3 = new Label("Farbwahl");
		colorPicker = new ColorPicker(Color.BLACK);
		
		Button delete = new Button("alles löschen");
		Button undo = new Button("Zurück");
		
		ObservableList<String> typs = FXCollections.observableArrayList( 
				"Line",
				"Line gestrichelt",
				"doppelte Line",
				"Kreis",
				"Rechteck"
		);
		
		comboBox = new ComboBox<String>(typs);
		// mein default Wert
		comboBox.setValue("Line");
		
		VBox sideMenu = new VBox(10,label1,slider,label2,comboBox,label3,colorPicker,undo,delete);
		
		delete.setOnMousePressed((MouseEvent e) -> {
			deleteGroup(e);
		});
		
		undo.setOnMousePressed((MouseEvent e) -> {
			undoGroup(e);
		});
		
		root.setLeft(sideMenu);

		Rectangle panel = new Rectangle(600,400,Color.WHITESMOKE);
		
		group = new Group(panel);

		root.setCenter(group);
		
		panel.setOnMousePressed((MouseEvent e) -> {
			lineStarter(e);
		});
		
		panel.setOnMouseDragged((MouseEvent e) -> {
			DragPainter(e);
		});
		
		panel.setOnMouseReleased((MouseEvent e) -> {
			LineEnder(e);
		});
		
		primaryStage.show();

	}
	
	private BorderPane root;
	private Group group;
	private double x, y, lastX, lastY;
	
	/*
	 * */
	private Slider slider; 
	private ComboBox<String> comboBox;
	private ColorPicker colorPicker;

	private void lineStarter(MouseEvent e){
		x=e.getX();y=e.getY();
		group.getChildren().add(new Circle(x,y,3,Color.TRANSPARENT));
	}
	
	private void DragPainter(MouseEvent e){
		lastX = x; lastY = y;
		x=e.getX();y=e.getY();
		String s = comboBox.getValue();
		if(s == "Rechteck"){
			/* Rectagle */
			Rectangle r = new Rectangle();
			r.setX(x);
			r.setY(y);
			r.setWidth(slider.getValue()+2);
			r.setHeight(slider.getValue()+2);
			r.setFill(colorPicker.getValue());
			
			group.getChildren().add(r);
		}else if (s == "Kreis"){
			/* Circle */
			Circle c = new Circle(x,y,slider.getValue(),colorPicker.getValue());
			
			group.getChildren().add(c);
		}else if (s == "doppelte Line"){
			/* Dobble Line*/
			Line l1 = new Line(x,y,lastX,lastY);
			l1.setStrokeWidth(slider.getValue());
			l1.setStroke(colorPicker.getValue());
			
			Line l2 = new Line(x,y+slider.getValue()+5,lastX,lastY+slider.getValue()+5);
			l2.setStrokeWidth(slider.getValue());
			l2.setStroke(colorPicker.getValue());
			
			group.getChildren().add(l1);
			group.getChildren().add(l2);
		}else if (s == "Line gestrichelt"){
			/* Dashed Line */
			Line l = new Line(x,y,lastX,lastY);
			l.setStrokeWidth(slider.getValue());
			l.setStroke(colorPicker.getValue());
			l.setStrokeDashOffset(2.0);
			
			group.getChildren().add(l);
		}else{
			/* LINE */
			Line l = new Line(x,y,lastX,lastY);
			l.setStrokeWidth(slider.getValue());
			l.setStroke(colorPicker.getValue());
			group.getChildren().add(l);
		}

	}
	
	private void LineEnder(MouseEvent e){
		group.getChildren().add(new Circle(lastX,lastY,3,Color.TRANSPARENT));
	}
	
	private void deleteGroup(MouseEvent e){
		int lastElement = group.getChildren().size();

		for(int i = (lastElement-1); i > 0;i--){
			group.getChildren().remove(i);
		}
	}
	
	private void undoGroup(MouseEvent e){
		
		int lastElement = group.getChildren().size();
		boolean firstCircle = false, delete = false;
		
		for(int i = (lastElement-1); i > 0;i--){
			
			Node obj = group.getChildren().get(i);

			if(obj instanceof Circle && firstCircle == false){
				group.getChildren().remove(i);
				firstCircle = true;
				delete = true;
			}else if(obj instanceof Circle && firstCircle == true){
				group.getChildren().remove(i);
				delete = false;
				break;
			}else if(delete){
				group.getChildren().remove(i);
			}
		}
		
	}
	
	public static void main(String[] args){
		launch(args);
	}
	

}
