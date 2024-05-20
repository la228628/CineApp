package be.helha.applicine.client.views;

import be.helha.applicine.common.models.Ticket;
import be.helha.applicine.common.models.Viewable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import java.net.URL;

public class TicketPaneViewController {

    @FXML
    private Label dateLabel;

    @FXML
    private ImageView imageMovieView;

    @FXML
    private ImageView qrCodeImageView;

    @FXML
    private Label roomLabel;

    @FXML
    private Label ticketVerificationCode;

    @FXML
    private Label ticketTypeLabel;

    @FXML
    private Label timeLabel;

    @FXML
    private Text titleMovieLabel;

    @FXML
    private Label versionMovieLabel;

    public void setTicket(Ticket ticket) {
        dateLabel.setText(ticket.getDate());
        roomLabel.setText("Room " + ticket.getRoom());
        ticketVerificationCode.setText(ticket.getTicketVerificationCode());
        ticketTypeLabel.setText(ticket.getType());
        timeLabel.setText(ticket.getTime());
        titleMovieLabel.setText(ticket.getMovieTitle());
        versionMovieLabel.setText(ticket.getMovieVersion());
        Viewable movie = ticket.getMovie();
        setImages(movie.getImagePath());
    }

    public void setImages(String imageMovie) {
        imageMovieView.setImage(new Image(imageMovie));
    }

    public static URL getFXMLResource() {
        return TicketPaneViewController.class.getResource("/be/helha/applicine/client/views/components/ticketPane.fxml");
    }
}

