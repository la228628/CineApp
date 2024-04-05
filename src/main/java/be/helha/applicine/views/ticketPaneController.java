package be.helha.applicine.views;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class ticketPaneController {

    @FXML
    private Label dateLabel;

    @FXML
    private ImageView imageMovieView;

    @FXML
    private ImageView qrCodeImageView;

    @FXML
    private Label roomLabel;

    @FXML
    private Label ticketIDLabel;

    @FXML
    private Label ticketTypeLabel;

    @FXML
    private Label timeLabel;

    @FXML
    private Text titleMovieLabel;

    @FXML
    private Label versionMovieLabel;

    public void setTicket(String title, String version, String date, String time, String room, String ticketID, String ticketType) {
    	titleMovieLabel.setText(title);
    	versionMovieLabel.setText(version);
    	dateLabel.setText(date);
    	timeLabel.setText(time);
    	roomLabel.setText(room);
    	ticketIDLabel.setText(ticketID);
    	ticketTypeLabel.setText(ticketType);
    }

    public void setImages(String imageMovie, String qrCode) {
    	imageMovieView.setImage(new javafx.scene.image.Image(imageMovie));
    	qrCodeImageView.setImage(new javafx.scene.image.Image(qrCode));
    }
}

