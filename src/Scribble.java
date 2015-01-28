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

public class Scribble extends Application {

	enum LinienArt {
		Kreis, Rechteck, DoppelLinie, Gestrichelt, Linie
	};

	@Override
	public void start(final Stage primaryStage) throws Exception {
		root = new BorderPane();

		final Scene scene = new Scene(root, 800, 400);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Scribble");

		final Label label1 = new Label("Strichstärke");

		slider = new Slider(1, 10, 1);
		slider.setShowTickLabels(true);

		final Label label2 = new Label("Stricharten");

		final Label label3 = new Label("Farbwahl");
		colorPicker = new ColorPicker(Color.BLACK);

		final Button delete = new Button("alles löschen");
		final Button undo = new Button("Zurück");

		final ObservableList<LinienArt> typs = FXCollections
				.observableArrayList(LinienArt.values());

		comboBox = new ComboBox<LinienArt>(typs);
		// mein default Wert
		comboBox.setValue(LinienArt.Linie);

		final VBox sideMenu = new VBox(10, label1, slider, label2, comboBox,
				label3, colorPicker, undo, delete);

		delete.setOnMousePressed((final MouseEvent e) -> {
			deleteGroup(e);
		});

		undo.setOnMousePressed((final MouseEvent e) -> {
			undoGroup(e);
		});

		root.setLeft(sideMenu);

		final Rectangle panel = new Rectangle(600, 400, Color.WHITESMOKE);

		group = new Group(panel);

		root.setCenter(group);

		panel.setOnMousePressed((final MouseEvent e) -> {
			lineStarter(e);
		});

		panel.setOnMouseDragged((final MouseEvent e) -> {
			DragPainter(e);
		});

		primaryStage.show();

	}

	private BorderPane root;
	private Group group;
	private double x, y, lastX, lastY;

	/*
	 * */
	private Slider slider;
	private ComboBox<LinienArt> comboBox;
	private ColorPicker colorPicker;
	private int gestichel;

	private void lineStarter(final MouseEvent e) {
		x = e.getX();
		y = e.getY();
		group.getChildren().add(new Group());
	}

	private void DragPainter(final MouseEvent e) {

		lastX = x;
		lastY = y;

		x = e.getX();
		y = e.getY();

		final LinienArt la = comboBox.getValue();

		if (LinienArt.Rechteck.equals(la)) {
			drawRechteck();
		} else if (LinienArt.Kreis.equals(la)) {
			drawCircle();
		} else if (LinienArt.DoppelLinie.equals(la)) {
			drawDobleLine();
		} else if (LinienArt.Gestrichelt.equals(la)) {
			drawDashedLine();
		} else {
			drawLine();
		}

	}

	/**
	 * 
	 */
	private void drawLine() {
		final Line l = new Line(x, y, lastX, lastY);
		l.setStrokeWidth(slider.getValue());
		l.setStroke(colorPicker.getValue());
		group.getChildren().add(l);
	}

	/**
	 * 
	 */
	private void drawDashedLine() {
		/* Dashed Line */
		if (gestichel % 2 == 0) {
			drawLine();
		}
		gestichel++;
	}

	/**
	 * 
	 */
	private void drawDobleLine() {
		/* Dobble Line */
		final Line l1 = new Line(x, y, lastX, lastY);
		l1.setStrokeWidth(slider.getValue());
		l1.setStroke(colorPicker.getValue());

		final Line l2 = new Line(x, y + slider.getValue() + 5, lastX, lastY
				+ slider.getValue() + 5);
		l2.setStrokeWidth(slider.getValue());
		l2.setStroke(colorPicker.getValue());

		group.getChildren().add(l1);
		group.getChildren().add(l2);
	}

	private void drawCircle() {
		/* Circle */
		final Circle c = new Circle(x, y, slider.getValue(),
				colorPicker.getValue());

		group.getChildren().add(c);
	}

	private void drawRechteck() {
		/* Rectagle */
		final Rectangle r = new Rectangle();
		r.setX(x);
		r.setY(y);
		r.setWidth(slider.getValue() + 2);
		r.setHeight(slider.getValue() + 2);
		r.setFill(colorPicker.getValue());

		group.getChildren().add(r);
	}

	private void deleteGroup(final MouseEvent e) {
		final int lastElement = group.getChildren().size();

		for (int i = (lastElement - 1); i > 0; i--) {
			group.getChildren().remove(i);
		}
	}

	private void undoGroup(final MouseEvent e) {

		final int lastElement = group.getChildren().size();

		// lastElement ist die größe meines Obj
		// Ich lösche alles außer das Rectangle
		for (int i = (lastElement - 1); i > 0; i--) {

			final Node obj = group.getChildren().get(i);
			// löscht auch mein Group element und bricht danach ab
			if (obj instanceof Group) {
				group.getChildren().remove(i);
				break;
			}

			group.getChildren().remove(i);
		}

	}

	public static void main(final String[] args) {
		launch(args);
	}

}