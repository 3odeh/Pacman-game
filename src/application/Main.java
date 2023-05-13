package application;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Main extends Application {

	private int score = 0;

	@Override
	public void start(Stage stage) {

		BorderPane bp = new BorderPane();

		Label top = new Label("Score :0");
		Label bot = new Label("Time:");
		bp.setTop(top);
		bp.setAlignment(top, Pos.CENTER);

		Pane center = new Pane();
		bp.setCenter(center);

		Circle ball = new Circle(50, 50, 20);
		ball.setStroke(Color.BLACK);
		ball.setFill(Color.RED);
		center.getChildren().add(ball);

		ArrayList<Node> nodes = new ArrayList<>();
		for (int i = 0; i < 9; i++) {

			Random ran = new Random();
			int x = ran.nextInt(150) + 50;
			int y = ran.nextInt(150) + 50;
			if (i < 5) {
				Circle c = new Circle(10);
				nodes.add(c);
				c.setCenterX(x);
				c.setCenterY(y);
				center.getChildren().add(c);
			} else {
				Rectangle r = new Rectangle(20, 20);
				r.setLayoutX(x);
				r.setLayoutY(y);
				nodes.add(r);
				center.getChildren().add(r);
			}
		}

		Scene scene = new Scene(bp, 400, 400);
		stage.setScene(scene);
		stage.show();

		Thread t = new Thread(new Runnable() {
			int x = 0;

			@Override
			public void run() {

				try {
					while (true) {
						x++;

						Platform.runLater(new Runnable() {

							@Override
							public void run() {

								bot.setText("Time:" + x + " s");

							}
						});

						Thread.sleep(1000);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		});
		t.start();

		bp.setAlignment(bot, Pos.CENTER);

		bp.setBottom(bot);

		scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {

				if (e.getCode() == KeyCode.DOWN) {

					ball.setCenterY(ball.getCenterY() + 5);

				} else if (e.getCode() == KeyCode.UP) {
					ball.setCenterY(ball.getCenterY() - 5);
				} else if (e.getCode() == KeyCode.RIGHT) {
					ball.setCenterX(ball.getCenterX() + 5);
				} else if (e.getCode() == KeyCode.LEFT) {
					ball.setCenterX(ball.getCenterX() - 5);
				}

				for (int i = 0; i < nodes.size(); i++) {
					if (nodes.get(i) instanceof Circle) {
						Circle c = (Circle) nodes.get(i);
						Point2D p1 = new Point2D(c.getCenterX(), c.getCenterY());
						Point2D p2 = new Point2D(ball.getCenterX(), ball.getCenterY());
						if (p1.distance(p2) < 10) {
							center.getChildren().remove(c);
							score += 5;
							nodes.remove(c);
							top.setText("Score :" + score);
						}

					} else {
						Rectangle r = (Rectangle) nodes.get(i);
						Point2D p1 = new Point2D(r.getLayoutX(), r.getLayoutY());
						Point2D p2 = new Point2D(ball.getCenterX(), ball.getCenterY());
						if (p1.distance(p2) < 10) {
							center.getChildren().remove(r);
							score += 10;
							nodes.remove(r);
							top.setText("Score :" + score);
						}
					}
				}

				if (score == 65) {
					t.stop();
				}

			}
		});
	}

	public static void main(String[] args) {
		launch(args);

	}
}
